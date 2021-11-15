package com.adailsilva.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cliente")
@PrimaryKeyJoinColumn(name = "idPessoa")
public class Cliente extends Pessoa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@OneToMany(mappedBy = "cliente")
	private List<BonusCliente> bonus = new ArrayList<>();

	@Getter
	@Setter
	private Integer qtdPontos;

	@Getter
	@Setter
	@Transient
	private String dataString;

	@Getter
	@Setter
	@Column(nullable = false)
	private Date dataCriacao;

}
