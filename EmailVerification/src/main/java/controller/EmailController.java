package controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@RestController(value = "/emailVerification")
public class EmailController {

    @RequestMapping(value = "/{emailId}", method = RequestMethod.GET)
    public OTPDao getOTP(@PathVariable("emailId") String emailId) {

        OTPDao otpObject = new OTPDao();

        // admin id and password;
        final String username = "rohillatanuj65@gmail.com";
        final String password = "Qwerty@123";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("rohillatanuj65@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailId)
            );

            final int max = 999999, min = 100000;
            int otp = ThreadLocalRandom.current().nextInt(min, max + 1);
            message.setSubject("One Time Password");
            message.setText("Dear Users,"
                    + "\n\nTo complete the Registration Process, please enter verification code on the Registration page" + "\n\nVerification code : " + otp + "\n\n Thanks");

            Transport.send(message);

            System.out.println("Done");

            // setting otp object
            otpObject.setOtp(otp);


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return otpObject;
    }
}
