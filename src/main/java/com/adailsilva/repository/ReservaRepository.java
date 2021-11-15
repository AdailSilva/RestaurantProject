package com.adailsilva.repository;

import java.util.Date;
import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.Mesa;
import com.adailsilva.model.Pessoa;
import com.adailsilva.model.Reserva;

@Repository(forEntity = Reserva.class)
public abstract class ReservaRepository extends AbstractEntityRepository<Reserva, Long> {

	public Reserva findByDateAndMesa(Mesa m, Date d) {
		return this.entityManager()
				.createQuery("from Reserva r where r.mesa = :mesa and date(r.data) = :data", Reserva.class)
				.setParameter("mesa", m).setParameter("data", d).getSingleResult();
	}

	public List<Reserva> findByMesaStatus(Mesa p, boolean status) {
		return this.entityManager()
				.createQuery("from Reserva r where r.ativo = :arg1 and r.mesa = :arg2", Reserva.class)
				.setParameter("arg1", status).setParameter("arg2", p).getResultList();
	}

	public List<Reserva> findByMesaStatusDate(Mesa p, boolean status, Date d) {
		return this.entityManager()
				.createQuery("from Reserva r where r.ativo = :arg1 and r.mesa = :arg2 and date(r.data) = date(:arg3)",
						Reserva.class)
				.setParameter("arg1", status).setParameter("arg2", p).setParameter("arg3", d).getResultList();
	}

	public abstract List<Reserva> findByPessoa(Pessoa p);

}
