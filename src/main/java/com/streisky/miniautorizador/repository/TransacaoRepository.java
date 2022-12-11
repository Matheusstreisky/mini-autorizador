package com.streisky.miniautorizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streisky.miniautorizador.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
