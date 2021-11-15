package com.adailsilva.test;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class HtmlTest {

	public static void main(String[] args) {
		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName("smtp.hotmail.com");
		email.setSslSmtpPort("465");
		email.setAuthenticator(new DefaultAuthenticator("adail101@hotmail.com", "@Hacker101"));
		try {
			email.setFrom("adail101@hotmail.com", "AdailSilva");
			email.setDebug(true);
			email.setSubject("Assunto do E-mail");

			StringBuilder builder = new StringBuilder();
			builder.append("<h1 style = \"color:red;\" >Test</h1>");
			builder.append(
					"<p>AdailSilva <b>AdailSilva</b>. AdailSilva</p>");

			email.setHtmlMsg(builder.toString());
			email.addTo("adail101@hotmail.com");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}
