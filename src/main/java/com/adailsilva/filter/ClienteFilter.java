package com.adailsilva.filter;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class ClienteFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String nome;
	@Getter
	@Setter
	private String sobrenome;
	@Getter
	@Setter
	private String cpf;
	@Getter
	@Setter
	private Date nascimento;
	@Getter
	@Setter
	private String telefone;
	@Getter
	@Setter
	private String usuario;
	

}
