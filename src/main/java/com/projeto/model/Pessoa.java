package com.projeto.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {

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
	@JoinColumn(name = "id_restaurante", nullable = false)
	private Restaurante restaurante;

	@Getter
	@Setter
	@Column(nullable = false)
	private String nome;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = ": precisa ser preenchido.")
	private String sobrenome;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotNull(message = ": precisa ser preenchido.")
	private Date nascimento;

	@Getter
	@Setter
	@Column(nullable = false)
	private String cpf;

	@Getter
	@Setter
	@Column(nullable = false)
	private String telefone;

	@Getter
	@Setter
	@Column(nullable = false)
	@ManyToMany
	@JoinTable(name = "acesso_pessoa", joinColumns = @JoinColumn(name = "funcionario_id"), inverseJoinColumns = @JoinColumn(name = "acesso_id"))
	private List<Acesso> acesso = new ArrayList<>();

	@Getter
	@Setter
	@Column(nullable = false,unique = true)
	private String usuario;

	@Getter
	@Setter
	@Column(nullable = false)
	private String senha;

	@Getter
	@Setter
	@OneToMany(mappedBy = "pessoa")
	private List<Endereco> enderecos = new ArrayList<>();

	@Getter
	@Setter
	@Column(nullable = true)
	private String email;

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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public boolean temAcesso(String acesso) {
		for (Acesso a : this.acesso) {
			if (acesso.equalsIgnoreCase(a.getDescricao())) {
				return true;
			}
		}

		return false;
	}

}
