package com.adailsilva.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.format.annotation.NumberFormat;

import com.adailsilva.repository.ItemPedidoRepository;
import com.adailsilva.repository.PedidoRepository;
import com.adailsilva.security.UsuarioLogado;
import com.adailsilva.security.UsuarioSistema;
import com.adailsilva.service.MailService;

import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named
public class DashboardBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	private ScheduleModel eventModel;

	@Inject
	private PedidoRepository pedidoRepo;
	@Inject
	private ItemPedidoRepository itemRepo;

	@Getter
	private Long pedidosDiarios = new Long(0);
	@Getter
	private Long cozinha = new Long(0);

	@Getter
	private Long garcon = new Long(0);

	@Getter
	private Long itensDiarios = new Long(0);

	@Getter
	private BigDecimal pedidosDia = BigDecimal.ZERO;

	@Getter
	private BigDecimal pedidosSemana = BigDecimal.ZERO;

	@Getter
	private BigDecimal deliveryDia = BigDecimal.ZERO;

	

	@Inject
	@Getter
	@UsuarioLogado
	private UsuarioSistema usuario;

	

	@PostConstruct
	public void init() {

		eventModel = new DefaultScheduleModel();

		usuario.getUsuario().getAcesso().forEach(e -> {
			System.out.println(e.getDescricao());
		});

		if (usuario.getUsuario().temAcesso("ADMINISTRADORES")) {
			consultasAdmin();
			consultasFuncionario();
			System.out.println("ADMIN");
		}
		if (usuario.getUsuario().temAcesso("VENDEDOR")) {
			consultasFuncionario();
		}
		if (usuario.getUsuario().temAcesso("CLIENTE")) {

		}

	}

	public void consultasAdmin() {
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.WEEK_OF_MONTH, -1);
		Date date = new Date();
		pedidosSemana = pedidoRepo.findBetweenDates(c1.getTime(), date);
		c1 = Calendar.getInstance();

		c1.add(Calendar.DAY_OF_YEAR, -1);
		pedidosDia = pedidoRepo.findBetweenDates(new Date(), date);
		deliveryDia = pedidoRepo.findBetweenDatesDelivery(c1.getTime(), date);

	}

	public void consultasFuncionario() {
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_YEAR, -1);
		Date date = new Date();
		cozinha = itemRepo.pendentesCozinhaCount();

		garcon = itemRepo.pendentesGarconCount();

		itensDiarios = itemRepo.findBetweenDatesCount(new Date(), date);

		pedidosDiarios = pedidoRepo.findBetweenNumbers(new Date(), date);

	}

	
}