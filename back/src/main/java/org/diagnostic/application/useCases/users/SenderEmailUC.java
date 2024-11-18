package org.diagnostic.application.useCases.users;


import java.io.*;
        import java.util.*;
        import java.util.logging.Logger;
import java.util.logging.Level;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.activation.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Component
@Service
@Slf4j
public class SenderEmailUC {

    @Positive
    private int smtpPort = 587; // Puerto típico para Zoho con STARTTLS

    @NotBlank
    private String smtpHost = "smtp.zoho.com"; // Servidor SMTP de Zoho

    @NotBlank
    private String mailUser = "info@d-iagnostic.com.ar"; // Tu dirección de correo

    @NotBlank
    private String mailUserPassword = "Seaprueba2024"; // Contraseña del correo

    @NotBlank
    private String fromEmail = "info@d-iagnostic.com.ar"; // Correo remitente

    @NotBlank
    private String toEmail = "recipient@example.com"; // Dirección de correo destinatario

    private String ccEmail = null; // Opcional, si no se usa se puede dejar como null

    @NotBlank
    private String subject = "Asunto predeterminado"; // Asunto del correo

    @NotBlank
    private String content = "Cuerpo del correo predeterminado"; // Contenido del correo

    private String schema = null; // Opcional, puede usarse para algún esquema de datos adjuntos

    private String filename = null; // Opcional, nombre del archivo adjunto, si aplica

    @NotNull
    private boolean SMTPStartTLSEnable = true; // STARTTLS habilitado para Zoho

    @NotNull
    private boolean SMTPHostTrusted = true; // Confía en el host SMTP

    @NotNull
    private boolean htmlContent = true; // Contenido del correo en formato HTML

    //@Scheduled(cron = "*/5 * * * * *")
    public void automaticMailSending(){
        log.info("Email sent!");
    }

    public static void sendEmail(String smtpHost, int smtpPort, String mailUser, String mailUserPassword,
                                 boolean isSMTPHostTrusted, boolean isSMTPStartTLSEnable,
                                 String fromEmail, String toEmail, String ccEmail,
                                 String subject, String content, boolean htmlContent, Attachment... attachments)
            throws Exception {

        // Create a mail session
        Session session = getMailSession(smtpHost, smtpPort, mailUser, mailUserPassword, true, true);

        // Construct the message
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg = setTORecipients(msg, toEmail);
        if (!ccEmail.isEmpty()){
            msg = setCCRecipients(msg, ccEmail);
        }
        msg.setSubject(subject);
        if (attachments != null && attachments.length > 0 ) {
            addContentAndAttachments(msg, content, htmlContent, attachments);
        }
        else{
            msg.setContent(content, "text/html");
        }

        // Send the message
        Transport.send(msg);
        Logger logger = Logger.getLogger("Email");
        logger.setLevel(Level.INFO);
        logger.info("Email sent!");

    }

    private static Session getMailSession(String smtpHost, int smtpPort, String mailUser, String mailUserPassword,
                                          boolean isSMTPStartTLSEnable, boolean isSMTPHostTrusted) {
        Session session = null;

        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "" + smtpPort);
        //int timeout = 60000;
        //props.put("mail.smtp.timeout", timeout);
        if(isSMTPStartTLSEnable){
            props.put("mail.smtp.starttls.enable", "true");
        }

        if (isSMTPHostTrusted) {
            props.put("mail.smtp.ssl.trust", smtpHost);
        }

        if(!mailUser.isEmpty() || !mailUserPassword.isEmpty()){
            props.put("mail.smtp.user", mailUser);
            props.put("mail.smtp.auth", "true");
            Authenticator auth = new SMTPAuthenticator(mailUser, mailUserPassword);
            //session = Session.getDefaultInstance(props, auth);
            session = Session.getInstance(props, auth);
        } else {
            //session = Session.getDefaultInstance(props);
            session = Session.getInstance(props);
        }

        return session;
    }

    private static class SMTPAuthenticator extends jakarta.mail.Authenticator {
        private String fUser;
        private String fPassword;

        public SMTPAuthenticator(String user, String password) {
            fUser = user;
            fPassword = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fUser, fPassword);
        }
    }

    public static class Attachment {
        private String name;
        private File file;

        public Attachment(String name, File file){
            this.name = name;
            this.file = file;
        }
    }

    private static Message setTORecipients(Message msg, String emails) throws MessagingException {

        int countEmails;
        StringTokenizer emailList = new StringTokenizer(emails, ",");
        countEmails = emailList.countTokens();

        InternetAddress[] address = new InternetAddress[countEmails];
        for (int i = 0; i < countEmails; i ++) {
            address[i] = new InternetAddress(emailList.nextToken());
        }
        msg.setRecipients(Message.RecipientType.TO, address);
        return msg;
    }

    private static Message setCCRecipients(Message msg, String emails) throws MessagingException {
        int countEmails;
        StringTokenizer emailList = new StringTokenizer(emails, ",");
        countEmails = emailList.countTokens();

        InternetAddress[] address = new InternetAddress[countEmails];
        for (int i = 0; i < countEmails; i ++) {
            address[i] = new InternetAddress(emailList.nextToken());
        }
        msg.setRecipients(Message.RecipientType.CC, address);
        return msg;
    }

    private static void addContentAndAttachments(Message msg, String content, boolean htmlContent, Attachment... attachments)
            throws MessagingException{

        Multipart multipart = new MimeMultipart();
        // content
        MimeBodyPart messagePart = new MimeBodyPart();
        if (htmlContent){
            messagePart.setContent(content, "text/html"); //MESSAGE_CONTENT_TYPE
        }
        else{
            messagePart.setText(content);
        }
        multipart.addBodyPart(messagePart);
        // attachments
        for (int i = 0; i < attachments.length; i++){
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fileData = new FileDataSource(attachments[i].file);
            DataHandler dh = new DataHandler(fileData);
            attachmentPart.setDataHandler(dh);
            attachmentPart.setFileName(attachments[i].name);

            multipart.addBodyPart(attachmentPart);
        }
        //
        msg.setContent(multipart);
    }

    private static final int SMTP_PORT = 587; // Puerto SMTP para Zoho con STARTTLS
    private static final String SMTP_HOST = "smtp.zoho.com"; // Servidor SMTP de Zoho
    private static final String MAIL_USER = "info@d-iagnostic.com.ar"; // Tu dirección de correo
    private static final String MAIL_USER_PASSWORD = "Seaprueba2024"; // Contraseña del correo
    private static final String FROM_EMAIL = "info@d-iagnostic.com.ar"; // Correo remitente
    private static final boolean SMTP_STARTTLS_ENABLE = true; // Habilitar STARTTLS
    private static final boolean SMTP_HOST_TRUSTED = true; // Confiar en el host SMTP
    private static final boolean HTML_CONTENT = true; // Contenido en formato HTML

    /**
     * Envía un correo electrónico utilizando la configuración predeterminada.
     *
     * @param toEmail  Dirección de correo destinatario.
     * @param subject  Asunto del correo.
     * @param content  Cuerpo del correo.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    public void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        // Configuración de propiedades para el envío de correo
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.ssl.trust", SMTP_HOST_TRUSTED ? SMTP_HOST : ""); // Confianza en el host

        // Autenticación
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_USER, MAIL_USER_PASSWORD);
            }
        });

        // Crear el mensaje
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);

        if (HTML_CONTENT) {
            message.setContent(content, "text/html");
        } else {
            message.setText(content);
        }

        // Enviar el correo
        Transport.send(message);
        System.out.println("Correo enviado con éxito a " + toEmail);
    }

}