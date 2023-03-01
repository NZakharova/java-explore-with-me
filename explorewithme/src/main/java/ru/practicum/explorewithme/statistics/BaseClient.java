package ru.practicum.explorewithme.statistics;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<T> get(String path, Class<T> type) {
        return get(path, type, null);
    }

    protected <T> ResponseEntity<T> get(String path, Class<T> type, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(type, HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<T> post(String path, Class<T> type, T body) {
        return post(path, type, null, body);
    }

    protected <T> ResponseEntity<T> post(String path, Class<T> type, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(type, HttpMethod.POST, path, parameters, body);
    }

    protected <T> ResponseEntity<T> put(String path, Class<T> type, T body) {
        return put(path, type, null, body);
    }

    protected <T> ResponseEntity<T> put(String path, Class<T> type, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(type, HttpMethod.PUT, path, parameters, body);
    }

    protected <T> ResponseEntity<T> patch(String path, Class<T> type, T body) {
        return patch(path, type, null, body);
    }

    protected <T> ResponseEntity<T> patch(String path, Class<T> type) {
        return patch(path, type, null, null);
    }

    protected <T> ResponseEntity<T> patch(String path, Class<T> type, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(type, HttpMethod.PATCH, path, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null);
    }

    protected ResponseEntity<Object> delete(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(Object.class, HttpMethod.DELETE, path, parameters, null);
    }

    private <T> ResponseEntity<T> makeAndSendRequest(Class<T> type, HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, null);

        ResponseEntity<T> serverResponse;
        if (parameters != null) {
            serverResponse = rest.exchange(path, method, requestEntity, type, parameters);
        } else {
            serverResponse = rest.exchange(path, method, requestEntity, type);
        }
        return prepareGatewayResponse(serverResponse);
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}