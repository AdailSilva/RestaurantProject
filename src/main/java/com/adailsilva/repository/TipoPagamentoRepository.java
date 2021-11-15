package com.adailsilva.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.TipoPagamento;

@Repository(forEntity = TipoPagamento.class)
public abstract class TipoPagamentoRepository extends AbstractEntityRepository<TipoPagamento, Integer> {
	
}
