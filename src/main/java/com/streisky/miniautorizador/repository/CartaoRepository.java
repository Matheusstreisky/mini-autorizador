package com.streisky.miniautorizador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streisky.miniautorizador.model.Cartao;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

	public Optional<Cartao> findByNumeroCartao(String numeroCartao);
}