package slGal.LiveEdu;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.UserName;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import slGal.LiveEdu.ORM.PersonInf;

/**
 *
 * @author mlch
 */
public class ApiGoogle {

    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "DirectoryCommandLine";

    /** Directory to store user credentials. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".store/oauth2_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** OAuth 2.0 scopes. */
    private static final List<String> SCOPES = Arrays.asList(DirectoryScopes.ADMIN_DIRECTORY_USER
            // ,"https://www.googleapis.com/auth/userinfo.profile"
            // ,"https://www.googleapis.com/auth/userinfo.email"
    );

    private static GoogleClientSecrets clientSecrets;

    /** Authorizes the installed application to access user's protected data. */
    private static Credential authorize(Path path) throws Exception {
        // load client secrets
        
        File clientSecret = path.resolve("../WEB-INF/resources/client_secrets/client_secrets.json").toFile();
        clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY,
                        new InputStreamReader(new FileInputStream(clientSecret)));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
                    + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void CreateUser(Path path, List<PersonInf> listPerson) throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        Credential credential = authorize(path);

        Directory directory =
                new Directory.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
                        "DirectoryCommandLine").build();       
        
        for (PersonInf person : listPerson) {
            User user = new User();
            // populate are the required fields only
            UserName name = new UserName();
            name.setFamilyName(person.getFirstnameEn());
            name.setGivenName(person.getLastnameEn());
            user.setName(name);
            user.setPassword(person.getPassCorporate());
            user.setPrimaryEmail(person.getEmailCorporate());
            
            user = directory.users().insert(user).execute();
        }

        //return user;
    }
}
