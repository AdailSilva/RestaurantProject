package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import com.adailsilva.model.GrupoMesas;
import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pedido;
import com.adailsilva.model.Mesa.StatusRelacionamento;
import com.adailsilva.model.Mesa.StatusReserva;
import com.adailsilva.model.Pedido.Status;
import com.adailsilva.repository.GrupoMesaRepository;
import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.service.MesaService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class MesasBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MesaRepository mesaRepo;

	@Getter
	@Setter
	private List<Mesa> mesasDisponiveis;

	@Getter
	@Setter
	private List<Mesa> mesas = new ArrayList<>();

	private GrupoMesas grupoMesa = new GrupoMesas();

	@Getter
	@Setter
	private Mesa mesaSelecionada;

	@Inject
	private GrupoMesaRepository grupoMesaRepo;

	@Inject
	private ItemPedidoRepository ipRepo;

	@Getter
	@Setter
	private List<GrupoMesas> gruposAtivos;

	@Getter
	@Setter
	private GrupoMesas grupoSelecionado;

	@Getter
	@Setter
	private List<String> mesasSelecionadas = new ArrayList<>();

	@Inject
	private PedidoRepository pedidoRepo;

	@Getter
	private boolean pedidoAndamento;

	@Getter
	@Setter
	private Pedido pedido;

	@Getter
	@Setter
	private String dialog;

	@Inject
	private MesaService mesaService;

	@PostConstruct
	public void carregarMesas() {
		mesaService.processarMesas();
		mesasDisponiveis = mesaRepo.disponiveisAgrupamento();
		gruposAtivos = grupoMesaRepo.gruposAtivos();
		System.out.println("Tamanho " + gruposAtivos.size());
		System.out.println("Mesas disponiveis " + mesasDisponiveis.size());
	}

	public void adicionarMesas() {
		System.out.println(mesasSelecionadas.size() + " < tamanho");
		mesasSelecionadas.forEach(e -> {
			System.out.println(e);
		});
		mesaToObject();

	}

	public void mesaToObject() {
		mesas.clear();
		mesasSelecionadas.forEach(m -> {
			Mesa mesa = mesaRepo.findByNumeroMesa(m);
			System.out.println("id " + mesa.getId());
			mesas.add(mesa);
		});
	}

	@Transactional
	public void processar() {
		if (mesas.size() == 0) {
			FacesUtil.addErrorMessage("Você precisa escolher uma mesa");
		} else if (mesas.size() == 1) {
			FacesUtil.addErrorMessage("Você precisa escolher pelo menos 2 mesas");
		} else {
			inflarGrupo();
			agruparMesas();
			gruposAtivos = grupoMesaRepo.gruposAtivos();
			mesasDisponiveis = mesaRepo.disponiveisAgrupamento();
		}
	}

	public void inflarGrupo() {
		grupoMesa.setDataInicio(new Date());
		StringBuffer numeroMesas = new StringBuffer();
		for (Mesa mesa : mesas) {
			numeroMesas.append(mesa.getNumeroMesa() + " ");

		}
		grupoMesa.setNumeroMesas(numeroMesas.toString());

		mesas.forEach(m -> {
			if (mesas.get(0).equals(m)) {
				m.setRelacionamento(grupoMesa);
				m.setStatusRelacionamento(StatusRelacionamento.RELACIONADAPAI);
				grupoMesa.setMesaSelecionada(m);

			} else {
				m.setRelacionamento(grupoMesa);
				m.setStatusRelacionamento(StatusRelacionamento.RELACIONADAFILHA);
			}
		});
	}

	@Transactional
	public void desagruparMesas() {
		for (Mesa m : mesaRepo.mesaPorGrupo(grupoSelecionado)) {
			if (m.getStatusRelacionamento() == StatusRelacionamento.RELACIONADAFILHA) {
				m.setStatus(StatusReserva.LIVRE);
			}
			m.setStatusRelacionamento(null);
			m.setRelacionamento(null);

			mesaRepo.save(m);
		}
		grupoSelecionado.setDataInicio(new Date());
		grupoSelecionado.setDataFim(new Date());
		grupoMesaRepo.save(grupoSelecionado);

		gruposAtivos.remove(grupoSelecionado);
		mesasDisponiveis = mesaRepo.disponiveisAgrupamento();

	}

	@Transactional
	public void agruparMesas() {
		grupoMesa = grupoMesaRepo.save(grupoMesa);
		Mesa mainlyMesa = grupoMesa.getMesaSelecionada();
		for (Mesa mesa : mesas) {
			mesa = mesaRepo.save(mesa);
			try {
				Pedido p = pedidoRepo.pedidoPorMesa(mesa);
				if (p.getMesa().getStatusRelacionamento() == StatusRelacionamento.RELACIONADAFILHA) {
					p.setStatus(Status.CANCELADO);
					Pedido p2 = pedidoRepo.pedidoPorMesa(mainlyMesa);

					p.getProdutos().forEach(e -> {
						e.setPedido(p2);
						ipRepo.save(e);

					});

					pedidoRepo.save(p);
					pedidoRepo.save(p2);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		mesas.clear();
		grupoMesa = new GrupoMesas();
		FacesUtil.addInfoMessage("Mesas agrupadas com sucesso !");

	}

	public void removerMesa() {
		this.mesasDisponiveis.add(mesaSelecionada);
		this.mesas.remove(mesaSelecionada);
		try {
			mesasSelecionadas.remove(mesaSelecionada.getNumeroMesa());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public boolean getMesaItem() {
		if (mesas.isEmpty()) {
			return false;
		}
		return true;
	}

	public void validarDinheiroGrupo() {
		System.out.println("Validar dinheiro grupo");

		try {
			pedido = pedidoRepo.pedidoPorMesa(grupoSelecionado.getMesaSelecionada());
		} catch (NoResultException e) {
			System.out.println("tratarei");
			pedidoAndamento = false;
			dialog = "PF('DialogEndereco').show()";
		}

		if (pedido != null) {

			pedidoAndamento = true;
			dialog = "PF('DialogEndereco').show()";
		}

		System.out.println("Status : " + pedidoAndamento);

	}

}
