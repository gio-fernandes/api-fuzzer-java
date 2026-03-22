package base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import utils.Config;

public class BaseTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = Config.BASE_URL;
    }
}