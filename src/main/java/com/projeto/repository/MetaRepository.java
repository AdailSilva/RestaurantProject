package com.projeto.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Meta;
import com.projeto.model.Meta.StatusMeta;

@Repository(forEntity = Meta.class)
public abstract class MetaRepository extends AbstractEntityRepository<Meta, Integer> {

	public List<Meta> findbyStatus(StatusMeta status) {
		return this.entityManager().createQuery("from Meta m where m.status = :param", Meta.class)
				.setParameter("param", status).getResultList();
	}

}
