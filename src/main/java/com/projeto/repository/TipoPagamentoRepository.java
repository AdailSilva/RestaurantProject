package com.projeto.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.TipoPagamento;

@Repository(forEntity = TipoPagamento.class)
public abstract class TipoPagamentoRepository extends AbstractEntityRepository<TipoPagamento, Integer> {
	
}
