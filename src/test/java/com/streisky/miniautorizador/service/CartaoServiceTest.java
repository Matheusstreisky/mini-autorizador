package com.streisky.miniautorizador.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streisky.miniautorizador.controller.form.CartaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.repository.CartaoRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartaoServiceTest {
	
	@Mock
	private CartaoRepository cartaoRepository;
	
	@InjectMocks
	private CartaoServiceImpl cartaoService;
	
	private static final String NUMERO_CARTAO = "1234567890987654";
	private static final String SENHA = "321";
	private final Cartao cartao = new Cartao(NUMERO_CARTAO, SENHA);
	
	@Test
	void testCriarCartao() {
		Mockito.when(cartaoRepository.save(cartao)).thenReturn(cartao);
		Cartao cartaoNovo = cartaoService.criarCartao(new CartaoForm(NUMERO_CARTAO, SENHA));
		
		Mockito.verify(cartaoRepository, Mockito.times(1)).save(cartao);
		assertEquals(cartao, cartaoNovo);
	}
	
	@Test
	void testCriarCartaoRepetido() {
		Mockito.when(cartaoRepository.save(cartao)).thenReturn(cartao);
		cartaoService.criarCartao(new CartaoForm(NUMERO_CARTAO, SENHA));
		
		Mockito.when(cartaoRepository.save(cartao)).thenThrow(DataIntegrityViolationException.class);
		assertThrows(DataIntegrityViolationException.class,
				() -> cartaoService.criarCartao(new CartaoForm(NUMERO_CARTAO, SENHA)));
	}
	
	@Test
	void testObterSaldo() {
		try {
			Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
			String saldo = cartaoService.obterSaldoCartao(NUMERO_CARTAO);
			
			Mockito.verify(cartaoRepository, Mockito.times(1)).findByNumeroCartao(NUMERO_CARTAO);
			assertEquals(Double.toString(500.00), saldo);
		} catch (CartaoInexistenteException e) {
			assertTrue(false);
		}
	}
	
	@Test
	void testObterSaldoCartaoInexistente() {
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.empty());
		assertThrows(CartaoInexistenteException.class,
				() -> cartaoService.obterSaldoCartao(NUMERO_CARTAO));
	}

}
