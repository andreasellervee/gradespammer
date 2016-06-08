import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class EmailSender {

    public static void sendEmail(String table) throws IOException {

        Properties prop = new Properties();
        InputStream input = new FileInputStream("config.properties");
        prop.load(input);

        try {
            Properties mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            MimeMessage generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(prop.getProperty("results.to")));

            generateMailMessage.setSubject("BPM Results");
            generateMailMessage.setContent(table, "text/html");

            Transport transport = getMailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", prop.getProperty("gmail.user"), prop.getProperty("gmail.password"));
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}