package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.EndpointData;
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

public class ApiFuzzerTest extends BaseTest {

    static Stream<EndpointData> endpointProvider() {
        return FuzzPayloads.endpoints().stream();
    }

    @ParameterizedTest(name = "Fuzzing: {0}")
    @MethodSource("endpointProvider")
    void fuzzEndpoints(EndpointData endpoint) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        for (Map<String, Object> payload : endpoint.getPayloads()) {

            String jsonPayload = mapper.writeValueAsString(payload);

            Response response = sendRequest(
                    endpoint.getMethod(),
                    endpoint.getPath(),
                    jsonPayload
            );

            String log = """
                    ==================================================
                    Endpoint: %s
                    Method: %s
                    Path: %s
                    Payload: %s
                    Status: %d
                    Response: %s
                    ==================================================
                    """.formatted(
                    endpoint.getName(),
                    endpoint.getMethod(),
                    endpoint.getPath(),
                    jsonPayload,
                    response.statusCode(),
                    response.getBody().asString()
            );

            System.out.println(log);
            LoggerUtil.saveLog(log);

            // 🚨 Detecta erro crítico real
            if (response.statusCode() == 500) {
                System.out.println("🚨 BUG CRÍTICO ENCONTRADO!");
                LoggerUtil.saveLog("🚨 BUG CRÍTICO: " + endpoint.getPath() + " | Payload: " + jsonPayload);
            }

            // ❌ NÃO pode quebrar
            assertNotEquals(
                    500,
                    response.statusCode(),
                    "A API retornou 500 no endpoint " + endpoint.getPath()
            );

            // ✅ Regra de fuzzing correta (melhor que lista fixa)
            assertTrue(
                    response.statusCode() < 500,
                    "Erro crítico detectado! Status: " + response.statusCode()
            );
        }
    }

    private Response sendRequest(String method, String path, String body) {

        return switch (method.toUpperCase()) {

            case "POST" -> given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .post(path)
                    .then()
                    .extract()
                    .response();

            case "PUT" -> given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .put(path)
                    .then()
                    .extract()
                    .response();

            case "PATCH" -> given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .patch(path)
                    .then()
                    .extract()
                    .response();

            default -> throw new IllegalArgumentException("Método HTTP não suportado: " + method);
        };
    }
}