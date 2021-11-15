package com.projeto.repository;

import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Endereco;
import com.projeto.model.Pessoa;

@Repository(forEntity = Endereco.class)
public abstract class EnderecoRepository extends AbstractEntityRepository<Endereco, Integer> {

	public List<Endereco> enderecosPorPessoa(Pessoa c) {
		return entityManager().createQuery("select e from Endereco e where e.pessoa =:c", Endereco.class)
				.setParameter("c", c).getResultList();
	}

}
