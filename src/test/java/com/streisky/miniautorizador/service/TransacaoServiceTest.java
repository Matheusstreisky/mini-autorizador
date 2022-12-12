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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.streisky.miniautorizador.controller.form.TransacaoForm;
import com.streisky.miniautorizador.exception.CartaoInexistenteException;
import com.streisky.miniautorizador.exception.SaldoInsuficienteException;
import com.streisky.miniautorizador.exception.SenhaInvalidaException;
import com.streisky.miniautorizador.model.Cartao;
import com.streisky.miniautorizador.model.OperacaoCartao;
import com.streisky.miniautorizador.model.Transacao;
import com.streisky.miniautorizador.repository.CartaoRepository;
import com.streisky.miniautorizador.repository.TransacaoRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransacaoServiceTest {

	@Mock
	private CartaoRepository cartaoRepository;
	
	@Mock
	private TransacaoRepository transacaoRepository;
	
	@InjectMocks
	private TransacaoServiceImpl transacaoService;
	
	private static final String NUMERO_CARTAO = "1234567890987654";
	private static final String SENHA = "321";
	private static final Double VALOR = 50.0;
	private static final String MSG_OK = "OK";
	
	private final Cartao cartao = new Cartao(NUMERO_CARTAO, SENHA);
	private final Transacao transacaoDebito = new Transacao(cartao, OperacaoCartao.DEBITO, VALOR);
	private final Transacao transacaoCredito = new Transacao(cartao, OperacaoCartao.CREDITO, VALOR);
	private final TransacaoForm transacaoForm = new TransacaoForm(NUMERO_CARTAO, SENHA, VALOR);
	
	@Test
	void debitarCartao() {
		try {
			Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
			Mockito.when(transacaoRepository.save(transacaoDebito)).thenReturn(transacaoDebito);
			Mockito.when(cartaoRepository.save(cartao)).thenReturn(cartao);
			String msg = transacaoService.debitarCartao(transacaoForm);
			
			Mockito.verify(cartaoRepository, Mockito.times(1)).findByNumeroCartao(NUMERO_CARTAO);
			Mockito.verify(transacaoRepository, Mockito.times(1)).save(transacaoDebito);
			Mockito.verify(cartaoRepository, Mockito.times(1)).save(cartao);
			
			assertEquals(MSG_OK, msg);
			assertEquals(450.0, cartao.getSaldo());
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			assertTrue(false);
		}
	}
	
	@Test
	void creditarCartao() {
		try {
			Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
			Mockito.when(transacaoRepository.save(transacaoCredito)).thenReturn(transacaoCredito);
			Mockito.when(cartaoRepository.save(cartao)).thenReturn(cartao);
			String msg = transacaoService.creditarCartao(transacaoForm);
			
			Mockito.verify(cartaoRepository, Mockito.times(1)).findByNumeroCartao(NUMERO_CARTAO);
			Mockito.verify(transacaoRepository, Mockito.times(1)).save(transacaoCredito);
			Mockito.verify(cartaoRepository, Mockito.times(1)).save(cartao);
			
			assertEquals(MSG_OK, msg);
			assertEquals(550.0, cartao.getSaldo());
		} catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
			assertTrue(false);
		}
	}
	
	@Test
	void debitarCartaoCartaoInexistente() {
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.empty());
		assertThrows(CartaoInexistenteException.class,
				() -> transacaoService.debitarCartao(transacaoForm));
	}
	
	@Test
	void debitarCartaoSenhaInvalida() {
		transacaoForm.setSenhaCartao(SENHA + "321");
		
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
		assertThrows(SenhaInvalidaException.class,
				() -> transacaoService.debitarCartao(transacaoForm));
	}
	
	@Test
	void debitarCartaoSaldoInsuficiente() {
		transacaoForm.setValor(1000.0);
		
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
		assertThrows(SaldoInsuficienteException.class,
				() -> transacaoService.debitarCartao(transacaoForm));
	}
	
	@Test
	void creditarCartaoCartaoInexistente() {
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.empty());
		assertThrows(CartaoInexistenteException.class,
				() -> transacaoService.creditarCartao(transacaoForm));
	}
	
	@Test
	void creditarCartaoSenhaInvalida() {
		transacaoForm.setSenhaCartao(SENHA + "321");

		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
		assertThrows(SenhaInvalidaException.class,
				() -> transacaoService.creditarCartao(transacaoForm));
	}
	
	@Test
	void creditarCartaoSaldoInsuficiente() {
		transacaoForm.setValor(0.0);
		
		Mockito.when(cartaoRepository.findByNumeroCartao(NUMERO_CARTAO)).thenReturn(Optional.of(cartao));
		assertThrows(SaldoInsuficienteException.class,
				() -> transacaoService.creditarCartao(transacaoForm));
	}
}
