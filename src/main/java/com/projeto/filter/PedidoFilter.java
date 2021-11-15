package com.projeto.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.projeto.model.Cliente;
import com.projeto.model.Funcionario;
import com.projeto.model.Pedido.Status;

import lombok.Getter;
import lombok.Setter;

public class PedidoFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Integer codigo;

	@Getter
	@Setter
	private Cliente cliente;

	@Getter
	@Setter
	private Funcionario funcionario;

	@Getter
	@Setter
	private Status status;

	@Getter
	@Setter
	private Date dataDe;

	@Getter
	@Setter
	private Date dataAte;

	@Getter
	@Setter
	private boolean delivery;

	@Getter
	@Setter
	private BigDecimal valorDe;

	@Getter
	@Setter
	private BigDecimal valorAte;

	public Status[] statusList() {
		return Status.values();
	}

}
