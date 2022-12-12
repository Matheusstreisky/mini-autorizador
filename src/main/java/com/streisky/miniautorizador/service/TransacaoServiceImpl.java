package com.streisky.miniautorizador.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import com.streisky.miniautorizador.controller.form.TransacaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.exception.SaldoInsuficienteException;
import com.streisky.miniautorizador.exception.SenhaInvalidaException;
import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.model.OperacaoCartao;
import com.streisky.miniautorizador.model.Transacao;
import com.streisky.miniautorizador.repository.CartaoRepository;
import com.streisky.miniautorizador.repository.TransacaoRepository;

import jakarta.validation.Valid;

@Service
public class TransacaoServiceImpl implements ITransacaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@Override
	public String debitarCartao(@RequestBody @Valid TransacaoForm transacaoForm)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		return realizarTransacao(transacaoForm, OperacaoCartao.DEBITO);
	}
	
	@Override
	public String creditarCartao(@RequestBody @Valid TransacaoForm transacaoForm)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		return realizarTransacao(transacaoForm, OperacaoCartao.CREDITO);
	}
	
	private String realizarTransacao(TransacaoForm transacaoForm, OperacaoCartao operacaoCartao)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		Cartao cartao = validarInformacoesCartao(transacaoForm, operacaoCartao);
			
		Transacao transacao = new Transacao(cartao, operacaoCartao, transacaoForm.getValor());
		transacaoRepository.save(transacao);
		
		atualizarSaldo(cartao, transacaoForm, operacaoCartao);
		cartaoRepository.save(cartao);
		
		return "OK";
	}
	
	private Cartao validarInformacoesCartao(TransacaoForm transacaoForm, OperacaoCartao operacaoCartao)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(transacaoForm.getNumeroCartao());
		
		verificarCartaoExistente(optional);
		
		Cartao cartao = optional.get();
		verificarSenhaCartao(cartao, transacaoForm);
		verificarSaldoCartao(cartao, transacaoForm, operacaoCartao);
		
		return cartao;
	}
	
	private void verificarCartaoExistente(Optional<Cartao> optional) throws CartaoInexistenteException {
		try {
			optional.get();
		} catch (NoSuchElementException e) {
			throw new CartaoInexistenteException();
		}
	}
	
	private void verificarSenhaCartao(Cartao cartao, TransacaoForm transacaoForm) throws SenhaInvalidaException {
		try {
			Assert.isTrue(cartao.getSenha().equals(transacaoForm.getSenhaCartao()), "");
		} catch (IllegalArgumentException e) {
			throw new SenhaInvalidaException();
		}
	}
	
	private void verificarSaldoCartao(Cartao cartao, TransacaoForm transacaoForm, OperacaoCartao operacaoCartao)
			throws SaldoInsuficienteException {
		try {
			Assert.isTrue(
					(operacaoCartao.equals(OperacaoCartao.DEBITO) && cartao.getSaldo() > transacaoForm.getValor())
					|| operacaoCartao.equals(OperacaoCartao.CREDITO) && transacaoForm.getValor() > 0
					, "");
		} catch (IllegalArgumentException e) {
			throw new SaldoInsuficienteException();
		}
	}
	
	private void atualizarSaldo(Cartao cartao, TransacaoForm transacaoForm, OperacaoCartao operacaoCartao) {
		cartao.setSaldo(
				(operacaoCartao.equals(OperacaoCartao.CREDITO)) 
				? cartao.getSaldo() + transacaoForm.getValor()
				: cartao.getSaldo() - transacaoForm.getValor()
				);
	}
}
