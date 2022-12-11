package com.streisky.miniautorizador.model;

import org.hibernate.annotations.Check;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cartao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@NotNull
	@NotEmpty
	@Length(min = 16, max = 16)
	@Column(unique = true)
	private String numeroCartao;
	
	@NotNull
	@NotEmpty
	private String senha;
	
	@Check(constraints = "saldo >= 0")
	private Double saldo;

	public Cartao() {
	}

	public Cartao(String numeroCartao, String senha) {
		this.numeroCartao = numeroCartao;
		this.senha = senha;
		this.saldo = 500.0;
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

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
}
