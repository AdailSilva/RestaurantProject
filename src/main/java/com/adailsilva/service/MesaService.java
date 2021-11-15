package com.adailsilva.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.adailsilva.model.Mesa;
import com.adailsilva.model.Reserva;
import com.adailsilva.model.Mesa.StatusReserva;
import com.adailsilva.repository.MesaRepository;
import com.adailsilva.repository.ReservaRepository;

public class MesaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MesaRepository mesaRepo;

	@Inject
	private ReservaRepository reservaRepo;

	public List<Mesa> processarMesas() {

		List<Mesa> mesas = mesaRepo.findAll();

		for (int i = 0; i < mesas.size(); i++) {
			Mesa mesa = mesas.get(i);
			List<Reserva> reservas = reservaRepo.findByMesaStatusDate(mesa, true, new Date());
			for (Reserva r : reservas) {
				System.out.println(r.getId());
				mesa.setStatus(StatusReserva.RESERVADA);
				mesa = mesaRepo.save(mesa);
			}
			if (reservas.size() == 0) {
				if (mesa.getStatus().equals(StatusReserva.RESERVADA)) {
					mesa.setStatus(StatusReserva.LIVRE);
					mesa = mesaRepo.save(mesa);
				}

			}

		}

		return mesas;
	}

}
