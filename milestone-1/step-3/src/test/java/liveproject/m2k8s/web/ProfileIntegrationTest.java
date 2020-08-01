package liveproject.m2k8s.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import liveproject.m2k8s.Profile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {"spring.main.allow-bean-definition-overriding=true", "server.servlet.context-path=/"})
@ActiveProfiles("test")
public class ProfileIntegrationTest {

    @LocalServerPort
    private int port;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:"+port;
    }

    @Test
    public void givenURI_whenAddingAndReading_thenVerifyResponse() throws Exception {
        RequestSpecification request = RestAssured.given();
        Profile joejoe = Profile.builder()
                .username("joejoe")
                .password("joejoe")
                .firstName("joejoe")
                .lastName("joejoe")
                .email("joejoe@joejoe.com")
                .build();
        String payload = objectMapper.writeValueAsString(joejoe);
        request.header("Content-Type", "application/json");
        request.body(payload);
        Response response = request.post("/profile");
        given().get("http://localhost:"+port+"/profile/joejoe")
                .then()
                .statusCode(200);
    }
}