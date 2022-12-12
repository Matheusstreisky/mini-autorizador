package com.streisky.miniautorizador.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.model.OperacaoCartao;
import com.streisky.miniautorizador.model.Transacao;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransacaoRepositoryTest {
	
	@Autowired
    private TestEntityManager entityManager;
	
	private static final String NUMERO_CARTAO = "1234567890987654";
	private static final String SENHA = "321";
	private final Cartao cartao = new Cartao(NUMERO_CARTAO, SENHA);
	private final Transacao transacao = new Transacao(cartao, OperacaoCartao.CREDITO, 500.0);
	
	@Test
	void testSaveValorNegativo() {
		transacao.setValor(-50.0);
		
		assertThrows(Exception.class, () -> {
			entityManager.persist(transacao);
		});
	}
}
