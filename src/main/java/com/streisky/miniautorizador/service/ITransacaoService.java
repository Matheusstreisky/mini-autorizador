package com.streisky.miniautorizador.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.streisky.miniautorizador.controller.form.TransacaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.exception.SaldoInsuficienteException;
import com.streisky.miniautorizador.exception.SenhaInvalidaException;

import jakarta.validation.Valid;

public interface ITransacaoService {

	public String debitarCartao(@RequestBody @Valid TransacaoForm transacaoForm)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException;
	
	public String creditarCartao(@RequestBody @Valid TransacaoForm transacaoForm)
			throws CartaoInexistenteException, SenhaInvalidaException, SaldoInsuficienteException;
}
