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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

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
	@JoinColumn(name = "id_categoria", nullable = false)
	@NotNull
	private Categoria categoria;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Descrição : precisa ser preenchido . ")
	private String descricao;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotNull(message = "Preço : precisa ser preenchido . ")
	private BigDecimal preco;

	@Getter
	@Setter
	@Column(nullable = false)
	@NotNull(message = "Custo : precisa ser preenchido . ")
	private BigDecimal custo;

	@Getter
	@Setter
	@Column(nullable = false)
	private String urlImagem;

	@Transient
	@Getter
	@Setter
	private Integer qtd = new Integer(0);

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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
