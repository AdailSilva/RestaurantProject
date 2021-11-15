package com.projeto.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.projeto.model.Mesa;
import com.projeto.model.Mesa.StatusReserva;
import com.projeto.repository.MesaRepository;
import com.projeto.repository.ReservaRepository;
import com.projeto.model.Reserva;

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
