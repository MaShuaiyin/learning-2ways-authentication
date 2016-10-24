package me.yingrui.learning.bidirection.ssl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * http://www.rap.ucar.edu/staff/paddy/cacerts
 * http://www.rap.ucar.edu/staff/paddy/cacerts/index.html
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GreetingControllerTest {

    @Value("${local.server.port}")
    int port;

    String password = "changeit";

    @Test
    public void should_connect_to_server_and_get_response() throws IOException {
        Properties systemProps = System.getProperties();
        systemProps.put("javax.net.ssl.trustStore", "./keystore/keystore.jks");
        systemProps.put("javax.net.ssl.trustStorePassword", password);
        System.setProperties(systemProps);

        try {
            // Open a secure connection.
            URL url = new URL(String.format("https://localhost:%d/hello", port));
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            // Set up the connection properties
            con.setRequestProperty("Connection", "close");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(30000);
            con.setReadTimeout(30000);
            con.setRequestMethod("GET");

            File pKeyFile = new File("keystore/cid.p12");
            String pKeyPassword = password;
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream keyInput = new FileInputStream(pKeyFile);
            keyStore.load(keyInput, pKeyPassword.toCharArray());
            keyInput.close();
            keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            SSLSocketFactory sockFact = context.getSocketFactory();
            con.setSSLSocketFactory(sockFact);

            // Check for errors
            int responseCode = con.getResponseCode();
            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.getInputStream();
            } else {
                inputStream = con.getErrorStream();
            }

            // Process the response
            BufferedReader reader;
            String line = null;
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            assertEquals("hello world", sb.toString());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}