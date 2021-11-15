package com.adailsilva.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.Pagamento;

@Repository(forEntity = Pagamento.class)
public abstract class PagamentoRepository extends AbstractEntityRepository<Pagamento, Integer> {


}
