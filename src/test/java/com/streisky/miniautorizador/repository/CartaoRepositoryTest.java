package com.streisky.miniautorizador.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streisky.miniautorizador.model.Cartao;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartaoRepositoryTest {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
    private TestEntityManager entityManager;
	
	private static final String NUMERO_CARTAO = "1234567890987654";
	private static final String SENHA = "321";
	private final Cartao cartao = new Cartao(NUMERO_CARTAO, SENHA);
	
	@Test
	void testfindByNumeroCartao() {
		entityManager.persist(cartao);
		
		Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(NUMERO_CARTAO);
		assertNotNull(optional.get());
	}
	
	@Test
	void testfindByNumeroCartaoInexistente() {
		entityManager.persist(cartao);
		
		Optional<Cartao> optional = cartaoRepository.findByNumeroCartao(NUMERO_CARTAO + "1");
		assertFalse(optional.isPresent());
	}
	
	@Test
	void testSaveNumeroCartaoRepetido() {
		Cartao cartaoNovo = new Cartao(NUMERO_CARTAO, SENHA);
		
		assertThrows(Exception.class, () -> {
			entityManager.persist(cartao);
			entityManager.persist(cartaoNovo);
		});
	}
	
	@Test
	void testSaveNumeroSaldoNegativo() {
		cartao.setSaldo(-50.0);
		
		assertThrows(Exception.class, () -> {
			entityManager.persist(cartao);
		});
	}
}
