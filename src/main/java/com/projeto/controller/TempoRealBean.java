package com.projeto.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.ItemPedido;
import com.projeto.model.Pedido;
import com.projeto.repository.ItemPedidoRepository;
import com.projeto.repository.PedidoRepository;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class TempoRealBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepo;

	@Inject
	private ItemPedidoRepository ipRepo;

	@Getter
	@Setter
	private List<Pedido> pedidos = new ArrayList<>();

	@Getter
	@Setter
	private List<ItemPedido> itens = new ArrayList<>();

	@Getter
	@Setter
	private BigDecimal valorTotal = BigDecimal.ZERO;

	public void preRender(ComponentSystemEvent event) {
		if (FacesUtil.isNotPostback()) {
			pedidos = pedidoRepo.pedidosDiaCorrente();
			pedidos.forEach(e -> {
				System.out.println("entrei");
				System.out.println(e.getDataFormatada());
				System.out.println(e.getData());
				System.out.println("================");
			});
			itens = ipRepo.itensDiaCorrente();
			valorTotal = (BigDecimal) pedidoRepo.valorTotalDiaCorrente();
		}
	}

}
