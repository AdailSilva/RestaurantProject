package com.projeto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.projeto.model.Pedido.Status;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meta")
public class Meta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum StatusMeta {
		ANDAMENTO, FINALIZADO
	}

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Getter
	@Setter
	@OneToOne
	@JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Data inicial precisa ser preenchida.")
	private Date dataInicial;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Data final precisa ser preenchida.")
	private Date dataFinal;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotNull(message = "Valor total :  precisa ser preenchido.")
	private BigDecimal valorTotal;

	@Getter
	@Setter
	@Column(nullable = true)
	private BigDecimal valorCorrente = BigDecimal.ZERO;

	@Getter
	@Setter
	@ManyToOne
	private Pessoa funcionario;

	@Getter
	@Setter
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusMeta status;

	@Transient
	public Double getPorcentagem() {
		return ((100 * valorCorrente.doubleValue()) / valorTotal.doubleValue());
	}

}
