package com.projeto.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;

import com.projeto.mail.MailConfig;
import com.projeto.model.Pedido;

public class MailService implements Serializable {

	private static final long serialVersionUID = 1L;

	private MailConfig mail = new MailConfig();
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public void sendMail(Pedido p) {

		try {
			if (p.getCliente() != null) {
				if (p.getCliente() != null) {
					if (StringUtils.isNotBlank(p.getCliente().getEmail())) {
						
						mail.getEmail().setFrom("joaoftnunes1@gmail.com", " Restaurante Manager");
						mail.getEmail().setDebug(true);
						mail.getEmail().setSubject("Pedido " + format.format(p.getData()) + " - Restaurante Manager");
						mail.getEmail().setHtmlMsg(formatarPedido(p));
						mail.getEmail().addTo(p.getCliente().getEmail());
						mail.getEmail().buildMimeMessage();
						mail.getEmail().sendMimeMessage();
						mail = new MailConfig();
					}

				}
			}
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	private String formatarPedido(Pedido p) {

		StringBuilder sb = new StringBuilder();
		sb.append("<table>" + "<tr>" + "<td style=\"width: 100px\">Número:</td> <td>" + p.getId() + "</td> </tr>"
				+ "<tr> <td>Cliente:</td><td>" + p.getCliente().getNome() + " " + p.getCliente().getSobrenome()
				+ "</td> </tr>" + "<tr><td>Valor total:</td><td>" + " R$ " + p.getValorTotal() + "</td>" + "</tr>"
				+ "</table>");

		sb.append("<br/>");
		sb.append(
				" <table border=\"1\" cellspacing=\"0\" cellpadding=\"3\"><tr><th>Produto</th><th>Valor unitário</th> <th>Quantidade</th><th>Valor total</th></tr>");
		p.getProdutos().forEach(e -> {
			sb.append("<tr><td>" + e.getProduto().getDescricao() + "</td><td>R$ " + e.getValorUnitario() + "</td><td>"
					+ e.getQuantidade() + "</td><td>R$ " + e.getValorTotal() + "</td> </tr>");
		});
		sb.append("</table>");
		return sb.toString();
	}

}
