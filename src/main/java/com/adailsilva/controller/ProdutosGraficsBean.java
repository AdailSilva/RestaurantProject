package com.adailsilva.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import com.adailsilva.model.Produto;
import com.adailsilva.model.adapter.ProdutoAdapter;
import com.adailsilva.repository.ProdutoRepository;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Named
public class ProdutosGraficsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private LineChartModel lineModel;

	@Getter
	@Setter
	private List<Produto> produtos = null;

	@Getter
	@Setter
	private Integer menuLineModel;

	@Getter
	@Setter
	private Integer radio1;

	@Getter
	@Setter
	private Integer radio2;

	@Getter
	@Setter
	private PieChartModel pieModel1;

	@Getter
	private List<ProdutoAdapter> produtoAdapter = new ArrayList<ProdutoAdapter>();

	@Getter
	private List<ProdutoAdapter> produtoAdapter2 = new ArrayList<ProdutoAdapter>();

	@Inject
	private ProdutoRepository produtoRepo;

	@Getter
	private BigDecimal lucroTotal;
	@Getter
	private BigDecimal custoTotal;

	@Getter
	@Setter
	private Date dtInicial;
	@Getter
	@Setter
	private Date dtFinal;

	@Getter
	@Setter
	private Integer produtoSelecionado;

	@Getter
	private List<ProdutoAdapter> produtosGraficLine = new ArrayList<>();

	private Axis yAxis;

	@Getter
	private Long maxValue = new Long(0);

	private String categoryName;

	@Getter
	private List<ProdutoAdapter> consultaGraficLine;

	@PostConstruct
	public void carregarProdutos() {
		produtos = produtoRepo.findAll();
		if (produtos == null) {
			produtos = new ArrayList<>();
		}
	}

	public void escolherRadio() {
		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();
		if (radio1 == 1) {
			d1.add(Calendar.DAY_OF_YEAR, -7);
		} else if (radio1 == 2) {
			d1.add(Calendar.DAY_OF_YEAR, -15);
		} else if (radio1 == 3) {
			d1.add(Calendar.MONTH, -1);
		} else if (radio1 == 4) {
			d1.add(Calendar.YEAR, -1);
		}
		produtoAdapter = produtoRepo.findBetweenDates(d1.getTime(), d2.getTime());

		createPieModel();
		preencherValores(produtoAdapter);

	}

	public void escolherRadio2() {

		Calendar d2 = Calendar.getInstance();
		if (radio1 == 1) {
			criarDatasDias(produtoAdapter2, d2, 7, true);
		} else if (radio1 == 2) {
			criarDatasDias(produtoAdapter2, d2, 15, true);
		} else if (radio1 == 3) {
			criarDatasDias(produtoAdapter2, d2, 30, true);
		} else if (radio1 == 4) {
			criarDatasDias(produtoAdapter2, d2, 30, false);
		}
		createPieModel();

	}

	public void criarDatasDias(List<ProdutoAdapter> produtos, Calendar c, int repeticoes, boolean dia) {
		if (dia) {
			for (int i = 0; i < repeticoes; i++) {
				c.add(Calendar.DAY_OF_YEAR, -1);
				ProdutoAdapter produto = new ProdutoAdapter(null, null, c.getTime());
				produtos.add(produto);

			}
		} else {
			for (int i = 0; i < 12; i++) {
				c.add(Calendar.MONTH, -1);
				ProdutoAdapter produto = new ProdutoAdapter(null, null, c.getTime());
				produtos.add(produto);

			}
		}

	}

	public void processar() {
		System.out.println("tamanho " + produtoAdapter.size());
		produtoAdapter = produtoRepo.findBetweenDates(dtInicial, dtFinal);
		createPieModel();
		preencherValores(produtoAdapter);
	}

	private void createPieModel() {
		pieModel1 = new PieChartModel();

		produtoAdapter.forEach(e -> {
			pieModel1.set(e.getProduto().getDescricao(), e.getQuantidade());

		});

		pieModel1.setTitle("Produtos");
		pieModel1.setLegendPosition("w");
	}

	// line model

	private void createLineModels() {

		lineModel = initCategoryModel();
		yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(10);
		lineModel.setTitle("");
		lineModel.setLegendPosition("e");
		lineModel.setShowPointLabels(true);
		lineModel.getAxes().put(AxisType.X, new CategoryAxis(categoryName));
		yAxis = lineModel.getAxis(AxisType.Y);
		yAxis.setLabel("");
		yAxis.setMin(0);
		yAxis.setMax(maxValue);

	}

	private LineChartModel initCategoryModel() {
		lucroTotal = BigDecimal.ZERO;
		custoTotal = BigDecimal.ZERO;
		Calendar c = Calendar.getInstance();
		maxValue = new Long(0);
		LineChartModel model = new LineChartModel();
		ChartSeries boys = new ChartSeries();
		SimpleDateFormat format = null;
		Produto produtoS = produtoRepo.findBy(produtoSelecionado);

		boys.setLabel(produtoS.getDescricao());
		preencherLista(format);
		consultaGraficLine = produtoRepo.findGroupDateDay(c.getTime(), new Date(), produtoS);
		if (menuLineModel == 1) {
			format = new SimpleDateFormat("HH:00");

		} else if (menuLineModel == 2) {
			format = new SimpleDateFormat("dd/MM");
			categoryName = "Semana";
			c.add(Calendar.DAY_OF_YEAR, -7);
			consultaGraficLine = produtoRepo.findGroupDateDay(c.getTime(), new Date(), produtoS);
		} else if (menuLineModel == 3) {
			categoryName = "Mês";
			format = new SimpleDateFormat("dd/MM");
			c.add(Calendar.MONTH, -1);
			consultaGraficLine = produtoRepo.findGroupDateDay(c.getTime(), new Date(), produtoS);
		} else if (menuLineModel == 4) {
			categoryName = "Ano";
			c.add(Calendar.YEAR, -1);
			format = new SimpleDateFormat("MM/yyyy");
			produtoRepo.findGroupDateYear(c.getTime(), new Date(), produtoS);
			consultaGraficLine = produtoRepo.findGroupDateYear(c.getTime(), new Date(), produtoS);
		}

		for (ProdutoAdapter e : produtosGraficLine) {
			for (ProdutoAdapter g : consultaGraficLine) {
				if (format.format(e.getDate()).equals(format.format(g.getDate()))) {
					e.setProduto(g.getProduto());
					e.setQuantidade(g.getQuantidade());
					if (g.getQuantidade() > maxValue) {
						maxValue = g.getQuantidade();
					}
				}
			}
		}

		maxValue += 20;

		Collections.reverse(produtosGraficLine);
		for (ProdutoAdapter produtoAdapter : produtosGraficLine) {
			System.out.println(produtoAdapter.getDate() + " " + produtoAdapter.getQuantidade());

			Integer n1 = Integer.valueOf(produtoAdapter.getQuantidade().toString());
			System.out.println("numero  " + n1);

			boys.set(format.format(produtoAdapter.getDate()), (int) n1);

		}

		model.addSeries(boys);

		return model;

	}

	public void preencherLista(SimpleDateFormat format) {
		Calendar c = Calendar.getInstance();
		int diaCorrente = c.get(Calendar.DAY_OF_MONTH);

		if (menuLineModel == 1) {

			format = new SimpleDateFormat("HH:00");
			for (int i = 0; i < 24; i++) {
				System.out.println(i + " => " + c.get(Calendar.HOUR));
				if (c.get(Calendar.DAY_OF_MONTH) == diaCorrente) {

					ProdutoAdapter produto = new ProdutoAdapter(null, null,null);
					produto.setDate(c.getTime());
					produtosGraficLine.add(produto);
					produto.setQuantidade(new Long(0));
					c.add(Calendar.HOUR_OF_DAY, -1);
				} else {
					break;
				}

			}

		} else if (menuLineModel == 2) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 7; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null,null);
				produto.setQuantidade(new Long(0));

				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				produtosGraficLine.add(produto);
				c.add(Calendar.DAY_OF_YEAR, -1);
			}
		} else if (menuLineModel == 3) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 30; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null,null);
				produto.setQuantidade(new Long(0));
				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				produtosGraficLine.add(produto);

				c.add(Calendar.DAY_OF_YEAR, -1);
			}
		} else if (menuLineModel == 4) {
			format = new SimpleDateFormat("MM/yyyy");
			for (int i = 0; i < 12; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null,null);
				produto.setQuantidade(new Long(0));
				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				produtosGraficLine.add(produto);
				c.add(Calendar.MONTH, -1);

			}
		}
	}

	public void action() {

		createLineModels();
		preencherValores(consultaGraficLine);

	}

	public void preencherValores(List<ProdutoAdapter> adpterList) {
		lucroTotal = BigDecimal.ZERO;
		custoTotal = BigDecimal.ZERO;
		adpterList.forEach(e -> {
			if (e.getProduto() != null) {

				System.out.println(e.getProduto().getDescricao() + " qtd " + e.getQuantidade() + " lucro "
						+ e.getLucro() + " custo : " + e.getCusto());
				lucroTotal = lucroTotal.add(e.getLucro());
				custoTotal = custoTotal.add(e.getCusto());
			}
		});
	}

}
