package com.projeto.repository;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.projeto.model.Pessoa;

@Repository(forEntity = Pessoa.class)
public abstract class PessoaRepository extends AbstractEntityRepository<Pessoa, Integer> {

	public abstract Pessoa findByUsuario(String usuario);
	
	public abstract Pessoa findByCpf(String cpf);
	

}
