package com.streisky.miniautorizador.service;

import com.streisky.miniautorizador.controller.form.CartaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.model.Cartao;

public interface ICartaoService {

	public Cartao criarCartao(CartaoForm cartaoForm);
	
	public String obterSaldoCartao(String numeroCartao) throws CartaoInexistenteException;
}
