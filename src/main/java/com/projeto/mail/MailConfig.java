package com.projeto.mail;

import java.io.Serializable;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import lombok.Getter;

//@RequestScoped
public class MailConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private HtmlEmail email = new HtmlEmail();

	public MailConfig() {
		email.setSSLOnConnect(true);
		email.setHostName("smtp.gmail.com");
		email.setSslSmtpPort("465");
		email.setAuthenticator(new DefaultAuthenticator("joaoftnunes1@gmail.com", "823a5320"));
	}

//	public void sendMail(String destinatárioMail, String assunto, String mensagem) {
//
//		try {
//			email.setFrom("joaoftnunes1@gmail.com", " Restaurante Manager");
//			email.setDebug(true);
//			email.setSubject(assunto);
//			email.setHtmlMsg(mensagem);
//			email.addTo(destinatárioMail);
//			email.buildMimeMessage();
//			email.sendMimeMessage();
//			
//
//		} catch (EmailException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void sendMail2(String remetenteMail, String nomeRemetente, String destinatárioMail, String assunto) {
//
//		try {
//			email.setFrom(remetenteMail, nomeRemetente);
//			email.setDebug(true);
//			email.setSubject(assunto);
//			StringBuilder builder = new StringBuilder();
//			builder.append("<h1 style = \"color:red;\" >Tituloasdasdasdasdas Email</h1>");
//			builder.append("<p>Corpo ezxczxczxczxczxczxcmail</p>");
//			email.setHtmlMsg(builder.toString());
//			email.addTo(destinatárioMail);
//			email.buildMimeMessage();
//			email.sendMimeMessage();
//
//		} catch (EmailException e) {
//			e.printStackTrace();
//		}
//	}
}
