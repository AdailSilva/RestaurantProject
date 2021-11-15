package com.adailsilva.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.filter.ClienteFilter;
import com.adailsilva.model.Cliente;
import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pessoa;
import com.adailsilva.model.Reserva;
import com.adailsilva.model.Mesa.StatusReserva;
import com.adailsilva.repository.ClienteRepository;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.MesaService;
import com.adailsilva.service.ReservaService;
import com.adailsilva.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class ReservaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Reserva reserva = new Reserva();

	@Getter
	@Setter
	private List<Mesa> mesas = new ArrayList<>();

	@Inject
	private MesaRepository mesaRepo;

	@Inject
	private MesaService mesaService;

	@Inject
	private ReservaService reservaService;

	@UsuarioLogado
	@Inject
	private UsuarioSistema user;

	@Getter
	@Setter
	private Pessoa pessoaSelecionada;

	@Getter
	@Setter
	private ClienteFilter filter = new ClienteFilter();

	@Getter
	@Setter
	private List<Cliente> clientes;

	@Inject
	private ClienteRepository clienteRepo;

	public void preRender() {
		mesas = mesaService.processarMesas();
		mesas = mesaRepo.findAll();
		mesas.removeIf(m -> m.getStatus().equals(StatusReserva.OCUPADA));
		

	}

	public void findCliente() {
		clientes = clienteRepo.filtrados(filter);
	}

	public void salvar() {
		if (pessoaSelecionada == null) {
			reserva.setPessoa(user.getUsuario());
			reserva.setRestaurante(user.getUsuario().getRestaurante());
		} else {
			reserva.setPessoa(pessoaSelecionada);
			reserva.setRestaurante(pessoaSelecionada.getRestaurante());
		}
		System.out.println("Número mesa :  " + reserva.getMesa().getNumeroMesa() + " data  " + reserva.getData());
		try {
			reserva.setAtivo(true);
			reservaService.savar(reserva);
		} catch (Exception e) {
			FacesUtil.addErrorMessage("Ocorreu um erro, contate o adm");
		}
	}

}
