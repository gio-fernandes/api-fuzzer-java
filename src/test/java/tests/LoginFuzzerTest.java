package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FuzzPayloads;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.LoggerUtil;

import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginFuzzerTest extends BaseTest {

    static Stream<Map<String, Object>> payloadProvider() {
        return FuzzPayloads.invalidLoginPayloads().stream();
    }

    @ParameterizedTest(name = "Login payload: {0}")
    @MethodSource("payloadProvider")
    void fuzzLoginEndpoint(Map<String, Object> payload) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(jsonPayload)
                .when()
                .post("/auth/login")
                .then()
                .extract()
                .response();

        String log = """
                ----------------------------------------
                Payload: %s
                Status: %d
                Response: %s
                ----------------------------------------
                """.formatted(
                jsonPayload,
                response.statusCode(),
                response.getBody().asString()
        );

        System.out.println(log);
        LoggerUtil.saveLog(log);

        assertNotEquals(500, response.statusCode());

        assertTrue(
                response.statusCode() == 200 ||
                        response.statusCode() == 400 ||
                        response.statusCode() == 401 ||
                        response.statusCode() == 403
        );
    }
}