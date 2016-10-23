package me.yingrui.learning.bidirection.ssl;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.CertificateAuthSettings;
import com.jayway.restassured.config.SSLConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import static com.jayway.restassured.RestAssured.expect;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GreetingControllerTest {

    @Value("${local.server.port}")
    int port;

    String password = "changeit";

    @Before
    public void onSetUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.port = port;
    }

    @Test
    public void should_connect_to_server_and_get_response() throws IOException {
        SSLConfig sslConfig = new SSLConfig()
                .trustStore("/Users/twer/workspace/learning-2ways-authentication/keystore/truststore.jks", "changeit")
                .keystore("/Users/twer/workspace/learning-2ways-authentication/keystore/keystore.jks", "changeit")
                .allowAllHostnames().relaxedHTTPSValidation();

        CertificateAuthSettings pkcs12 = new CertificateAuthSettings().
                trustStore(sslConfig.getTrustStore()).
                trustStoreType("PKCS12");

        Response response = expect()
                .statusCode(HttpStatus.SC_OK)
                .given()
//                .config(RestAssured.config().sslConfig(sslConfig))
                .auth().certificate("/Users/twer/workspace/learning-2ways-authentication/keystore/cid.p12", "changeit", pkcs12)
                .when().get(String.format("https://localhost:%d/hello", port));

        System.out.println(response.getStatusLine());
        System.out.println(response.getHeaders().toString());
        ResponseBody body = response.getBody();
        body.print();
        Assert.assertEquals("hello world", body.asString());
    }


}