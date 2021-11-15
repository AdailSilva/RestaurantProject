package com.projeto.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Acesso;

@Repository(forEntity = Acesso.class)
public abstract class AcessoRepository extends AbstractEntityRepository<Acesso, Integer> {

	public abstract Acesso findByDescricao(String descricao);
	


}
