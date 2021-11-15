package com.adailsilva.repository;

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

import com.adailsilva.filter.ProdutoFilter;
import com.adailsilva.model.Categoria;
import com.adailsilva.model.Produto;
import com.adailsilva.model.adapter.ProdutoAdapter;

@Repository(forEntity = Produto.class)
@Transactional
public abstract class ProdutoRepository extends AbstractEntityRepository<Produto, Integer> {

	public abstract Produto findByDescricao(String descricao);

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {

		Session session = this.entityManager().unwrap(Session.class);

		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getDescricao())) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));

		}

		if (filtro.getCategoria() != null) {
			criteria.add(Restrictions.eqOrIsNull("categoria", filtro.getCategoria()));
		}

		if (filtro.getPrecoInicial() != null) {
			criteria.add(Restrictions.ge("preco", filtro.getPrecoInicial()));
		}

		if (filtro.getPrecoFinal() != null) {
			criteria.add(Restrictions.le("preco", filtro.getPrecoFinal()));
		}
		if (filtro.getCustoInicial() != null) {
			criteria.add(Restrictions.ge("custo", filtro.getCustoInicial()));
		}

		if (filtro.getCustoFinal() != null) {
			criteria.add(Restrictions.le("custo", filtro.getCustoFinal()));
		}

		return criteria.list();
	}

	public List<Produto> porCategoria(Categoria categoria) {
		return entityManager().createQuery("select p from Produto p where p.categoria=:categoria", Produto.class)
				.setParameter("categoria", categoria).getResultList();
	}

	public List<ProdutoAdapter> findByMostSellers() {
		return this.entityManager()
				.createQuery(
						"select new com.adailsilva.model.adapter.ProdutoAdapter(prod , SUM(ip.quantidade)) from ItemPedido ip "
								+ "join ip.produto prod " + "where ip.statusIp = 'FINALIZADO' group by prod ",
						ProdutoAdapter.class)
				.getResultList();
	}

	public List<ProdutoAdapter> findBetweenDates(Date dtInicial, Date dtFinal) {
		return this.entityManager()
				.createQuery(
						"select new com.adailsilva.model.adapter.ProdutoAdapter(prod , SUM(ip.quantidade)) from ItemPedido ip "
								+ "join ip.produto prod " + "where ip.statusIp = 'FINALIZADO' "
								+ " and ip.data BETWEEN :data1 AND :data2  " + "group by prod ",
						ProdutoAdapter.class)
				.setParameter("data1", dtInicial).setParameter("data2", dtFinal).getResultList();
	}

	public List<ProdutoAdapter> findGroupDateDay(Date dtInicial, Date dtFinal, Produto produto) {
		return this.entityManager()
				.createQuery(
						"select new com.adailsilva.model.adapter.ProdutoAdapter(prod , SUM(ip.quantidade),date(ip.data)) "
								+ "from ItemPedido ip " + "join ip.produto prod " + "where ip.statusIp = 'FINALIZADO' "
								+ " and ip.data BETWEEN :data1 AND :data2 " + "and ip.produto =  :produtoParam "
								+ "group by prod,date(ip.data) ",
						ProdutoAdapter.class)
				.setParameter("produtoParam", produto).setParameter("data1", dtInicial).setParameter("data2", dtFinal)
				.getResultList();

	}

	// public List<ProdutoAdapter> findGroupDateYear(Date dtInicial, Date
	// dtFinal, Produto produto) {
	// return this.entityManager()
	// .createQuery(
	// "select new com.adailsilva.model.adapter.ProdutoAdapter(prod ,
	// SUM(ip.quantidade),date(ip.data),MONTH(ip.data)) "
	// + "from ItemPedido ip " + "join ip.produto prod " + "where ip.statusIp =
	// 'FINALIZADO' "
	// + " and ip.data BETWEEN :data1 AND :data2 " + "and ip.produto =
	// :produtoParam "
	// + "group by prod,MONTH(ip.data) ",
	// ProdutoAdapter.class)
	// .setParameter("produtoParam", produto).setParameter("data1",
	// dtInicial).setParameter("data2", dtFinal)
	// .getResultList();
	//
	// }

	public List<ProdutoAdapter> findGroupDateYear(Date dtInicial, Date dtFinal, Produto produto) {
		return this.entityManager()
				.createQuery(
						"select new com.adailsilva.model.adapter.ProdutoAdapter(prod , SUM(ip.quantidade),MONTH(ip.data),year(ip.data)) "
								+ "from ItemPedido ip " + "join ip.produto prod " + "where ip.statusIp = 'FINALIZADO' "
								+ " and date(ip.data) BETWEEN date(:data1) AND date(:data2) "
								+ "and ip.produto =  :produtoParam " + "group by prod,MONTH(ip.data),year(ip.data) ",
						ProdutoAdapter.class)
				.setParameter("produtoParam", produto).setParameter("data1", dtInicial).setParameter("data2", dtFinal)
				.getResultList();
	}

}
