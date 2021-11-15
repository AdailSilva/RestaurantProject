package com.projeto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

	public enum Status {
		ANDAMENTO, CANCELADO, PAGO, ENVIADO
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
	@ManyToOne
	private Pessoa funcionario;

	@Getter
	@Setter
	@ManyToOne
	private Pessoa cliente;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Getter
	@Setter
	@Column(nullable = false)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Getter
	@Setter
	@ManyToOne
	private Mesa mesa;

	@Getter
	@Setter
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Getter
	@Setter
	@Column(nullable = false)
	private Boolean delivery;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "endereco_entrega", nullable = true)
	private Endereco enderecoEntrega;

	@Getter
	@Setter
	private String observacaoCliente;

	@Getter
	@Setter
	private String observacaoFuncionario;

	@Getter
	@Setter
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> produtos = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Transient
	public void calcularSubtotal() {
		this.setValorTotal(BigDecimal.ZERO);
		produtos.forEach(s -> {
			setValorTotal(getValorTotal().add(s.getValorTotal()));
			System.out.println(s.getProduto().getDescricao() + "  qtd  " + s.getQuantidade() + " preco "
					+ s.getProduto().getPreco() + " " + s.getValorTotal());
		});

		System.out.println("valor total " + getValorTotal());
	}

	@Transient
	public String getDataFormatada() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		return df.format(data);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
