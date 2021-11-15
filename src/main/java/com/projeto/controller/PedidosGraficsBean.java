package com.projeto.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import com.projeto.model.adapter.ProdutoAdapter;
import com.projeto.repository.PedidoRepository;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Named
public class PedidosGraficsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private LineChartModel lineModel;

	@Getter
	@Setter
	private Integer menuLineModel;

	@Getter
	private List<ProdutoAdapter> produtoAdapterList = new ArrayList<ProdutoAdapter>();

	@Getter
	private List<ProdutoAdapter> produtoAdapter = new ArrayList<ProdutoAdapter>();

	@Inject
	private PedidoRepository pedidoRepo;

	@Getter
	private BigDecimal lucroTotal = BigDecimal.ZERO;
	@Getter
	private BigDecimal custoTotal = BigDecimal.ZERO;

	@Getter
	@Setter
	private Date dtInicial;
	@Getter
	@Setter
	private Date dtFinal;

	private Axis yAxis;

	@Getter
	private Double maxValue = new Double(0);

	private String categoryName;

	private void criarLineModel() {

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
		Calendar c = Calendar.getInstance();
		LineChartModel model = new LineChartModel();
		ChartSeries chartSeries = new ChartSeries();
		SimpleDateFormat format = null;
		chartSeries.setLabel("Pedidos");
		preencherLista(format, chartSeries);

		realizarConsulta(format, c, chartSeries);

		format = new SimpleDateFormat("dd/MM");
		// unificarValores(chartSeries, format,chartSeries);
		maxValue += 20;

		model.addSeries(chartSeries);

		return model;

	}

	public void realizarConsulta(SimpleDateFormat format, Calendar c, ChartSeries chartSeries) {
		if (menuLineModel == 1) {
			format = new SimpleDateFormat("dd/MM");
			categoryName = "Semana";
			c.add(Calendar.DAY_OF_YEAR, -7);
			produtoAdapter = pedidoRepo.findBetweenDatesFaturamento(c.getTime(), new Date());
		} else if (menuLineModel == 2) {
			categoryName = "Mês";
			format = new SimpleDateFormat("dd/MM");
			c.add(Calendar.MONTH, -1);
			produtoAdapter = pedidoRepo.findBetweenDatesFaturamento(c.getTime(), new Date());
		} else if (menuLineModel == 3) {
			categoryName = "Ano";
			c.add(Calendar.YEAR, -1);
			format = new SimpleDateFormat("MM/yyyy");
			produtoAdapter = pedidoRepo.findBetweenDatesFaturamento(c.getTime(), new Date());
		}

		for (ProdutoAdapter produtoAdapter2 : produtoAdapter) {
			chartSeries.set(format.format(produtoAdapter2.getDate()), produtoAdapter2.getValorTotal());
			if (produtoAdapter2.getValorTotal().doubleValue() > maxValue) {
				maxValue = produtoAdapter2.getValorTotal().doubleValue();
			}
		}
	}

	public void preencherLista(SimpleDateFormat format, ChartSeries chartSeries) {
		Calendar c = Calendar.getInstance();

		if (menuLineModel == 1) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 7; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null, null);
				produto.setQuantidade(new Long(0));

				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				c.add(Calendar.DAY_OF_YEAR, -1);
				produtoAdapterList.add(produto);
			}
		} else if (menuLineModel == 2) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 30; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null, null);
				produto.setQuantidade(new Long(0));
				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				c.add(Calendar.DAY_OF_YEAR, -1);
				produtoAdapterList.add(produto);
			}
		} else if (menuLineModel == 3) {
			format = new SimpleDateFormat("MM/yyyy");
			for (int i = 0; i < 12; i++) {
				ProdutoAdapter produto = new ProdutoAdapter(null, null, null);
				produto.setQuantidade(new Long(0));
				produto.setDate(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				c.add(Calendar.MONTH, -1);
				produtoAdapterList.add(produto);

			}
		}

		Collections.reverse(produtoAdapterList);
		for (ProdutoAdapter e : produtoAdapterList) {
			chartSeries.set(format.format(e.getDate()), 0);
		}

	}

	public void preencherValores(List<ProdutoAdapter> adpterList) {
		lucroTotal = BigDecimal.ZERO;
		custoTotal = BigDecimal.ZERO;
		adpterList.forEach(e -> {

			lucroTotal = lucroTotal.add(e.getLucroTotal());
			custoTotal = custoTotal.add(e.getCustoTotal());

		});
	}

	public void escolherRadio() {
		criarLineModel();
		preencherValores(produtoAdapter);
	}

}
