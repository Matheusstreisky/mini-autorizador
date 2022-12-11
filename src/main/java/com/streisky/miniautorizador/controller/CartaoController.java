package com.streisky.miniautorizador.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.streisky.miniautorizador.controller.form.CartaoForm;
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

}
