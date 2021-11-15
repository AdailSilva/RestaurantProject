package com.adailsilva.model;

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
@Table(name = "grupo_mesas")
public class GrupoMesas {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "mesa_id", nullable = false)
	private Mesa mesaSelecionada;

	@Getter
	@Setter
	@Column(nullable = false, name = "numero_mesas")
	private String numeroMesas;

	@Getter
	@Setter
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	@Getter
	@Setter
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;

}
