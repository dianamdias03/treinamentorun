/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EnviarEmailTeste {

    public static void main(String[] args) {

        EnviarEmailTeste enviarEmail = new EnviarEmailTeste();
        enviarEmail.testHtmlEmail();
    }

    public void enviar(String email, String titulo, String corpo) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.zoho.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("m3acessoriaesportiva@zoho.com", "2Fcontext61325*0*3556907111810_*");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("m3acessoriaesportiva@zoho.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(titulo);
            message.setText(corpo);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testHtmlEmail() {

        HtmlEmail email = new HtmlEmail();
//        email.setHostName("smtp.zoho.com");
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
//        email.setSmtpPort(587);
        email.setSSLOnConnect(true);
//        email.setTLS(true);
//        email.setAuthentication("m3acessoriaesportiva@zoho.com", "2Fcontext61325*0*3556907111810_*");
        email.setAuthentication("adrianomdias@gmail.com", "addfbmdndaia");
        email.setCharset("UTF-8");

        try {
            email.addTo("adrianomdias@gmail.com");

            email.setFrom("m3acessoriaesportiva@zoho.com", "M3 Grupo de Corridas");
            email.setSubject("Titulo do email");

            email.setHtmlMsg("<html><body><b>Atleta: Adriano Dias</b><br><b>Periodo: 18/set a 24/set</b><br><br><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Segunda&#45;feira&nbsp;&#45;&nbsp;18&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Terca&#45;feira&nbsp;&#45;&nbsp;19&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Natacao</b>:<br>&#45;&nbsp;Nado&nbsp;de&nbsp;costas\n"
                    + "&#45;&nbsp;Nado&nbsp;livre\n"
                    + "&#45;&nbsp;Nado&nbsp;leve<br></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;2km&nbsp;leve\n"
                    + "&#45;10x&nbsp;250m&nbsp;p&#x2F;&nbsp;60&lsquo;&lsquo;&nbsp;&#x28;&nbsp;interv&nbsp;freq&nbsp;bx&nbsp;130bpm&#x29;\n"
                    + "&#45;3km&nbsp;leve<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Quarta&#45;feira&nbsp;&#45;&nbsp;20&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Quinta&#45;feira&nbsp;&#45;&nbsp;21&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;&nbsp;5&nbsp;km&nbsp;leve\n"
                    + "&#45;&nbsp;10&nbsp;km&nbsp;forte\n"
                    + "&#45;&nbsp;5&nbsp;km&nbsp;leve<br></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>Treino&nbsp;coletivo&nbsp;no&nbsp;parque&nbsp;das&nbsp;nacoes&nbsp;as&nbsp;19&#x3A;30<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Sexta&#45;feira&nbsp;&#45;&nbsp;22&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Sabado&nbsp;&#45;&nbsp;23&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>15&nbsp;km&nbsp;leve<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Domingo&nbsp;&#45;&nbsp;24&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;&nbsp;10&nbsp;min&nbsp;caminhada\n"
                    + "&#45;&nbsp;20&nbsp;minutos&nbsp;forte\n"
                    + "&#45;&nbsp;10&nbsp;minutos&nbsp;leve\n"
                    + "&#45;&nbsp;20&nbsp;minutos&nbsp;fonte\n"
                    + "&#45;&nbsp;10&nbsp;minutos&nbsp;leve<br></td></tr></table><br><br></body></html>");
            email.setTextMsg("Your email client does not support HTML messages");

            email.send();

        } catch (EmailException ex) {
            Logger.getLogger(EnviarEmailTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
