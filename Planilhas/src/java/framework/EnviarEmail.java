/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;

public class EnviarEmail {

    public static final String email = "m3esporte@zoho.com";
    public static final String senha = "2Fcontext61325*0*3556907111810_*";
    public static final String reply = "marceloolimpio2010@hotmail.com";

    public static void main(String[] args) {

        EnviarEmail enviarEmail = new EnviarEmail();

        enviarEmail.enviarHtml(
                "adrianomdias@gmail.com",
                null,
                "Teste de email da M3 Grupo de Corridas",
                "Adriano,"
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
                        return new PasswordAuthentication(EnviarEmail.email, EnviarEmail.senha);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EnviarEmail.email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(titulo);
            message.setText(corpo);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean enviarHtml(String enderecoEmail, String enderecoEmail2, String titulo, String corpo) {

        boolean retorno;
        ImageHtmlEmail objetoEmail = new ImageHtmlEmail();
        objetoEmail.setSmtpPort(465);
        objetoEmail.setSSLOnConnect(true);
        objetoEmail.setHostName("smtp.zoho.com");
        objetoEmail.setAuthentication(EnviarEmail.email, EnviarEmail.senha);
        objetoEmail.setCharset("UTF-8");

        try {
            objetoEmail.addTo(enderecoEmail);
            if (enderecoEmail2 != null) {
                objetoEmail.addTo(enderecoEmail2);
            }

            objetoEmail.setFrom(EnviarEmail.email, "M3 Grupo de Corridas");
            objetoEmail.addReplyTo(EnviarEmail.reply, "Treinador Marcelo Olimpio");
            objetoEmail.addReplyTo("m3esporte@zoho.com", "Treinador Marcelo Olimpio");
            objetoEmail.setSubject(titulo);
            objetoEmail.setHtmlMsg(corpo);
//            email.setTextMsg("Seu cliente de email nao suporta mensagens no formato HTML");

            objetoEmail.send();

            retorno = true;
        } catch (EmailException ex) {
            Arquivo.gravarLog("Erro enviando email para " + enderecoEmail + ": " + ex.getMessage());
//            Logger.getLogger(teste.EnviarEmailTeste.class.getName()).log(Level.SEVERE, null, ex);
            retorno = false;
        }
        return retorno;
    }

}
