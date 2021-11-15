package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.model.Cliente;
import com.adailsilva.model.Pedido;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PedidosClienteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepo;

	@Getter
	@Setter
	private List<Pedido> pedidos = new ArrayList<>();

	@UsuarioLogado
	@Inject
	private UsuarioSistema user;

	@PostConstruct
	public void iniciar() {
		pedidos = pedidoRepo.findByCliente((Cliente) user.getUsuario());
		System.out.println("tamaho : " + pedidos.size());
	}

}
