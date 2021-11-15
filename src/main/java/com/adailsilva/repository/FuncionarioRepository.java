package com.adailsilva.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.adailsilva.filter.FuncionarioFilter;
import com.adailsilva.model.Funcionario;

@Repository(forEntity = Funcionario.class)
public abstract class FuncionarioRepository extends AbstractEntityRepository<Funcionario, Integer> {

	public Funcionario porUsuario(String email) {
		return (Funcionario) this.entityManager()
				.createNativeQuery("select * from funcionario where usuario = :email", Funcionario.class)
				.setParameter("email", email).getSingleResult();
	}

	public abstract Funcionario findByCpf(String cpf);

	@SuppressWarnings("unchecked")
	public List<Funcionario> filtrados(FuncionarioFilter filtro) {
		Session session = this.entityManager().unwrap(Session.class);

		Criteria criteria = session.createCriteria(Funcionario.class).createAlias("acesso", "ac");

		if (StringUtils.isNotBlank(filtro.getNome())) {

			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));

		}
		if (StringUtils.isNotBlank(filtro.getSobrenome())) {

			criteria.add(Restrictions.ilike("sobrenome", filtro.getSobrenome(), MatchMode.ANYWHERE));

		}

		if (StringUtils.isNotBlank(filtro.getCpf())) {

			criteria.add(Restrictions.ilike("cpf", filtro.getCpf(), MatchMode.ANYWHERE));

		}

		if (StringUtils.isNotBlank(filtro.getTelefone())) {

			criteria.add(Restrictions.ilike("telefone", filtro.getTelefone(), MatchMode.ANYWHERE));

		}

		if (filtro.getNascimento() != null) {

			criteria.add(Restrictions.eq("aniversario", filtro.getNascimento()));

		}
		if (StringUtils.isNotBlank(filtro.getUsuario())) {

			criteria.add(Restrictions.ilike("usuario", filtro.getUsuario(), MatchMode.ANYWHERE));

		}

		if (StringUtils.isNotBlank(filtro.getAcesso())) {

			criteria.add(Restrictions.eq("ac.descricao", filtro.getAcesso()));

		}

		return criteria.list();
	}
}
