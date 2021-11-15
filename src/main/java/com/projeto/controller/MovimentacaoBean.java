package com.projeto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.model.Mesa;
import com.projeto.model.Pedido;
import com.projeto.model.Reserva;
import com.projeto.repository.MesaRepository;
import com.projeto.repository.PedidoRepository;
import com.projeto.repository.ReservaRepository;
import com.projeto.service.MesaService;
import com.projeto.util.jsf.FacesUtil;

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
