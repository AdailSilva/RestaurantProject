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

import com.projeto.model.adapter.ClienteAdapter;
import com.projeto.repository.ClienteRepository;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Named
public class ClientesGraficsBean implements Serializable {

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
	private List<ClienteAdapter> clienteAdapterList = new ArrayList<ClienteAdapter>();

	@Getter
	private List<ClienteAdapter> clienteAdapter = new ArrayList<ClienteAdapter>();

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

	@Inject
	private ClienteRepository clienteRepo;

	@Getter
	private Long maxValue = new Long(0);

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
		preencherLista(format);

		Collections.reverse(clienteAdapterList);

		format = new SimpleDateFormat("dd/MM");
		for (ClienteAdapter produto : clienteAdapterList) {
			chartSeries.set(format.format(produto.getData()), 0);

		}
		realizarConsulta(format, c, chartSeries);
		maxValue += 10;

		model.addSeries(chartSeries);

		return model;

	}

	public void realizarConsulta(SimpleDateFormat format, Calendar c, ChartSeries chartSeries) {
		if (menuLineModel == 1) {
			format = new SimpleDateFormat("dd/MM");
			categoryName = "Semana";
			c.add(Calendar.DAY_OF_YEAR, -7);
		} else if (menuLineModel == 2) {
			categoryName = "Mês";
			format = new SimpleDateFormat("dd/MM");
			c.add(Calendar.MONTH, -1);
		} else if (menuLineModel == 3) {
			categoryName = "Ano";
			c.add(Calendar.YEAR, -1);
			format = new SimpleDateFormat("MM/yyyy");
		}
		clienteAdapter = clienteRepo.findBetweenDatesCount(c.getTime(), new Date());
		for (ClienteAdapter cliente : clienteAdapter) {
			chartSeries.set(format.format(cliente.getData()), cliente.getNumeros());
			if (cliente.getNumeros() > maxValue) {
				maxValue = cliente.getNumeros();
			}
		}
	}

	public void preencherLista(SimpleDateFormat format) {
		Calendar c = Calendar.getInstance();

		if (menuLineModel == 1) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 7; i++) {
				ClienteAdapter produto = new ClienteAdapter(null, null);
				produto.setNumeros(new Long(0));
				produto.setData(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));

				c.add(Calendar.DAY_OF_YEAR, -1);
				clienteAdapterList.add(produto);

			}
		} else if (menuLineModel == 2) {
			format = new SimpleDateFormat("dd/MM");
			for (int i = 0; i < 30; i++) {
				ClienteAdapter produto = new ClienteAdapter(null, null);
				produto.setNumeros(new Long(0));
				produto.setData(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				c.add(Calendar.DAY_OF_YEAR, -1);
				clienteAdapterList.add(produto);

			}
		} else if (menuLineModel == 3) {
			format = new SimpleDateFormat("MM/yyyy");
			for (int i = 0; i < 12; i++) {
				ClienteAdapter produto = new ClienteAdapter(null, null);
				produto.setNumeros(new Long(0));
				produto.setData(DateUtils.truncate(c.getTime(), java.util.Calendar.DAY_OF_MONTH));
				c.add(Calendar.MONTH, -1);
				clienteAdapterList.add(produto);

			}
		}
	}

	public void escolherRadio() {
		criarLineModel();

	}

}
