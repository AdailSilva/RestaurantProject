package com.projeto.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.GrupoMesas;

@Repository(forEntity = GrupoMesas.class)
public abstract class GrupoMesaRepository extends AbstractEntityRepository<GrupoMesas, Integer> {

	public List<GrupoMesas> gruposAtivos() {
		return entityManager().createQuery(
				"select gm from GrupoMesas gm where (gm.mesaSelecionada.statusRelacionamento = 'RELACIONADAPAI' or gm.mesaSelecionada.statusRelacionamento = 'RELACIONADAFILHA') and gm.dataFim is null ",
				GrupoMesas.class).getResultList();
	}

}
