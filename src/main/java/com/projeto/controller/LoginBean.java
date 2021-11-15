
package com.projeto.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.projeto.mail.MailConfig;
import com.projeto.repository.ProdutoRepository;
import com.projeto.util.jsf.FacesUtil;

import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletRequest request;

	@Inject
	private HttpServletResponse response;
	@Getter
	@Setter
	private String usuario;

	@Inject
	private MailConfig mail;

	@Inject
	private ProdutoRepository produtoRepo;

	public void preRender(ComponentSystemEvent e) throws ParseException {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		Date dt1 = c.getTime();

		Date dt2 = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("entrei");
		produtoRepo.findAll();
//		produtoRepo.teste(c.getTime(), dt2, produtoRepo.findBy(new Integer(1))).forEach(g -> {
//			System.out.println(dt.format(g.getDate()) + "  produto  " + g.getProduto() + " qtd " + g.getQuantidade());
//		});
		;
		if ("true".equals(request.getParameter("invalid"))) {
			FacesUtil.addErrorMessage("Usuário ou senha inválido!");
		}

	}

	public void login() throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(request, response);

		facesContext.responseComplete();
	}

	

}
