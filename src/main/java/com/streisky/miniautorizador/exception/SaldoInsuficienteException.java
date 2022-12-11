package com.streisky.miniautorizador.exception;

public class SaldoInsuficienteException extends Exception {

	private static final long serialVersionUID = -8608788089694529360L;

	public SaldoInsuficienteException() {
		super("SALDO_INSUFICIENTE");
	}
}
