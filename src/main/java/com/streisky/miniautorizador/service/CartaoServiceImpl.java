package com.streisky.miniautorizador.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.streisky.miniautorizador.controller.form.CartaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.repository.CartaoRepository;

@Service
public class CartaoServiceImpl implements ICartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public Cartao criarCartao(CartaoForm cartaoForm) {
		Cartao cartao = cartaoForm.toCartao();
		cartaoRepository.save(cartao);
		
		return cartao;
	}
	
	@Override
	public String obterSaldoCartao(String numeroCartao) throws CartaoInexistenteException {
		Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(numeroCartao);
		
		verificarCartaoExistente(optional);
		return optional.get().getSaldo().toString();
	}
	
	private void verificarCartaoExistente(Optional<Cartao> optional) throws CartaoInexistenteException {
		try {
			Assert.isTrue(optional.isPresent() && !optional.isEmpty(), "");
		} catch (Exception e) {
			throw new CartaoInexistenteException();
		}
	}
}
