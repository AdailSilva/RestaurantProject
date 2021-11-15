package com.adailsilva.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {

	public enum StatusIP {
		ANDAMENTO, CANCELADO, RETIRADA, FINALIZADO
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
	private Integer quantidade = 0;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Getter
	@Setter
	@Column(nullable = false)
	private BigDecimal valorUnitario = BigDecimal.ZERO;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;
	
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "funcionario_id", nullable = true)
	private Pessoa funcionario;

	
	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "pedido_id", nullable = false)
	private Pedido pedido;

	@Getter
	@Setter
	@Column(nullable = false)
	private Integer sequencia;

	@Getter
	@Setter
	@Column(nullable = false, name = "status_ip")
	@Enumerated(EnumType.STRING)
	private StatusIP statusIp;

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
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getValorTotal() {
		return this.getValorUnitario().multiply(new BigDecimal(this.getQuantidade()));
	}

	@Override
	public String toString() {
		return "ItemPedido [id=" + id + ", quantidade=" + quantidade + ", data=" + data + ", valorUnitario="
				+ valorUnitario + ", produto=" + produto + ", pedido=" + pedido + ", sequencia=" + sequencia
				+ ", statusIp=" + statusIp + "]";
	}

}
