import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class HtmlTest {

	public static void main(String[] args) {
		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName("smtp.gmail.com");
		email.setSslSmtpPort("465");
		email.setAuthenticator(new DefaultAuthenticator("joaoftnunes1@gmail.com", "823a5320"));
		try {
			email.setFrom("joaoftnunes1@gmail.com", "João");
			email.setDebug(true);
			email.setSubject("Assunto do E-mail");

			StringBuilder builder = new StringBuilder();
			builder.append("<h1 style = \"color:red;\" >Titlo</h1>");
			builder.append(
					"<p>Lorem ipsum dolor sit amet, <b>consectetur adipiscing elit</b>. Duis nec aliquam tortor. Sed dignissim dolor ac est consequat egestas. Praesent adipiscing dolor in consectetur fringilla.</p>");
			builder.append("<a href=\"http://wwww.botecodigital.info\">Boteco Digital</a> <br> ");
			builder.append("<img src=\"http://www.botecodigital.info/wp-content/themes/boteco/img/logo.png\">");

			email.setHtmlMsg(builder.toString());
			email.addTo("joaoftnunes1@gmail.com");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}
