package com.adailsilva.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.adailsilva.filter.CategoriaFilter;
import com.adailsilva.model.Categoria;

@Repository(forEntity = Categoria.class)
public abstract class CategoriaRepository extends AbstractEntityRepository<Categoria, Integer> {
	
	public abstract Categoria findByDescricao(String descricao);

	public List<Categoria> buscarPorTipo(String s) {
		return this.entityManager().createQuery("select h from Categoria h where h.tipo = :tipo", Categoria.class)
				.setParameter("tipo", s).getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Categoria> filtrados(CategoriaFilter filtro) {

		Session session = this.entityManager().unwrap(Session.class);

		Criteria criteria = session.createCriteria(Categoria.class);

		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));

		}

		if (StringUtils.isNotBlank(filtro.getTipo())) {
			criteria.add(Restrictions.eqOrIsNull("tipo", filtro.getTipo()));
		}

		return criteria.list();
	}

}
