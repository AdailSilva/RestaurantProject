package com.adailsilva.repository;

import java.util.Date;
import java.util.List;

import org.apache.deltaspike.data.api.AbstractEntityRepository;
import org.apache.deltaspike.data.api.Repository;

import com.adailsilva.model.ItemPedido;
import com.adailsilva.model.Pedido;

@Repository(forEntity = ItemPedido.class)
public abstract class ItemPedidoRepository extends AbstractEntityRepository<ItemPedido, Integer> {

	public List<ItemPedido> itensCozinha() {
		return entityManager().createQuery("select p from ItemPedido p where p.statusIp='ANDAMENTO' order by p.data ",
				ItemPedido.class).getResultList();
	}

	public List<ItemPedido> itensRetirada() {
		return entityManager().createQuery("select p from ItemPedido p where p.statusIp='RETIRADA' order by p.data ",
				ItemPedido.class).getResultList();
	}

	public List<ItemPedido> itensPorPedido(Pedido pedido) {
		return entityManager().createQuery("select ip from ItemPedido ip where ip.pedido = :pedido  ", ItemPedido.class)
				.setParameter("pedido", pedido).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ItemPedido> itensDiaCorrente() {
		return entityManager().createNativeQuery(
				"select * from item_pedido ip join Pedido p on ip.pedido_id = p.id where date(p.data) = date(now()) ",
				ItemPedido.class).getResultList();
	}

	

	public Long findBetweenNumbers(Date date1, Date date2) {
		return (Long) entityManager().createQuery(
				"select count(p) from ItemPedido p where p.statusIp = 'PAGO' and p.data BETWEEN :data1 AND :data2  ",
				Long.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}

	public Long pendentesCozinhaCount() {

		return entityManager()
				.createQuery("select count(ip) from ItemPedido ip where ip.statusIp = 'ANDAMENTO'", Long.class)
				.getSingleResult();
	}

	public Long pendentesGarconCount() {
		return entityManager()
				.createQuery("select count(ip) from ItemPedido ip where ip.statusIp = 'RETIRADA'", Long.class)
				.getSingleResult();

	}

	public Long findBetweenDatesCount(Date date1, Date date2) {
		return (Long) entityManager().createQuery(
				"select count(p) from ItemPedido p where p.statusIp = 'FINALIZADO' and date(p.data) BETWEEN date(:data1) AND date(:data2) ) ",
				Long.class).setParameter("data1", date1).setParameter("data2", date2).getSingleResult();

	}

}
