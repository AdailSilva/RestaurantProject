package com.adailsilva.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Getter
	@Setter
	@Column(nullable = false, length = 20)
	@NotBlank(message = "Descrição : precisa ser preenchido.")
	private String descricao;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Rua : precisa ser preenchido.")
	private String rua;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Bairro : precisa ser preenchido.")
	private String bairro;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Número : precisa ser preenchido.")
	private String numero;

	@Getter
	@Setter
	@Column
	@NotBlank(message = "Complemento : precisa ser preenchido.")
	private String complemento;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Cep : precisa ser preenchido.")
	private String cep;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "pessoa_id", nullable = true)
	private Pessoa pessoa;

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
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
