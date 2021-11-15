package com.adailsilva.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.GrupoMesas;
import com.adailsilva.model.Mesa;
import com.adailsilva.model.Mesa.StatusReserva;

@Repository(forEntity = Mesa.class)
public abstract class MesaRepository extends AbstractEntityRepository<Mesa, Integer> {

	public List<Mesa> mesasEmUso() {

		return entityManager().createQuery(
				"select m from Mesa m where (m.statusRelacionamento = 'RELACIONADAPAI' or m.statusRelacionamento is null)  AND  (m.status = 'LIVRE' OR m.status = 'OCUPADA' )",
				Mesa.class).getResultList();
	}

	public List<Mesa> mesasOcupadas() {
		return entityManager().createQuery(
				"select m from Mesa m where m.status='OCUPADA' and (m.statusRelacionamento is null or m.statusRelacionamento != 'RELACIONADAFILHA') ",
				Mesa.class).getResultList();
	}

	public List<Mesa> disponiveisAgrupamento() {
		return entityManager().createQuery(
				" select m from Mesa m where m.statusRelacionamento is null AND  (m.status = 'LIVRE' OR m.status = 'OCUPADA' ) ",
				Mesa.class).getResultList();
	}

	public List<Mesa> mesaPorGrupo(GrupoMesas gm) {
		return entityManager().createQuery("select m from Mesa m where m.relacionamento=:grupo", Mesa.class)
				.setParameter("grupo", gm).getResultList();
	}

	public abstract Mesa findByNumeroMesa(String numeroMesa);

	public abstract List<Mesa> findByStatus(StatusReserva status);

}
