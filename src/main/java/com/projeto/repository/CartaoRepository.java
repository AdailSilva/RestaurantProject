package com.projeto.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Cartao;

@Repository(forEntity = Cartao.class)
public abstract class CartaoRepository extends AbstractEntityRepository<Cartao, Integer> {
	
}
