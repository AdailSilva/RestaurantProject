package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pedido;
import com.adailsilva.model.Reserva;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.repository.ReservaRepository;
import com.adailsilva.service.MesaService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class MovimentacaoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MesaRepository mesaRepo;

	@Getter
	@Setter
	private List<Mesa> mesas = new ArrayList<>();

	@Getter
	@Setter
	private Mesa mesaSelecionada;

	@Getter
	@Setter
	private Pedido pedido;

	@Inject
	private PedidoRepository pedidoRepo;

	@Inject
	private ReservaRepository reservaRepo;

	@Inject
	private MesaService mesaService;

	@PostConstruct
	public void preRender() {
		mesaService.processarMesas();
		mesas = mesaRepo.findAll();

	}

	public void detalhes() {
		System.out.println(mesaSelecionada.getNumeroMesa());
		pedido = pedidoRepo.pedidoPorMesa(mesaSelecionada);
		System.out.println(pedido.getId());
		System.out.println(pedido.getProdutos().size());
	}

	public void liberarMesa() {
		Reserva r = reservaRepo.findByMesaStatusDate(mesaSelecionada, true, new Date()).get(0);
		r.setAtivo(false);
		reservaRepo.save(r);
		mesaService.processarMesas();
		mesas = mesaRepo.findAll();
		FacesUtil.addInfoMessage("Mesa liberada com sucesso !");

	}

}
