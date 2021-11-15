package com.adailsilva.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tipo_pagamento")
public class TipoPagamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Getter
	@Setter
	@OneToOne
	@JoinColumn(name = "cartao_id", nullable = true)
	private Cartao cartao;

	@Getter
	@Setter
	@Column(nullable = true, name = "cod_operacao")
	private String codOperacao;

	@Getter
	@Setter
	private BigDecimal valor;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "pagamento_id")
	private Pagamento pagamento;

	@Override
	public String toString() {
		return "TipoPagamento [id=" + id + ", codOperacao=" + codOperacao + ", valor=" + valor + ", pagamento="
				+ pagamento + "]";
	}

}
