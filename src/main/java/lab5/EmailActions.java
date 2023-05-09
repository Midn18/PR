package lab5;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class EmailActions {
    static String IMAP = "imap.gmail.com";
    static String POP3 = "pop.gmail.com";
    static String USER = "you email address";
    static String PASSWORD = "your password generated by google";
    static String SMTP = "smtp.gmail.com";

    static Store store;

    public static void establishConnectionImap(String host, String user, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.ssl.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            store = emailSession.getStore("imap");
            store.connect(host, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void establishConnectionPop3(String host, String user, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.ssl.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            store = emailSession.getStore("pop3");
            store.connect(host, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getMailList(String host, String user, String password) {
        try {
            if (host.equals(IMAP)) {
                establishConnectionImap(host, user, password);
            } else {
                establishConnectionPop3(host, user, password);
            }

            // Accesați folderul de e-mailuri și preluați lista de mesaje
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();

            // Afisati lista de e-mail-uri
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("Email #" + (i + 1) + ":");
                System.out.println("  From: " + message.getFrom()[0]);
                System.out.println("  Subject: " + message.getSubject());
                System.out.println("  Sent Date: " + message.getSentDate());
            }

            // Inchideți conexiunea la serverul IMAP
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadEmailWithAttachment(String host, String user, String password, int messageIndex) {
        try {
            if (host.equals(IMAP)) {
                establishConnectionImap(host, user, password);
            } else {
                establishConnectionPop3(host, user, password);
            }

            // Accesati folderul de e-mailuri si preluati lista de mesaje
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();

            // Extragem mesajul dorit
            Message message = messages[messageIndex];

            // Afisam informatii despre mesaj
            System.out.println("Email:");
            System.out.println("  From: " + message.getFrom()[0]);
            System.out.println("  Subject: " + message.getSubject());
            System.out.println("  Sent Date: " + message.getSentDate());

            // Verificam daca mesajul are atasamente
            if (message.getContent() instanceof Multipart) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        // Descarcam atasamentul
                        String fileName = bodyPart.getFileName();
                        InputStream inputStream = bodyPart.getInputStream();
                        File file = new File(fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                        fileOutputStream.close();
                        inputStream.close();
                        System.out.println("Attachment downloaded: " + fileName);
                    }
                }
            }
            // Inchidem conexiunea la serverul IMAP
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTextEmail(String host, String user, String password, String to, String subject, String text) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, password);
                        }
                    });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendEmailsWithAttachments(String host, String user, String password, String to, String subject, String text) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, password);
                        }
                    });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Email with Attachment");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Please see the attached file for details.");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String filename = "src/main/java/lab5/fileName.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

