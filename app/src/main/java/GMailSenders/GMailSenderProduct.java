package GMailSenders;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSenderProduct extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSenderProduct(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body,
                                      String sender, String recipients, String productName, int productPrice,
                                      int productQuantity, String firstName, String lastName, String phoneNumber,
                                      String country, String city, String street, String userQuantity)

    throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setContent(
                "<html>\n" +
                        "                        <head>\n" +
                        "                        <meta charset=\\\"ult-8\\\">\n" +
                        "                        <center>\n" +
                        "                        <img src=\"https://sun9-44.userapi.com/impg/2zDHbyhTDAulK0Ow4wd3Z8F58rbuADZ9UzYqmw/xZqhChmSz4E.jpg?size=1347x550&quality=96&sign=3cc508715273a1ec96f65b9c32732aa3&type=album\" alt=\"vk.com\" style=\"width:50%;height:50%\">\n" +
                        "                        <hr>\n" +
                        "                        </center>\n" +
                        "                        <title>E-mail</title>\n" +
                        "                        <style>\n" +
                        "                        font {\n" +
                        "                        font-family: 'Times New Roman', serif;\n" +
                        "                        }\n" +
                        "                        </style>\n" +
                        "                        </head>\n" +
                        "                        <body text=\\\"navy\\\" link=\\\"silver\\\" vlink=\\\"black\\\" alink=\\\"platinum\\\">\n" +
                        "                        <center>\n" +
                        "                        <h1>Product Purchase!</h1>\n" +
                        "                        </center>\n" +
                        "                        <center>\n" +
                        "                        <hr>\n" +
                        "                        <h2>\n" +
                        "                        Product:\n" +
                        "                        <h2>\n" +
                        "                        Name: " +productName +"\n" +
                        "                        <h2>\n" +
                        "                        Price: " +productPrice +" tg\n" +
                        "                        <h2>\n" +
                        "                        Quantity: "+productQuantity +"\n" +
                        "                        <hr>\n" +
                        "                        <h2>\n" +
                        "                        User:\n" +
                        "                        <h2>\n" +
                        "                        Fisrt Name: "+firstName +"\n" +
                        "                        <h2>\n" +
                        "                        Last Name: "+lastName +"\n" +
                        "                        <h2>\n" +
                        "                        E-mail: " +recipients +"\n"+
                        "                        <h2>\n" +
                        "                        Phone Number: "+phoneNumber +"\n" +
                        "                        <h2>\n" +
                        "                        Country: "+country +"\n" +
                        "                        <h2>\n" +
                        "                        City: "+city +"\n" +
                        "                        <h2>\n" +
                        "                        Street: "+street +"\n" +
                        "                        <h2>\n" +
                        "                        User quantity: "+userQuantity +"\n" +
                        "                        </center>\n" +
                        "                        </body>\n" +
                        "                        </html>\n",
                "text/html");

        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sender));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sender));

        Transport.send(message);
    }
}