package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuzzPayloads {

    public static List<Map<String, Object>> invalidLoginPayloads() {
        List<Map<String, Object>> payloads = new ArrayList<>();

        payloads.add(Map.of("username", "", "password", "123456"));
        payloads.add(Map.of("username", "usuario_teste", "password", ""));
        payloads.add(Map.of("username", "A".repeat(5000), "password", "123456"));
        payloads.add(Map.of("username", "' OR '1'='1", "password", "123456"));
        payloads.add(Map.of("username", "<script>alert('xss')</script>", "password", "123456"));
        payloads.add(Map.of("username", "正常ユーザー", "password", "senha_unicode_çãõ"));
        payloads.add(Map.of("username", "admin", "password", "A".repeat(10000)));
        payloads.add(Map.of("username", "admin"));

        Map<String, Object> wrongType = new HashMap<>();
        wrongType.put("username", 12345);
        wrongType.put("password", true);
        payloads.add(wrongType);

        Map<String, Object> nullPayload = new HashMap<>();
        nullPayload.put("username", null);
        nullPayload.put("password", "123456");
        payloads.add(nullPayload);

        return payloads;
    }

    public static List<Map<String, Object>> invalidUserPayloads() {
        List<Map<String, Object>> payloads = new ArrayList<>();

        payloads.add(Map.of("firstName", "", "age", 25));
        payloads.add(Map.of("firstName", "A".repeat(5000), "age", 25));
        payloads.add(Map.of("firstName", "<script>alert('xss')</script>", "age", 25));
        payloads.add(Map.of("firstName", "' OR '1'='1", "age", 25));
        payloads.add(Map.of("firstName", "Maria"));

        Map<String, Object> wrongType = new HashMap<>();
        wrongType.put("firstName", 999);
        wrongType.put("age", "vinte");
        payloads.add(wrongType);

        Map<String, Object> nullPayload = new HashMap<>();
        nullPayload.put("firstName", null);
        nullPayload.put("age", 20);
        payloads.add(nullPayload);

        return payloads;
    }

    public static List<Map<String, Object>> invalidProductPayloads() {
        List<Map<String, Object>> payloads = new ArrayList<>();

        payloads.add(Map.of("title", "", "price", 99));
        payloads.add(Map.of("title", "A".repeat(5000), "price", 99));
        payloads.add(Map.of("title", "<script>alert('xss')</script>", "price", 99));
        payloads.add(Map.of("title", "' OR '1'='1", "price", 99));
        payloads.add(Map.of("title", "Notebook"));

        Map<String, Object> wrongType = new HashMap<>();
        wrongType.put("title", true);
        wrongType.put("price", "cem");
        payloads.add(wrongType);

        Map<String, Object> nullPayload = new HashMap<>();
        nullPayload.put("title", null);
        nullPayload.put("price", 50);
        payloads.add(nullPayload);

        return payloads;
    }

    public static List<EndpointData> endpoints() {
        return List.of(
                new EndpointData(
                        "Login",
                        "POST",
                        "/auth/login",
                        invalidLoginPayloads(),
                        List.of(200, 400, 401, 403)
                ),
                new EndpointData(
                        "Add User",
                        "POST",
                        "/users/add",
                        invalidUserPayloads(),
                        List.of(200, 400, 401, 403)
                ),
                new EndpointData(
                        "Add Product",
                        "POST",
                        "/products/add",
                        invalidProductPayloads(),
                        List.of(200, 201, 400, 401, 403)
                )
        );
    }
}