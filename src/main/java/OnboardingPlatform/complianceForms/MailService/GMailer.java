package OnboardingPlatform.complianceForms.MailService;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.service.CustomerService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;

@Service
public class GMailer {

    private static final String admin_email = "demoonboarding8@gmail.com";
    private final Gmail service;
    @Autowired
    private CustomerService customerService;

    public GMailer() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("OnboardingPlatform_Mailer")
                .build();
    }

    // NetHttpTransport and GSONFactory only internally needed in the API
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GMailer.class.getResourceAsStream("/client_secret_746884760670-bsl9nku7t13712i7ekhd9j8s586n8i1d.apps.googleusercontent.com.json")));


        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND)) // scope = what are we authorized to do
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow,receiver).authorize("user");
    }

    public void sendMail(String subject, String message) throws Exception {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();

        if (lastCreatedCustomer != null) {
            String recipientEmail = lastCreatedCustomer.getEmailAddress();

            // Encode as MIME message
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(admin_email));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipientEmail));
            email.setSubject(subject);
            email.setText(message);

            // Encode and wrap the MIME message into a gmail message
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            byte[] encodedBytes = Base64.getMimeEncoder().encode(rawMessageBytes); // Use MIME encoder

            String encodedEmail = Base64.getEncoder().encodeToString(encodedBytes); // Convert to Base64-encoded string

            // Set the encoded message in the 'raw' field of the Message object
            Message msg = new Message();
            msg.setRaw(encodedEmail);

            try {
                // send message
                msg = service.users().messages().send("me", msg).execute();
                System.out.println("Message id: " + msg.getId());
                System.out.println(msg.toPrettyString());
            } catch (GoogleJsonResponseException e) {
                // Exception handling
            }
        } else {
            System.out.println("Last created customer not found.");
        }
    }

    public static void sendEmail () throws Exception {
        System.out.println("Sending email...");

        String subject = "Subject of the email";
        String message = """
            Dear reader 
                
            Hello World.
                
            Best regards,
            myself
            """;

        new GMailer().sendMail(subject, message);
    }




}
