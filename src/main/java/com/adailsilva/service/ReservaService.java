package com.adailsilva.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.adailsilva.model.Reserva;
import com.adailsilva.repository.ReservaRepository;
import com.adailsilva.util.jsf.FacesUtil;

public class ReservaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ReservaRepository reservaRepo;

	public boolean savar(Reserva r) {
		try {
			reservaRepo.findByDateAndMesa(r.getMesa(), r.getData());
			FacesUtil.addErrorMessage("Já existe uma reserva para esta mesa e data");
			return false;
		} catch (Exception e) {

		}

		reservaRepo.save(r);
		FacesUtil.addInfoMessage("Reserva registrada com sucesso !");

		return true;

	}
}
