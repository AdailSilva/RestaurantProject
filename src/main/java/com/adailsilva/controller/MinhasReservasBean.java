package com.adailsilva.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.adailsilva.model.Reserva;
import com.adailsilva.repository.ReservaRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;

import lombok.Getter;

@ViewScoped
@Named
public class MinhasReservasBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private List<Reserva> reservas;

	@Inject
	private ReservaRepository reservaRepo;

	@UsuarioLogado
	@Inject
	private UsuarioSistema user;

	@PostConstruct
	public void inicializar() {
		reservas = reservaRepo.findByPessoa(user.getUsuario());

	}

}
