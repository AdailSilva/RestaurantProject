package com.projeto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Cliente;
import com.projeto.model.Pedido;
import com.projeto.repository.PedidoRepository;
import com.projeto.security.UsuarioLogado;
import com.projeto.security.UsuarioSistema;

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
