import javax.inject.Inject;

import org.junit.Test;

import com.proteto.repository.ProdutoRepository;

public class Datas {

	@Inject
	private ProdutoRepository produto;

	@Test
	public void teste() {
		produto.findAll();
	}

}
