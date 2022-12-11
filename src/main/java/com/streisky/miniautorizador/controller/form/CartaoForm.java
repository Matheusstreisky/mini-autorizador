package com.streisky.miniautorizador.controller.form;

import org.hibernate.validator.constraints.Length;

import com.streisky.miniautorizador.model.Cartao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CartaoForm {
	
	@NotNull
	@NotEmpty
	@Length(min = 16, max = 16)
	private String numeroCartao;
	
	@NotNull
	@NotEmpty
	private String senha;

	public CartaoForm() {
	}

	public CartaoForm(String numeroCartao, String senha) {
		super();
		this.numeroCartao = numeroCartao;
		this.senha = senha;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public String getSenha() {
		return senha;
	}

	public void setSaldo(String senha) {
		this.senha = senha;
	}
	
	public Cartao toCartao() {
		return new Cartao(numeroCartao, senha);
	}
}
