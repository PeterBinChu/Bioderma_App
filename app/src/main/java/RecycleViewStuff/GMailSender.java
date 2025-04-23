package RecycleViewStuff;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private String sheesh="sheesh";

    static {
        Security.addProvider(new JSSEProvider());
    }

    public GMailSender(String user, String password) {
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
                                      String sender, String recipients, String newPass) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setContent(
                "<html>\n" +
                        "<head>\n" +
                        "<meta charset=\"ult-8\">\n" +
                        "<center>\n" +
                        "<img src=\"https://sun9-44.userapi.com/impg/2zDHbyhTDAulK0Ow4wd3Z8F58rbuADZ9UzYqmw/xZqhChmSz4E.jpg?size=1347x550&quality=96&sign=3cc508715273a1ec96f65b9c32732aa3&type=album\" alt=\"vk.com\" style=\"width:50%;height:50%\">\n" +
                        "<hr>\n" +
                        "</center>\n" +
                        "<title>E-mail</title>\n" +
                        "<style>\n" +
                        "font {\n" +
                        "font-family: 'Times New Roman', serif;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body text=\"navy\" link=\"silver\" vlink=\"black\" alink=\"platinum\">\n" +
                        "<center>\n" +
                        "<h1>Almost done!</h1>\n" +
                        "</center>\n" +
                        "<center>\n" +
                        "<hr>\n" +
                        "<h2>\n" +
                        "to confirm your email address, please enter this code in the app:\n" +
                        "</center>\n" +
                        "<center>\n" +
                        "<table cellspacing=\"5\" cellpadding=\"5\">\n" +
                        "              <tr>\n" +
                        "                  <center>\n" +
                        "                    <td class=”button” bgcolor=\"#757eff\">\n" +
                        "                    <font size=\"6\" color=\"#ffffff\">\n" +
                        newPass + "\n" +
                        "                          </font>\n" +
                        "                     </td>\n" +
                        "                  </center>\n" +
                        "              </tr>\n" +
                        "          </table>\n" +
                        "</center>\n" +
                        "</body>\n" +
                        "</html>",
                "text/html");

        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        Transport.send(message);
    }
}