/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

public class EnviarEmail {

    public static void main(String[] args) {

        EnviarEmail enviarEmail = new EnviarEmail();

        enviarEmail.enviar("m3acessoriaesportiva@zoho.com",
                "Teste de email da M3 Acessoria Esportiva",
                "Filipe,"
                + "\n\nEm breve você receberá os seus treinos por email!");

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
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("m3acessoriaesportiva@zoho.com", "a1b2c3d4");
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

    public void enviarHtml(String enderecoEmail, String titulo, String corpo) {

        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setHostName("smtp.zoho.com");
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.setAuthentication("m3acessoriaesportiva@zoho.com", "a1b2c3d4");
        email.setCharset("UTF-8");

        try {
            email.addTo(enderecoEmail);

            email.setFrom("m3acessoriaesportiva@zoho.com", "M3 Acessoria");
            email.setSubject(titulo);
            email.setHtmlMsg(corpo);
//            email.setTextMsg("Seu cliente de email nao suporta mensagens no formato HTML");

            email.send();

        } catch (EmailException ex) {
            Logger.getLogger(teste.EnviarEmailTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
