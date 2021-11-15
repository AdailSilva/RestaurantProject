package Grafics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.projeto.model.adapter.ProdutoAdapter;

public class GraficsTest {

	public static void main(String[] args) {
		List<ProdutoAdapter> produtos = new ArrayList<>();
		Calendar c = Calendar.getInstance();

		for (int i = 0; i < 30; i++) {
			ProdutoAdapter produto = new ProdutoAdapter(null, null,null);
			c.add(Calendar.DAY_OF_YEAR, -1);
			produto.setDate(c.getTime());
			produtos.add(produto);

		}

		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		produtos.forEach(e -> {
			System.out.println(dt.format(e.getDate()));
		});
		
		System.out.println(produtos.size());

	}

}
