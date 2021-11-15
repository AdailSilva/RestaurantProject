package com.projeto.filter;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class CategoriaFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String tipo;

	@Getter
	@Setter
	private String descricao;

}
