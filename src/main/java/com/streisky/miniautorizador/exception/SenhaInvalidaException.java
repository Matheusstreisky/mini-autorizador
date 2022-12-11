package com.streisky.miniautorizador.exception;

public class SenhaInvalidaException extends Exception {
	
	private static final long serialVersionUID = -6818597332055458500L;

	public SenhaInvalidaException() {
		super("SENHA_INVALIDA");
	}
}
