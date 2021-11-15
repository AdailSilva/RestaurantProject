package com.adailsilva.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.Cartao;

@Repository(forEntity = Cartao.class)
public abstract class CartaoRepository extends AbstractEntityRepository<Cartao, Integer> {
	
}
