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
/*
        enviarEmail.enviar("m3acessoriaesportiva@zoho.com",
                "Teste de email da M3 Acessoria Esportiva",
                "Filipe,"
                + "\n\nEm breve você receberá os seus treinos por email!");
*/
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
        email.setHostName("smtp.zoho.com");
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.setAuthentication("m3acessoriaesportiva@zoho.com", "2Fcontext61325*0*3556907111810_*");
        email.setCharset("UTF-8");

        try {
            email.addTo("adrianomdias@gmail.com");

            email.setFrom("m3acessoriaesportiva@zoho.com", "M3 Acessoria");
            email.setSubject("Titulo do email");

//            URL url = new URL("http://localhost:8084/planilhas/testeemail.html");
//            String cid2 = email.embed(url, "logo ??.gif");

            email.setHtmlMsg("<html><body><b>Atleta: Adriano Dias</b><br><b>Periodo: 18/set a 24/set</b><br><br><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Segunda&#45;feira&nbsp;&#45;&nbsp;18&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Terca&#45;feira&nbsp;&#45;&nbsp;19&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Natacao</b>:<br>&#45;&nbsp;Nado&nbsp;de&nbsp;costas\n" +
"&#45;&nbsp;Nado&nbsp;livre\n" +
"&#45;&nbsp;Nado&nbsp;leve<br></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;2km&nbsp;leve\n" +
"&#45;10x&nbsp;250m&nbsp;p&#x2F;&nbsp;60&lsquo;&lsquo;&nbsp;&#x28;&nbsp;interv&nbsp;freq&nbsp;bx&nbsp;130bpm&#x29;\n" +
"&#45;3km&nbsp;leve<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Quarta&#45;feira&nbsp;&#45;&nbsp;20&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Quinta&#45;feira&nbsp;&#45;&nbsp;21&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;&nbsp;5&nbsp;km&nbsp;leve\n" +
"&#45;&nbsp;10&nbsp;km&nbsp;forte\n" +
"&#45;&nbsp;5&nbsp;km&nbsp;leve<br></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>Treino&nbsp;coletivo&nbsp;no&nbsp;parque&nbsp;das&nbsp;nacoes&nbsp;as&nbsp;19&#x3A;30<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Sexta&#45;feira&nbsp;&#45;&nbsp;22&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\">&#x2A;&#x2A;&nbsp;FOLGA&nbsp;&#x2A;&#x2A;<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Sabado&nbsp;&#45;&nbsp;23&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>15&nbsp;km&nbsp;leve<br></td></tr></table><br><table border=1 style=\"width: 100%; border-collapse: collapse; border-style: solid; border-color: #f2f2fa #f2f2fa; \"><tr style=\"background-color: #f2f2f2\"><td style=\"padding: 10px;\"><h3>Domingo&nbsp;&#45;&nbsp;24&#x2F;set</h3></td></tr><tr><td style=\"padding: 15px;\"><b>Corrida</b>:<br>&#45;&nbsp;10&nbsp;min&nbsp;caminhada\n" +
"&#45;&nbsp;20&nbsp;minutos&nbsp;forte\n" +
"&#45;&nbsp;10&nbsp;minutos&nbsp;leve\n" +
"&#45;&nbsp;20&nbsp;minutos&nbsp;fonte\n" +
"&#45;&nbsp;10&nbsp;minutos&nbsp;leve<br></td></tr></table><br><br></body></html>");
            email.setTextMsg("Your email client does not support HTML messages");

            email.send();

        } catch (EmailException ex) {
            Logger.getLogger(EnviarEmailTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/*
    public static void sendResetPwdMail(String appCode, ODocument user) throws Exception {
        final String errorString = "Cannot send mail to reset the password: ";

        //check method input
        if (!user.getSchemaClass().getName().equalsIgnoreCase(UserDao.MODEL_NAME)) {
            throw new PasswordRecoveryException(errorString + " invalid user object");
        }

        //initialization
        String siteUrl = Application.NETWORK_HTTP_URL.getValueAsString();
        int sitePort = Application.NETWORK_HTTP_PORT.getValueAsInteger();
        if (StringUtils.isEmpty(siteUrl)) {
            throw new PasswordRecoveryException(errorString + " invalid site url (is empty)");
        }

        String textEmail = PasswordRecovery.EMAIL_TEMPLATE_TEXT.getValueAsString();
        String htmlEmail = PasswordRecovery.EMAIL_TEMPLATE_HTML.getValueAsString();
        if (StringUtils.isEmpty(htmlEmail)) {
            htmlEmail = textEmail;
        }
        if (StringUtils.isEmpty(htmlEmail)) {
            throw new PasswordRecoveryException(errorString + " text to send is not configured");
        }

        boolean useSSL = PasswordRecovery.NETWORK_SMTP_SSL.getValueAsBoolean();
        boolean useTLS = PasswordRecovery.NETWORK_SMTP_TLS.getValueAsBoolean();
        String smtpHost = PasswordRecovery.NETWORK_SMTP_HOST.getValueAsString();
        int smtpPort = PasswordRecovery.NETWORK_SMTP_PORT.getValueAsInteger();
        if (StringUtils.isEmpty(smtpHost)) {
            throw new PasswordRecoveryException(errorString + " SMTP host is not configured");
        }

        String username_smtp = null;
        String password_smtp = null;
        if (PasswordRecovery.NETWORK_SMTP_AUTHENTICATION.getValueAsBoolean()) {
            username_smtp = PasswordRecovery.NETWORK_SMTP_USER.getValueAsString();
            password_smtp = PasswordRecovery.NETWORK_SMTP_PASSWORD.getValueAsString();
            if (StringUtils.isEmpty(username_smtp)) {
                throw new PasswordRecoveryException(errorString + " SMTP username is not configured");
            }
        }
        String emailFrom = PasswordRecovery.EMAIL_FROM.getValueAsString();
        String emailSubject = PasswordRecovery.EMAIL_SUBJECT.getValueAsString();
        if (StringUtils.isEmpty(emailFrom)) {
            throw new PasswordRecoveryException(errorString + " sender email is not configured");
        }

        try {
            String userEmail = ((ODocument) user.field(UserDao.ATTRIBUTES_VISIBLE_ONLY_BY_THE_USER)).field("email").toString();

            String username = (String) ((ODocument) user.field("user")).field("name");

            //Random
            String sRandom = appCode + "%%%%" + username + "%%%%" + UUID.randomUUID();
            String sBase64Random = new String(Base64.encodeBase64(sRandom.getBytes()));

            //Save on DB
            ResetPwdDao.getInstance().create(new Date(), sBase64Random, user);

            //Send mail
            HtmlEmail email = null;

            URL resetUrl = new URL(Application.NETWORK_HTTP_SSL.getValueAsBoolean() ? "https" : "http", siteUrl, sitePort, "/user/password/reset/" + sBase64Random);

            //HTML Email Text
            ST htmlMailTemplate = new ST(htmlEmail, '$', '$');
            htmlMailTemplate.add("link", resetUrl);
            htmlMailTemplate.add("user_name", username);
            htmlMailTemplate.add("token", sBase64Random);

            //Plain text Email Text
            ST textMailTemplate = new ST(textEmail, '$', '$');
            textMailTemplate.add("link", resetUrl);
            textMailTemplate.add("user_name", username);
            textMailTemplate.add("token", sBase64Random);

            email = new HtmlEmail();

            email.setHtmlMsg(htmlMailTemplate.render());
            email.setTextMsg(textMailTemplate.render());

            //Email Configuration
            email.setSSL(useSSL);
            email.setSSLOnConnect(useSSL);
            email.setTLS(useTLS);
            email.setStartTLSEnabled(useTLS);
            email.setStartTLSRequired(useTLS);
            email.setSSLCheckServerIdentity(false);
            email.setSslSmtpPort(String.valueOf(smtpPort));
            email.setHostName(smtpHost);
            email.setSmtpPort(smtpPort);
            email.setCharset("utf-8");

            if (PasswordRecovery.NETWORK_SMTP_AUTHENTICATION.getValueAsBoolean()) {
                email.setAuthenticator(new DefaultAuthenticator(username_smtp, password_smtp));
            }
            email.setFrom(emailFrom);
            email.addTo(userEmail);

            email.setSubject(emailSubject);
            if (BaasBoxLogger.isDebugEnabled()) {
                StringBuilder logEmail = new StringBuilder()
                        .append("HostName: ").append(email.getHostName()).append("\n")
                        .append("SmtpPort: ").append(email.getSmtpPort()).append("\n")
                        .append("SslSmtpPort: ").append(email.getSslSmtpPort()).append("\n")
                        .append("SSL: ").append(email.isSSL()).append("\n")
                        .append("TLS: ").append(email.isTLS()).append("\n")
                        .append("SSLCheckServerIdentity: ").append(email.isSSLCheckServerIdentity()).append("\n")
                        .append("SSLOnConnect: ").append(email.isSSLOnConnect()).append("\n")
                        .append("StartTLSEnabled: ").append(email.isStartTLSEnabled()).append("\n")
                        .append("StartTLSRequired: ").append(email.isStartTLSRequired()).append("\n")
                        .append("SubType: ").append(email.getSubType()).append("\n")
                        .append("SocketConnectionTimeout: ").append(email.getSocketConnectionTimeout()).append("\n")
                        .append("SocketTimeout: ").append(email.getSocketTimeout()).append("\n")
                        .append("FromAddress: ").append(email.getFromAddress()).append("\n")
                        .append("ReplyTo: ").append(email.getReplyToAddresses()).append("\n")
                        .append("BCC: ").append(email.getBccAddresses()).append("\n")
                        .append("CC: ").append(email.getCcAddresses()).append("\n")
                        .append("Subject: ").append(email.getSubject()).append("\n")
                        //the following line throws a NPE in debug mode
                        //.append("Message: ").append(email.getMimeMessage().getContent()).append("\n")

                        .append("SentDate: ").append(email.getSentDate()).append("\n");
                BaasBoxLogger.debug("Password Recovery is ready to send: \n" + logEmail.toString());
            }
            email.send();

        } catch (EmailException authEx) {
            BaasBoxLogger.error("ERROR SENDING MAIL:" + ExceptionUtils.getStackTrace(authEx));
            throw new PasswordRecoveryException(errorString + " Could not reach the mail server. Please contact the server administrator");
        } catch (Exception e) {
            BaasBoxLogger.error("ERROR SENDING MAIL:" + ExceptionUtils.getStackTrace(e));
            throw new Exception(errorString, e);
        }

    }
    */
}
