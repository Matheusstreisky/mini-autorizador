package com.streisky.miniautorizador.controller.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TransacaoForm {
	
	@NotNull
	@NotEmpty
	@Length(min = 16, max = 16)
	private String numeroCartao;
	
	@NotNull
	@NotEmpty
	private String senhaCartao;
	
	@Min(0)
	private Double valor;

	public TransacaoForm() {
	}

	public TransacaoForm(String numeroCartao, String senhaCartao, Double valor) {
		super();
		this.numeroCartao = numeroCartao;
		this.senhaCartao = senhaCartao;
		this.valor = valor;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getSenhaCartao() {
		return senhaCartao;
	}

	public void setSenhaCartao(String senhaCartao) {
		this.senhaCartao = senhaCartao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
