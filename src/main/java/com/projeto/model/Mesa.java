package com.projeto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mesa")
public class Mesa implements Serializable {

	public enum StatusReserva {
		LIVRE, RESERVADA, OCUPADA
	}

	public enum StatusRelacionamento {
		RELACIONADAPAI, RELACIONADAFILHA
	}

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
	@Column(nullable = false)
	private Integer qtdCadeiras;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "id_restaurante")
	private Restaurante restaurante;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20, nullable = false)
	@Getter
	@Setter
	private StatusReserva status;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_relacionamento", length = 20, nullable = true)
	@Getter
	@Setter
	private StatusRelacionamento statusRelacionamento;

	@Column(name = "numero", length = 20, nullable = false)
	@Getter
	@Setter
	private String numeroMesa;

	@Getter
	@Setter
	@OneToOne
	private GrupoMesas relacionamento;

	@Getter
	@Setter
	@OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Reserva> reservas = new ArrayList<>();

	

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
		Mesa other = (Mesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
