package com.streisky.miniautorizador.controller;

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
import com.streisky.miniautorizador.service.TransacaoServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
	
	@Autowired
	private TransacaoServiceImpl transacaoService;
	
	@PostMapping
	public ResponseEntity<String> debitarCartao(@RequestBody @Valid TransacaoForm transacaoForm) {
		try {
			String msg = transacaoService.debitarCartao(transacaoForm);
			return ResponseEntity.created(null).body(msg);
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}
	
	@PostMapping("/credito")
	public ResponseEntity<String> creditarCartao(@RequestBody @Valid TransacaoForm transacaoForm) {
		try {
			String msg = transacaoService.creditarCartao(transacaoForm);
			return ResponseEntity.created(null).body(msg);
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}
}
