package com.adailsilva.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bonus_cliente")
public class BonusCliente implements Serializable {

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
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@Getter
	@Setter
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUso;
}
