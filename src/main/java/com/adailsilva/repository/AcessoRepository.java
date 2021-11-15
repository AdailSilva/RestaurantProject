package com.adailsilva.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.Acesso;

@Repository(forEntity = Acesso.class)
public abstract class AcessoRepository extends AbstractEntityRepository<Acesso, Integer> {

	public abstract Acesso findByDescricao(String descricao);
	


}
