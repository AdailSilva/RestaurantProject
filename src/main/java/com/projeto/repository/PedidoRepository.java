package com.projeto.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.projeto.filter.PedidoFilter;
import com.projeto.model.Cliente;
import com.projeto.model.Funcionario;
import com.projeto.model.Mesa;
import com.projeto.model.Pedido;
import com.projeto.model.adapter.ProdutoAdapter;

@Repository(forEntity = Pedido.class)
public abstract class PedidoRepository extends AbstractEntityRepository<Pedido, Integer> {

	public abstract List<Pedido> findByCliente(Cliente cliente);

	public Pedido pedidoPorMesa(Mesa mesa) {

		return entityManager()
				.createQuery("select p from Pedido p where p.mesa = :mesa and p.status = 'ANDAMENTO'", Pedido.class)
				.setParameter("mesa", mesa).getSingleResult();

	}

	@SuppressWarnings("unchecked")
	public List<Pedido> pedidosDiaCorrente() {
		return entityManager()
				.createNativeQuery("select * from Pedido p where date(p.data) = date(now() ) order by data desc",
						Pedido.class)
				.getResultList();
	}

	public Object valorTotalDiaCorrente() {

		return entityManager()
				.createNativeQuery(
						"select sum(p.valorTotal) from Pedido p   where date(p.data) = date(now()) and p.status = 'PAGO' ")
				.getSingleResult();
	}

	public List<Pedido> deliveryPagamentoPendente() {
		return entityManager().createQuery(
				"select p from Pedido p where p.mesa is null and (p.status = 'ANDAMENTO' or p.status = 'ENVIADO') ",
				Pedido.class).getResultList();
	}

	public BigDecimal findBetweenDates(Date date1, Date date2) {
		return (BigDecimal) entityManager().createQuery(
				"select sum(p.valorTotal) from Pedido p where p.status = 'PAGO' and date(p.data) BETWEEN date(:data1) AND date(:data2)  ",
				BigDecimal.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}
	
	public BigDecimal findBetweenDatesFunc(Date date1, Date date2,Funcionario f) {
		return (BigDecimal) entityManager().createQuery(
				"select sum(p.valorTotal) from Pedido p where p.funcionario = :func and p.status = 'PAGO' and date(p.data) BETWEEN date(:data1) AND date(:data2)  ",
				BigDecimal.class).setParameter("data1", date1).setParameter("data2", date2).setParameter("func", f).getSingleResult();

	}

	public BigDecimal findBetweenDatesDelivery(Date date1, Date date2) {
		return (BigDecimal) entityManager().createQuery(
				"select sum(p.valorTotal) from Pedido p where p.mesa is null and ( p.status = 'PAGO' and p.data BETWEEN :data1 AND :data2 ) ",
				BigDecimal.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}

	public Long findBetweenDatesCount(Date date1, Date date2) {
		return (Long) entityManager().createQuery(
				"select count(p) from Pedido p where p.status = 'PAGO' and p.data BETWEEN :data1 AND :data2 ) ",
				Long.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}
	
	

	public Long findBetweenNumbers(Date date1, Date date2) {
		return (Long) entityManager().createQuery(
				"select count(p) from Pedido p where p.status = 'PAGO' and (date(p.data) BETWEEN date(:data1) AND date(:data2))  ",
				Long.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}

	public List<ProdutoAdapter> findBetweenDatesFaturamento(Date date1, Date date2) {
		return entityManager().createQuery(
				"select new com.projeto.model.adapter.ProdutoAdapter(sum((ip.valorUnitario*ip.quantidade)),date(ip.pedido.data),sum(ip.produto.custo * ip.quantidade),count(distinct ip.pedido.id)) "
						+ "from ItemPedido ip where ip.pedido.status = 'PAGO' and date(ip.pedido.data) BETWEEN date(:data1) AND date(:data2) group by date(ip.pedido.data)  ",
				ProdutoAdapter.class).setParameter("data1", date1).setParameter("data2", date2).getResultList();

	}

	public List<Pedido> filtrados(PedidoFilter filtro) {

		Session session = this.entityManager().unwrap(Session.class);

		Criteria criteria = session.createCriteria(Pedido.class);

		if (filtro.getCodigo() != null) {
			criteria.add(Restrictions.eq("id", filtro.getCodigo()));
		}

		if (filtro.getFuncionario() != null) {
			criteria.add(Restrictions.eq("funcionario", filtro.getFuncionario()));
		}

		if (filtro.getCliente() != null) {
			criteria.add(Restrictions.eq("cliente", filtro.getCliente()));
		}

		if (filtro.getStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}

		if (filtro.isDelivery()) {
			criteria.add(Restrictions.eq("delivery", filtro.isDelivery()));
		}

		if (filtro.getValorDe() != null) {
			criteria.add(Restrictions.ge("valorTotal", filtro.getValorDe()));
		}

		if (filtro.getValorAte() != null) {
			criteria.add(Restrictions.le("valorTotal", filtro.getValorAte()));
		}

		if (filtro.getDataDe() != null) {
			criteria.add(Restrictions.ge("data", filtro.getDataDe()));
		}

		if (filtro.getDataAte() != null) {
			criteria.add(Restrictions.le("data", filtro.getDataAte()));
		}
		System.out.println(filtro.getDataDe());
		return criteria.list();
	}

}
