package com.adailsilva.model.adapter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.adailsilva.model.Produto;

import lombok.Getter;
import lombok.Setter;

public class ProdutoAdapter {

	@Getter
	@Setter
	private Produto produto;

	@Getter
	@Setter
	private Long quantidade;

	@Getter
	@Setter
	private Date date;

	@Getter
	@Setter
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Getter
	@Setter
	private BigDecimal custoTotal = BigDecimal.ZERO;

	@Getter
	@Setter
	private BigDecimal lucroTotal = BigDecimal.ZERO;

	@Getter
	@Setter
	private Long quantidadePedidos;

	public ProdutoAdapter(BigDecimal valorTotal, Date date, BigDecimal custo, Long numerosPedidos) {
		this.valorTotal = valorTotal;
		this.date = date;
		this.custoTotal = custo;
		this.quantidadePedidos = numerosPedidos;
		this.lucroTotal = this.lucroTotal.add(this.valorTotal).subtract(this.custoTotal);
	}

	public ProdutoAdapter(Produto produto, Long quantidade, Date date) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.date = date;
	}

	public ProdutoAdapter(Produto produto, Long quantidade, Date date, int d) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.date = date;
	}

	public ProdutoAdapter(Produto produto, Long quantidade, Integer mes, Integer ano) {
		this.produto = produto;
		this.quantidade = quantidade;
		DateFormat formatter = new SimpleDateFormat("MM/yyyy");
		try {
			date = (Date) formatter.parse(mes + "/" + ano);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		// System.out.println(
		// "produto : " + produto.getDescricao() + "data : " + dt.format(date) +
		// " quantidade : " + quantidade);
	}

	public ProdutoAdapter(Produto produto, Long quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;

	}

	public BigDecimal getLucro() {

		return produto.getPreco().subtract(produto.getCusto()).multiply(new BigDecimal(this.quantidade));
	}

	public BigDecimal getCusto() {

		return produto.getCusto().multiply(new BigDecimal(this.quantidade));
	}

}
