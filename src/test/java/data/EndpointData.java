package data;

import java.util.List;
import java.util.Map;

public class EndpointData {

    private final String name;
    private final String method;
    private final String path;
    private final List<Map<String, Object>> payloads;
    private final List<Integer> allowedStatusCodes;

    public EndpointData(String name,
                        String method,
                        String path,
                        List<Map<String, Object>> payloads,
                        List<Integer> allowedStatusCodes) {
        this.name = name;
        this.method = method;
        this.path = path;
        this.payloads = payloads;
        this.allowedStatusCodes = allowedStatusCodes;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public List<Map<String, Object>> getPayloads() {
        return payloads;
    }

    public List<Integer> getAllowedStatusCodes() {
        return allowedStatusCodes;
    }

    @Override
    public String toString() {
        return name + " [" + method + " " + path + "]";
    }
}