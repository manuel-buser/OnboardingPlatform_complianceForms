package OnboardingPlatform.complianceForms.MailService;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.repository.CustomerRepository;
import OnboardingPlatform.complianceForms.service.CustomerService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

@Service
public class GMailer {

    private static final String TEST_EMAIL = "demoonboarding8@gmail.com";
    private final Gmail service;
    private CustomerService customerService;

    // Constructor for dependency injection
    public GMailer(CustomerService customerService) throws Exception {
        this.customerService = customerService;
        this.service = createGmailService(); // Initialize service to null for now
    }



    private Gmail createGmailService() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        return new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("OnboardingPlatform_Mailer")
                .build();
    }

    // NetHttpTransport and GSONFactory only internally needed in the API
    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GMailer.class.getResourceAsStream("/client_secret_746884760670-bsl9nku7t13712i7ekhd9j8s586n8i1d.apps.googleusercontent.com.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public void sendMail(String subject, String message) throws Exception {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();
        System.out.println(lastCreatedCustomer);

        if (lastCreatedCustomer != null) {
            String recipientEmail = lastCreatedCustomer.getEmailAddress(); // Get recipient email from the last created customer

            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);

            String senderEmail = "demoonboarding8@gmail.com";

            email.setFrom(new InternetAddress(senderEmail));
            email.addRecipient(TO, new InternetAddress(recipientEmail));
            email.setSubject(subject);
            email.setText(message);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message msg = new Message();
            msg.setRaw(encodedEmail);

            try {
                msg = service.users().messages().send("me", msg).execute();
                System.out.println("Message id: " + msg.getId());
                System.out.println(msg.toPrettyString());
            } catch (GoogleJsonResponseException e) {
                GoogleJsonError error = e.getDetails();
                if (error.getCode() == 403) {
                    System.err.println("Unable to send message: " + e.getDetails());
                } else {
                    throw e;
                }
            }
        } else {
            System.out.println("Last created customer not found.");
        }

    }





}
