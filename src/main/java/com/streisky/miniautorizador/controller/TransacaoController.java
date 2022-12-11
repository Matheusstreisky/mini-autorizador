package com.streisky.miniautorizador.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	@PostMapping
	public ResponseEntity<String> debitarCartao(@RequestBody @Valid TransacaoForm transacaoForm) {
		return realizarTransacao(transacaoForm, OperacaoCartao.DEBITO);
	}
	
	@PostMapping("/credito")
	public ResponseEntity<String> creditarCartao(@RequestBody @Valid TransacaoForm transacaoForm) {
		return realizarTransacao(transacaoForm, OperacaoCartao.CREDITO);
	}
	
	private ResponseEntity<String> realizarTransacao(TransacaoForm transacaoForm, OperacaoCartao operacaoCartao) {
		try {
			Cartao cartao = validarInformacoesCartao(transacaoForm);
			
			Transacao transacao = new Transacao(cartao, operacaoCartao, transacaoForm.getValor());
			transacaoRepository.save(transacao);
			
			atualizarSaldo(cartao, transacaoForm, operacaoCartao);
			cartaoRepository.save(cartao);
			
			return ResponseEntity.created(null).body("OK");
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}
	
	private Cartao validarInformacoesCartao(TransacaoForm transacaoForm) throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException {
		Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(transacaoForm.getNumeroCartao());
		
		if (optional.isPresent()) {
			Cartao cartao = optional.get();
			
			if (!verificarSenhaCartao(cartao, transacaoForm))
				throw new SenhaInvalidaException();
			
			if (!verificarSaldoCartao(cartao, transacaoForm))
				throw new SaldoInsuficienteException();
			
			return cartao;
		}
		
		throw new CartaoInexistenteException();
	}
	
	private boolean verificarSenhaCartao(Cartao cartao, TransacaoForm transacaoForm) {
		return cartao.getSenha().equals(transacaoForm.getSenhaCartao());
	}
	
	private boolean verificarSaldoCartao(Cartao cartao, TransacaoForm transacaoForm) {
		return cartao.getSaldo() > transacaoForm.getValor();
	}
	
	private void atualizarSaldo(Cartao cartao, TransacaoForm transacaoForm, OperacaoCartao operacaoCartao) {
		cartao.setSaldo(
				(operacaoCartao.equals(OperacaoCartao.CREDITO)) 
				? cartao.getSaldo() + transacaoForm.getValor()
				: cartao.getSaldo() - transacaoForm.getValor()
				);
	}
}
