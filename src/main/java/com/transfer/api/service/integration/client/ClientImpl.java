package com.transfer.api.service.integration.client;

import com.google.gson.Gson;
import com.transfer.api.service.integration.client.response.ClientResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@Slf4j
public class ClientImpl implements Client {

    private final Gson gson = new Gson();
    private static final String CLIENT_SERVICE = "clientService";

    // Optimized HttpClient with strict timeouts for ECS/Fargate
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Override
    @Cacheable(cacheNames = "validateIfTheClientExists")
    @CircuitBreaker(name = CLIENT_SERVICE, fallbackMethod = "fallbackValidateClient")
    public ClientResponse validateIfTheClientExists(final String idClient) {
        log.info("Validating existence for client ID: {}", idClient);

        String uriClient = "http://localhost:9090/clientes/" + idClient;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriClient))
                .timeout(Duration.ofSeconds(5))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Client {} not found (404).", idClient);
                return ClientResponse.builder().build();
            }

            if (statusCode >= 400) {
                throw new RuntimeException("Client service returned error status: " + statusCode);
            }

            ClientResponse clientResponse = this.gson.fromJson(response.body(), ClientResponse.class);
            log.info("Client {} successfully validated.", clientResponse.id());
            return clientResponse;

        } catch (IOException | InterruptedException e) {
            log.error("Communication failure with client service for ID: {}", idClient);
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new RuntimeException("Client integration I/O failure", e);
        }
    }

    /**
     * Fallback method triggered when the circuit is OPEN or an exception occurs.
     */
    public ClientResponse fallbackValidateClient(String idClient, Throwable e) {
        log.error("Circuit Breaker triggered for client {}. Reason: {}", idClient, e.getMessage());

        // Returning a builder with an error message so the business logic can decide how to proceed
        return ClientResponse.builder()
                .id(idClient)
                .error("Client service is temporarily unavailable")
                .build();
    }
}