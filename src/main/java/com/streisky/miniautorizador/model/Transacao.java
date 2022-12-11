package com.streisky.miniautorizador.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Check;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
public class Transacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cartao_id")
	private Cartao cartao;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime data = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	private OperacaoCartao operacaoCartao;
	
	@Check(constraints = "valor >= 0")
	private Double valor;

	public Transacao() {
	}

	public Transacao(Cartao cartao, OperacaoCartao operacaoCartao, Double valor) {
		super();
		this.cartao = cartao;
		this.operacaoCartao = operacaoCartao;
		this.valor = valor;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public OperacaoCartao getOperacaoCartao() {
		return operacaoCartao;
	}

	public void setOperacaoCartao(OperacaoCartao operacaoCartao) {
		this.operacaoCartao = operacaoCartao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
