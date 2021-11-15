import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Session;

import com.projeto.model.Menu;

public class MainlyClassJava implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();
		Session session = (Session) manager.unwrap(Session.class);
		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		//
		// Produto p = new Produto();
		// Categoria c = manager.find(Categoria.class, new Long(1));
		// p.setCategoria(c);
		// p.setCusto(new BigDecimal(10));
		// p.setPreco(new BigDecimal(10));
		// p.setDescricao("teste");
		// p.setUrlImagem("xxx");

		// manager.merge(p);
		// Cartao c = manager.find(Cartao.class, new Integer(1));
		// System.out.println(c.getBandeira());
		// List<Menu> subm = manager.createQuery("select c from MenuSubMenu mf "
		// + "join Menu m on mf.menuPai = m.id " + "join Menu m2 on mf.menuFilho
		// = m2.id "
		// + "where m.id = 3", Menu.class)
		// .getResultList();
		//
		// subm.forEach(s -> {
		// System.out.println(s.getId());
		// });
		// Menu m = manager.find(Menu.class, new Long(6));
		// Menu mfilhos = manager
		// .createQuery("select mp from Menu mf JOIN mf.menuPai mp where
		// mf.menuPai = mp.id", Menu.class)
		// .getSingleResult();
		// System.out.println(mfilhos.getDescricao());

		// for (Menu menu : mfilhos) {
		// System.out.println(menu.getDescricao());
		// }

		// manager.createQuery(
		// "SELECT ctrl FROM ControleCartaProtocolada ctrl INNER JOIN
		// ctrl.usuario u INNER JOIN ctrl.condomino c where ctrl.statusCarta =
		// :status and c.nome like :name")
		// .getResultList();

		// select mp.descricao as 'Pai',mf.descricao 'Filho' from Menu mf
		// join Menu mp on mf.menuPai_id = mp.id
		// Menu m2 = new Menu();
		// m2.setDescricao("dieth");
		// m2.setTipo("Bebida");
		// m2.setMenuPai(m);
		//
		// manager.merge(m2);
		@SuppressWarnings("removal")
		Menu mp = manager.find(Menu.class, new Long(1));
		// Menu m = new Menu();
		// m.setDescricao("com gás");
		// m.setTipo("Bebida");
		// m.setMenuPai(mp);
		// manager.merge(m);
		mp.getSubMenus().forEach(mf -> {
			System.out.println("Meu Pai " + mf.getMenuPai().getDescricao());
			System.out.println("Meu filho " + mf.getDescricao());
			System.out.println();
		});
		trx.commit();
		session.close();

	}
}
