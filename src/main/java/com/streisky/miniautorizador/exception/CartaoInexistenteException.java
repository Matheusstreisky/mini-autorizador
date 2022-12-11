package com.streisky.miniautorizador.exception;

public class CartaoInexistenteException extends Exception {
	
	private static final long serialVersionUID = -4433723002502447143L;

	public CartaoInexistenteException() {
		super("CARTAO_INEXISTENTE");
	}
}
