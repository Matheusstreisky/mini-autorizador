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
	public ResponseEntity<String> realizarTransacao(@RequestBody @Valid TransacaoForm transacaoForm) {
		try {
			Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(transacaoForm.getNumeroCartao());
			
			if (optional.isPresent()) {
				Cartao cartao = optional.get();
				
				if (!verificarSenhaCartao(cartao, transacaoForm))
					throw new SenhaInvalidaException();
				
				if (!verificarSaldoCartao(cartao, transacaoForm))
					throw new SaldoInsuficienteException();
				
				Transacao transacao = new Transacao(cartao, OperacaoCartao.DEBITO, transacaoForm.getValor());
				transacaoRepository.save(transacao);
				
				descontarSaldo(cartao, transacaoForm);
				cartaoRepository.save(cartao);
				
				return ResponseEntity.created(null).body("OK");
			}
			else
				throw new CartaoInexistenteException();
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}
	
	private boolean verificarSenhaCartao(Cartao cartao, TransacaoForm transacaoForm) {
		return cartao.getSenha().equals(transacaoForm.getSenhaCartao());
	}
	
	private boolean verificarSaldoCartao(Cartao cartao, TransacaoForm transacaoForm) {
		return cartao.getSaldo() > transacaoForm.getValor();
	}
	
	private void descontarSaldo(Cartao cartao, TransacaoForm transacaoForm) {
		cartao.setSaldo(cartao.getSaldo() - transacaoForm.getValor());
	}
}
