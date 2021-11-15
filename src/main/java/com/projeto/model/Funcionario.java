package com.projeto.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="funcionario")
@PrimaryKeyJoinColumn(name="idPessoa")
public class Funcionario extends Pessoa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date admissao;

	@Getter
	@Setter
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date demissao;

	

}
