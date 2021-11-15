package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.ItemPedido;
import com.projeto.model.ItemPedido.StatusIP;
import com.projeto.repository.ItemPedidoRepository;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class CozinhaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Getter
	@Setter
	private List<ItemPedido> itensCozinha;

	@Getter
	@Setter
	private List<ItemPedido> itensRetirada;

	@Getter
	@Setter
	private ItemPedido ipSelecionado = new ItemPedido();

	@Inject
	private ItemPedidoRepository itemPedidoRepo;

	public void preRenderCozinha(ComponentSystemEvent e) {
		if (FacesUtil.isNotPostback()) {
			itensCozinha = itemPedidoRepo.itensCozinha();
		}

	}
	
	public void refreshCozinha(){
		itensCozinha = itemPedidoRepo.itensCozinha();
	}

	public void preRenderRetirada(ComponentSystemEvent e) {
		if (FacesUtil.isNotPostback()) {
			itensRetirada = itemPedidoRepo.itensRetirada();
		}

	}

	public void liberarPedido() {

		itensCozinha.remove(ipSelecionado);
		ipSelecionado.setStatusIp(StatusIP.RETIRADA);
		itemPedidoRepo.save(ipSelecionado);
	}

	public void entregarPedido() {

		itensRetirada.remove(ipSelecionado);
		ipSelecionado.setStatusIp(StatusIP.FINALIZADO);
		itemPedidoRepo.save(ipSelecionado);
	}

}
