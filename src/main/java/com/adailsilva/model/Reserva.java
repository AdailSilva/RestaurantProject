package com.adailsilva.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reserva")
public class Reserva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "id_restaurante")
	private Restaurante restaurante;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "id_pessoa")
	private Pessoa pessoa;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "id_mesa")
	private Mesa mesa;

	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "Data : precisa ser preenchido . ")
	private Date data;

	@Getter
	@Setter
	@NotNull
	private boolean ativo;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
