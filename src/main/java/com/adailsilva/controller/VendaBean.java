package com.adailsilva.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.adailsilva.model.Categoria;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.Funcionario;
import com.adailsilva.model.ItemPedido;
import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pedido;
import com.adailsilva.model.Produto;
import com.adailsilva.model.ItemPedido.StatusIP;
import com.adailsilva.model.Mesa.StatusRelacionamento;
import com.adailsilva.model.Mesa.StatusReserva;
import com.adailsilva.model.Pedido.Status;
import com.adailsilva.repository.CategoriaRepository;
import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.repository.ProdutoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.MesaService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class VendaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Getter
	@UsuarioLogado
	private UsuarioSistema usuario;

	@Getter
	@Setter
	private Boolean categoriasB = false;

	@Getter
	@Setter
	private Boolean panelProdutos = false;

	@Getter
	@Setter
	private Boolean panelCategorias = true;

	@Inject
	private CategoriaRepository categoriaRepo;

	@Getter
	@Setter
	private Categoria categoriaSelecionada;

	@Inject
	private ProdutoRepository produtoRepo;

	@Getter
	@Setter
	List<Categoria> categorias;

	@Getter
	@Setter
	List<Produto> produtosPorCategoria;

	@Getter
	@Setter
	private Produto produtoSelecionado;

	@Inject
	private MesaRepository mesaRepo;

	@Getter
	@Setter
	private List<Mesa> mesas = new ArrayList<>();

	@Getter
	@Setter
	private Mesa mesaSelecinada;

	@Getter
	@Setter
	private ItemPedido ip = new ItemPedido();

	@Getter
	@Setter
	private Pedido pedido = new Pedido();

	@Inject
	private EntityManager manager;

	@Inject
	private PedidoRepository pedidoRepo;

	@Inject
	private ItemPedidoRepository itemPedidoRepo;

	@Getter
	@Setter
	private ItemPedido ipSelecionado = new ItemPedido();

	@Getter
	@Setter
	private List<ItemPedido> itens = new ArrayList<>();

	@Getter
	@Setter
	private List<Produto> produtosFiltrados = new ArrayList<>();

	@Getter
	@Setter
	private String filtroProd;

	@Inject
	private MesaService mesaService;

	public void preRender(ComponentSystemEvent e) {
		if (FacesUtil.isNotPostback()) {
			mesaService.processarMesas();
			mesas = mesaRepo.mesasEmUso();
			mesas.removeIf(m -> m.getStatus().equals(StatusReserva.RESERVADA));

		}

	}

	/*
	 * Tipos produtos
	 */

	public void buscarRefeicoes() {
		if (mesaSelecinada == null) {
			FacesUtil.addErrorMessage("Você precisa selecionar uma mesa");
		} else {
			categorias = categoriaRepo.buscarPorTipo("Refeicao");
			if (!categorias.isEmpty()) {
				categoriasB = true;
			}
		}
	}

	public void buscarSobremesas() {
		if (mesaSelecinada == null) {
			FacesUtil.addErrorMessage("Você precisa selecionar uma mesa");
		} else {
			categorias = categoriaRepo.buscarPorTipo("Sobremesa");
			if (!categorias.isEmpty()) {
				categoriasB = true;
			}
		}
	}

	public void buscarBebidas() {

		if (mesaSelecinada == null) {
			FacesUtil.addErrorMessage("Você precisa selecionar uma mesa");
		} else {
			categorias = categoriaRepo.buscarPorTipo("Bebida");
			if (!categorias.isEmpty()) {
				categoriasB = true;
			}
		}

	}

	public void filtrarProdutos() {
		produtosFiltrados.clear();
		produtosPorCategoria = produtoRepo.porCategoria(categoriaSelecionada);
		produtosFiltrados.addAll(produtosPorCategoria);
		panelCategorias = false;
		panelProdutos = true;

	}

	/*
	 * 
	 * Navegação
	 */

	public void retornarCategorias() {
		panelProdutos = false;
		panelCategorias = true;
	}

	/*
	 * 
	 * Produtos
	 */

	public void adicionarQTD() {
		produtoSelecionado.setQtd(produtoSelecionado.getQtd() + 1);

	}

	public void diminuirQTDDialog() {

		if (produtoSelecionado.getQtd() > 0) {
			produtoSelecionado.setQtd(produtoSelecionado.getQtd() - 1);
		}

		if (produtoSelecionado.getQtd() == 0) {
			removerProduto();
		}

	}

	public void diminuirQTD() {

		if (produtoSelecionado.getQtd() > 0) {
			produtoSelecionado.setQtd(produtoSelecionado.getQtd() - 1);
		}

	}

	public void removerProduto() {

		itens.forEach(p -> {
			if (p.getProduto().getDescricao().equals(produtoSelecionado.getDescricao())) {
				p.setId(1111);
				ipSelecionado = p;

			}
		});
		System.out.println(ipSelecionado.getId());
		if (ipSelecionado.getId() != null) {
			System.out.println(itens.remove(ipSelecionado));
		}

		ipSelecionado = new ItemPedido();

	}

	/*
	 * 
	 *
	 * Emissão pedido
	 */

	@Transactional
	public String emitirPedido() {
		if (!itens.isEmpty()) {

			if (mesaSelecinada.getStatus().equals(StatusReserva.LIVRE)) {
				criarPedidoIp();
				adicionarIpPedido();

			} else if (mesaSelecinada.getStatus().equals(StatusReserva.OCUPADA)) {
				adicionarIpPedido();

			}
			
			

			return "venda.xhtml ?faces-redirect=true";
		} else {

			FacesUtil.addErrorMessage("Não existe item para ser emitido!");
			return "";
		}

	}

	/*
	 * 
	 * Gravação BD
	 */
	public void criarPedidoIp() {
		Cliente c = manager.find(Cliente.class, new Integer(1));
		pedido.setCliente(c);
		pedido.setData(new Date());
		pedido.setFuncionario(usuario.getUsuario());
		pedido.setMesa(mesaSelecinada);
		pedido.setStatus(Status.ANDAMENTO);
		pedido.setValorTotal(new BigDecimal((0)));
		pedido.setDelivery(false);
		pedido = pedidoRepo.save(pedido);
		mesaSelecinada.setStatus(StatusReserva.OCUPADA);
		mesaSelecinada = mesaRepo.save(mesaSelecinada);

	}

	public void adicionarIpPedido() {
		BigDecimal value = BigDecimal.ZERO;
		pedido = pedidoRepo.pedidoPorMesa(mesaSelecinada);
		for (ItemPedido ipList : itens) {
			ipList.setPedido(pedido);
			ipList.setQuantidade(ipList.getProduto().getQtd());
			itemPedidoRepo.save(ipList);
			value = value.add(ipList.getValorTotal());
		}
		System.out.println("value " + value);
		pedido.setValorTotal(value);
		pedido = pedidoRepo.save(pedido);

	}

	/*
	 * 
	 * Adição dos itens em memória
	 */

	public void adicionarItemPedido() {
		boolean aux = false;
		if (produtoSelecionado.getQtd() != 0) {
			for (ItemPedido ipp : itens) {
				if (ipp.getProduto().getDescricao().equals(produtoSelecionado.getDescricao())) {
					ipp.setQuantidade(produtoSelecionado.getQtd());
					aux = true;
				}
			}

			if (aux == false) {
				ItemPedido ip = new ItemPedido();
				ip.setProduto(produtoSelecionado);
				ip.setQuantidade(produtoSelecionado.getQtd());
				ip.setSequencia(1);
				ip.setStatusIp(StatusIP.ANDAMENTO);
				ip.setData(new Date());
				ip.setValorUnitario(produtoSelecionado.getPreco());

				if (!usuario.getUsuario().temAcesso("CLIENTE")) {
					ip.setFuncionario((Funcionario) usuario.getUsuario());
				}

				itens.add(ip);

			}

			FacesUtil.addInfoMessage("Produto adicionado com sucesso!");
		} else {
			FacesUtil.addErrorMessage("Quantidade igual a zero.");
		}

	}

	/*
	 * 
	 * validar que a lista não está vazia
	 */
	public boolean isCondicaoEmissaoPedido() {
		return !itens.isEmpty();
	}

	/*
	 * 
	 * Ajax
	 */

	public void ajaxList() {
		produtosFiltrados.clear();
		for (Produto prod : produtosPorCategoria) {
			if (prod.getDescricao().toLowerCase().contains(filtroProd.toLowerCase())) {
				produtosFiltrados.add(prod);
			}

		}

		produtosFiltrados.forEach(p -> {
			System.out.println(p.getDescricao());
		});

	}

}