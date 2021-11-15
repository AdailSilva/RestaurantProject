package com.projeto.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.projeto.filter.ClienteFilter;
import com.projeto.model.Cliente;
import com.projeto.model.Mesa;
import com.projeto.model.Pessoa;
import com.projeto.model.Reserva;
import com.projeto.model.Mesa.StatusReserva;
import com.projeto.repository.ClienteRepository;
import com.projeto.repository.MesaRepository;
import com.projeto.security.UsuarioLogado;
import com.projeto.security.UsuarioSistema;
import com.projeto.service.MesaService;
import com.projeto.service.ReservaService;
import com.projeto.util.jsf.FacesUtil;

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
