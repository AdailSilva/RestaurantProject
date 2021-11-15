package com.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.filter.PedidoFilter;
import com.projeto.model.Cliente;
import com.projeto.model.Funcionario;
import com.projeto.model.Pedido;
import com.projeto.repository.ClienteRepository;
import com.projeto.repository.FuncionarioRepository;
import com.projeto.repository.PedidoRepository;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DetalhesPedido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Pedido pedido = new Pedido();

	@Inject
	private PedidoRepository pedidoRepo;

	@Getter
	@Setter
	private Integer id;

	@Inject
	@Getter
	@Setter
	private PedidoFilter filter;

	@Getter
	private List<Cliente> clientes;

	@Getter
	private List<Funcionario> funcionarios;

	@Inject
	private ClienteRepository clienteRepo;

	@Inject
	private FuncionarioRepository funcionarioRepo;

	@Getter
	@Setter
	private List<Pedido> pedidos;

	public void inicializar() {
		pedido = pedidoRepo.findBy(id);
	}

	public void inicializarTelaPedido() {
		clientes = clienteRepo.findAll();
		funcionarios = funcionarioRepo.findAll();
	}

	public void visualizar() {
		System.out.println(filter);
		pedidos = pedidoRepo.filtrados(filter);
		System.out.println("tamanho" + pedidos.size());

	}

}
