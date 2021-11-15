package com.adailsilva.filter;

import java.io.Serializable;
import java.math.BigDecimal;

import com.adailsilva.model.Categoria;

import lombok.Getter;
import lombok.Setter;

public class ProdutoFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	private Categoria categoria;

	@Getter
	@Setter
	private String descricao;

	@Getter
	@Setter
	private BigDecimal precoInicial;

	@Getter
	@Setter
	private BigDecimal precoFinal;

	@Getter
	@Setter
	private BigDecimal custoInicial;

	@Getter
	@Setter
	private BigDecimal custoFinal;

}
