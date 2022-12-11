package com.streisky.miniautorizador.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.streisky.miniautorizador.controller.form.CartaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.repository.CartaoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@PostMapping
	public ResponseEntity<CartaoForm> criarCartao(@RequestBody @Valid CartaoForm cartaoForm, UriComponentsBuilder uriBuilder) {
		try {
			Cartao cartao = cartaoForm.toCartao();
			cartaoRepository.save(cartao);
			
			URI uri = uriBuilder.path("/cartoes/{numeroCartao}").buildAndExpand(cartao.getNumeroCartao()).toUri();
			return ResponseEntity.created(uri).body(cartaoForm);			
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.unprocessableEntity().body(cartaoForm);
		}
	}
	
	@GetMapping("/{numeroCartao}")
	public ResponseEntity<String> obterSaldoCartao(@PathVariable String numeroCartao) {
		try {
			Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(numeroCartao);
			
			return optional
					.map(o -> ResponseEntity.ok().body(o.getSaldo().toString()))
					.orElseThrow(() -> new CartaoInexistenteException());
		} catch (CartaoInexistenteException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
