import java.util.Date;

import com.projeto.model.Produto;

import lombok.Getter;
import lombok.Setter;

public class ProdutoAdapter {

	@Getter
	@Setter
	private Produto produto;

	@Getter
	@Setter
	private Integer quantidade;

	@Getter
	@Setter
	private Date date;

	public ProdutoAdapter(Produto produto, Integer quantidade, Date date) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.date = date;
	}

	public ProdutoAdapter(Produto produto, Integer quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;

	}

}
