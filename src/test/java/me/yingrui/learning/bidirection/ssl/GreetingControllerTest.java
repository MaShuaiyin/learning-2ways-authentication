package me.yingrui.learning.bidirection.ssl;

import com.jayway.restassured.RestAssured;
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

import java.io.IOException;

import static com.jayway.restassured.RestAssured.expect;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GreetingControllerTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void onSetUp() {
        RestAssured.port = port;
    }

    @Test
    public void should_return_oauth_token_with_grant_type_of_password() throws IOException {
        Response response = expect()
                .statusCode(HttpStatus.SC_OK)
                .given()
                .when().get("/hello");

        System.out.println(response.getStatusLine());
        System.out.println(response.getHeaders().toString());
        ResponseBody body = response.getBody();
        body.print();
        Assert.assertEquals("hello world", body.asString());
    }


}