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

import com.adailsilva.filter.ClienteFilter;
import com.adailsilva.model.Cartao;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.ItemPedido;
import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pagamento;
import com.adailsilva.model.Pedido;
import com.adailsilva.model.TipoPagamento;
import com.adailsilva.model.ItemPedido.StatusIP;
import com.adailsilva.model.Mesa.StatusReserva;
import com.adailsilva.model.Pedido.Status;
import com.adailsilva.repository.CartaoRepository;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.repository.PagamentoRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.repository.TipoPagamentoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.MailService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class PedidoBean implements Serializable {

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
	private Pedido pedido;

	@Inject
	private ClienteRepository clienteRepo;

	@Getter
	@Setter
	private ClienteFilter clienteFilter = new ClienteFilter();

	@Getter
	@Setter
	private List<Mesa> mesas = new ArrayList<>();

	@Inject
	private MesaRepository mesaRepo;

	@Inject
	private PedidoRepository pedidoRepo;

	@Getter
	@Setter
	private Cliente clienteSeleciondo;

	@Getter
	@Setter
	private Mesa mesaSelecionada;

	@Getter
	@Setter
	private Byte step = 0;

	@Getter
	@Setter
	private Pagamento pagamento = new Pagamento();

	@Getter
	@Setter
	private TipoPagamento formaPagamento = new TipoPagamento();

	@Getter
	@Setter
	private TipoPagamento formaPagamentoSelecionado = new TipoPagamento();

	@Getter
	@Setter
	private List<Cartao> cartoes = new ArrayList<>();

	@Getter
	@Setter
	private Cartao cartaoSelecionado = new Cartao();

	@Inject
	private CartaoRepository cartaoRepo;

	@Getter
	@Setter
	private Byte tipoPagamento = 0;

	@Getter
	@Setter
	private String retornoCartao;

	@Getter
	@Setter
	private Boolean faltaDinheiro = false;

	@Inject
	private PagamentoRepository pagamentoRepo;

	@Inject
	private ItemPedidoRepository ipRepository;

	@Inject
	private TipoPagamentoRepository tipoPagamentoRepo;

	@Getter
	@Setter
	private BigDecimal valorTotalPago = BigDecimal.ZERO;

	private List<TipoPagamento> formasPagamentoBD;

	@Getter
	@Setter
	private List<Cliente> clientes = new ArrayList<>();

	@Getter
	@Setter
	private List<Pedido> pedidoDelivery = new ArrayList<>();

	@Getter
	@Setter
	private Pedido pedidoSelecionado;

	private MailService mailService = new MailService();

	public void preRender(ComponentSystemEvent e) {
		if (FacesUtil.isNotPostback()) {
			System.out.println("Fui chaamdo");
			mesas.clear();
			cartoes.clear();
			mesas = mesaRepo.mesasOcupadas();
			cartoes = cartaoRepo.findAll();
			pedidoDelivery = pedidoRepo.deliveryPagamentoPendente();

		}

	}

	public void selecionarMesa() {
		if (mesaSelecionada == null && pedidoSelecionado == null) {
			FacesUtil.addErrorMessage("Voc?? precisa selecionar uma mesa ou um cliente ! ");
		} else {

			pedido = pedidoRepo.pedidoPorMesa(mesaSelecionada);
			step = 1;
			pedido.calcularSubtotal();
		}
	}

	public void selecionarPedidoDelivery() {
		if (mesaSelecionada == null && pedidoSelecionado == null) {
			FacesUtil.addErrorMessage("Voc?? precisa selecionar uma mesa ou um cliente ! ");
		} else {

			pedido = pedidoRepo.findBy(pedidoSelecionado.getId());
			pedido.getProdutos().forEach(e -> {
				System.out.println(e.getProduto().getDescricao());
			});
			step = 1;
			pedido.calcularSubtotal();
		}
	}

	/*
	 * 
	 * Navega????o
	 * 
	 * 
	 */

	public void pedidoToPagamento() {
		step = 2;
	}

	public void pagamentoToPedido() {
		pedidoSelecionado = null;
		mesaSelecionada = null;
		step = 1;
	}

	public void pedidoToMesa() {
		step = 0;
		tipoPagamento = 0;

	}

	public void pagamentoToConclusao() {
		processarPagamentoParcial();
		if (!this.pagamento.getPagamento().isEmpty()) {
			processarValorPago();
			if (valorTotalPago.compareTo(pedido.getValorTotal()) == -1) {
				faltaDinheiro = true;
				step = 2;
			} else {
				faltaDinheiro = false;
				step = 3;
			}
		} else {
			FacesUtil.addInfoMessage("Voc?? precisa escolher uma forma de pagamento!");
		}

		if (valorTotalPago.compareTo(pedido.getValorTotal()) == 0
				|| valorTotalPago.compareTo(pedido.getValorTotal()) == 1) {
			pagamento.getPagamento().forEach(p -> {
				System.out.println(p);
				// p = tipoPagamentoDAO.guardar(p);
			});
			atualizacaoPedido();
			gravarPagamento();
			gravarFormasPagamento();
			atualizarIP(pedido);

		}

	}

	/*
	 * 
	 * Valores monet??rios
	 * 
	 * 
	 */

	public void processarValorPago() {
		valorTotalPago = BigDecimal.ZERO;
		pagamento.getPagamento().forEach(p -> {
			valorTotalPago = valorTotalPago.add(p.getValor());

		});
	}

	public void processarPagamentoParcial() {
		if (tipoPagamento == 1) {
			formaPagamento.setPagamento(pagamento);
			formaPagamento.setCartao(null);
			validarDinheiroIngressado();

		} else if (tipoPagamento == 2) {
			cartaoSelecionado = cartaoRepo.findBy((Integer.parseInt(retornoCartao)));
			formaPagamento.setPagamento(pagamento);
			formaPagamento.setCartao(cartaoSelecionado);
			pagamento.getPagamento().add(formaPagamento);

		}

		formaPagamento = new TipoPagamento();

	}

	public boolean validarDinheiroIngressado() {
		for (TipoPagamento p : pagamento.getPagamento()) {
			if (p.getCartao() == null) {
				p.setValor(p.getValor().add(formaPagamento.getValor()));
				return true;
			}
		}
		pagamento.getPagamento().add(formaPagamento);
		return false;
	}

	public BigDecimal getDiferencaValor() {
		return pedido.getValorTotal().subtract(valorTotalPago);
	}

	public void removerFormaPagamento() {
		System.out.println(valorTotalPago);
		valorTotalPago = valorTotalPago.subtract(formaPagamentoSelecionado.getValor());
		this.valorTotalPago.add(formaPagamentoSelecionado.getValor());
		this.pagamento.getPagamento().remove(formaPagamentoSelecionado);
		System.out.println(getDiferencaValor());
		System.out.println(valorTotalPago);
	}

	public BigDecimal getTroco() {
		return valorTotalPago.subtract(pedido.getValorTotal());
	}

	public void buscarClientes() {
		clientes = clienteRepo.filtrados(clienteFilter);
	}

	public void selecionarCliente() {
		pedido.setCliente(clienteSeleciondo);
	}

	// Grava????o no banco de dados

	public void atualizacaoPedido() {
		pedido.setStatus(Status.PAGO);
		pedido.setData(new Date());
		pedido.setFuncionario(usuario.getUsuario());
		pedido = pedidoRepo.save(pedido);
		sendMail();
		if (mesaSelecionada != null) {
			mesaSelecionada.setStatus(StatusReserva.LIVRE);
			mesaSelecionada = mesaRepo.save(mesaSelecionada);
		}

	}

	public void gravarPagamento() {
		pagamento.setPedido(pedido);
		pagamento.setValor(pedido.getValorTotal());
		formasPagamentoBD = pagamento.getPagamento();
		pagamento = pagamentoRepo.save(pagamento);
	}

	public void gravarFormasPagamento() {
		System.out.println("=====formas pagamentos ======");
		formasPagamentoBD.forEach(p -> {
			p.setPagamento(pagamento);
			// System.out.println(p);
			p = tipoPagamentoRepo.save(p);
		});
		System.out.println("=====formas pagamentos ======");
	}

	public void atualizarIP(Pedido pedido) {
		for (ItemPedido ip : ipRepository.itensPorPedido(pedido)) {
			ip.setStatusIp(StatusIP.FINALIZADO);
			ipRepository.save(ip);
		}
	}

	public void dormir() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) {
			System.out.println("Puxa, estava dormindo! Voc?? me acordou");
		}
	}

	public void sendMail() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mailService.sendMail(pedido);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
