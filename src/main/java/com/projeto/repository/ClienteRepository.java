package com.projeto.repository;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.projeto.filter.ClienteFilter;
import com.projeto.model.Cliente;
import com.projeto.model.adapter.ClienteAdapter;

@Repository(forEntity = Cliente.class)
@Transactional
public abstract class ClienteRepository extends AbstractEntityRepository<Cliente, Integer> {

	public Cliente porCPF(String cpf) {

		return entityManager().createQuery("select c from Cliente c where c.cpf = :cpf ", Cliente.class)
				.setParameter("cpf", cpf).getSingleResult();

	}

	public Cliente porUsuario(String email) {
		return (Cliente) this.entityManager()
				.createNativeQuery("select * from cliente where usuario = :email", Cliente.class)
				.setParameter("email", email).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {
		Session session = this.entityManager().unwrap(Session.class);

		Criteria criteria = session.createCriteria(Cliente.class).createAlias("acesso", "ac");

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

			criteria.add(Restrictions.eq("nascimento", filtro.getNascimento()));

		}
		if (StringUtils.isNotBlank(filtro.getUsuario())) {

			criteria.add(Restrictions.ilike("usuario", filtro.getUsuario(), MatchMode.ANYWHERE));

		}

		criteria.add(Restrictions.eq("ac.descricao", "CLIENTE"));

		return criteria.list();
	}

	public List<ClienteAdapter> findBetweenDatesCount(Date date1, Date date2) {

		return entityManager().createQuery(
				"select new com.projeto.model.adapter.ClienteAdapter(date(c.dataCriacao),count(c.id)) from Cliente c where date(c.dataCriacao) BETWEEN date(:data1) AND date(:data2) group by date(c.dataCriacao)",
				ClienteAdapter.class).setParameter("data1", date1).setParameter("data2", date2).getResultList();

	}
}
