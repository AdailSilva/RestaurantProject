package com.adailsilva.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.filter.ClienteFilter;
import com.adailsilva.model.Categoria;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.Endereco;
import com.adailsilva.model.ItemPedido;
import com.adailsilva.model.Pedido;
import com.adailsilva.model.Pessoa;
import com.adailsilva.model.Produto;
import com.adailsilva.model.ItemPedido.StatusIP;
import com.adailsilva.model.Pedido.Status;
import com.adailsilva.repository.CategoriaRepository;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.repository.PessoaRepository;
import com.adailsilva.repository.ProdutoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class DeliveryBean implements Serializable {

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
	private Pessoa pessoa;

	@Inject
	private PessoaRepository pessoaRepo;

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
	private List<Categoria> categorias;

	@Getter
	@Setter
	private List<Produto> produtosPorCategoria;

	@Getter
	@Setter
	private Produto produtoSelecionado;

	@Getter
	@Setter
	private ItemPedido ip = new ItemPedido();

	@Getter
	@Setter
	private Pedido pedido = new Pedido();

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

	@Getter
	@Setter
	private Endereco enderecoSelecionado;

	@Getter
	@Setter
	private Pessoa clienteSelecionado;

	@Getter
	@Setter
	private ClienteFilter clienteFilter = new ClienteFilter();

	@Inject
	private ClienteRepository clienteRepo;

	@Getter
	@Setter
	private List<Cliente> clientes;

	@PostConstruct
	public void posRender() {

		pessoa = pessoaRepo.findBy(usuario.getUsuario().getId());

	}

	/*
	 * Tipos produtos
	 */

	public void buscarRefeicoes() {

		categorias = categoriaRepo.buscarPorTipo("Refeicao");
		if (!categorias.isEmpty()) {
			categoriasB = true;
		}

	}

	public void buscarSobremesas() {

		categorias = categoriaRepo.buscarPorTipo("Sobremesa");
		if (!categorias.isEmpty()) {
			categoriasB = true;
		}

	}

	public void buscarBebidas() {

		categorias = categoriaRepo.buscarPorTipo("Bebida");
		if (!categorias.isEmpty()) {
			categoriasB = true;
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

	public String emitirPedido() {
		if (!itens.isEmpty()) {

			criarPedidoIp();
			adicionarIpPedido();

			if (usuario.getUsuario().temAcesso("CLIENTE")) {
				return "dashboard.xhtml ?faces-redirect=true";
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

		if (clienteSelecionado != null) {
			pedido.setCliente(clienteSelecionado);
		} else {
			pedido.setCliente(usuario.getUsuario());
		}
		pedido.setData(new Date());
		pedido.setFuncionario(usuario.getUsuario());
		pedido.setMesa(null);
		pedido.setStatus(Status.ANDAMENTO);
		pedido.setValorTotal(new BigDecimal((0)));
		pedido.setDelivery(true);
		pedido.setEnderecoEntrega(enderecoSelecionado);
		pedido = pedidoRepo.save(pedido);

	}

	public void adicionarIpPedido() {

		for (ItemPedido ipList : itens) {
			ipList.setPedido(pedido);
			ipList.setQuantidade(ipList.getProduto().getQtd());
			itemPedidoRepo.save(ipList);
		}

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

	public void buscarClientes() {
		clientes = clienteRepo.filtrados(clienteFilter);
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

	public void carregarEnderecos() {
		clienteSelecionado = clienteRepo.findBy(clienteSelecionado.getId());
		System.out.println(clienteSelecionado.getEnderecos().size());
	}

}