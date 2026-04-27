package com.transfer.api.service.integration.account;

import com.google.gson.Gson;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
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
public class AccountImpl implements Account {

    private final Gson gson = new Gson();
    private static final String ACCOUNT_SERVICE = "accountService";

    // HttpClient configured with strict timeout to avoid blocking Fargate threads
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Override
    @Cacheable(cacheNames = "searchSourceAccountData")
    @CircuitBreaker(name = ACCOUNT_SERVICE, fallbackMethod = "fallbackSearchAccount")
    public AccountOriginResponse searchSourceAccountData(final String idAccount) {

        log.info("Calling account integration for ID: {}", idAccount);

        // Ideally sourced from @Value or Config Server
        String uriTransferAccountOrigin = "http://localhost:9090/contas/" + idAccount;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriTransferAccountOrigin))
                .timeout(Duration.ofSeconds(5))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                log.info("Account {} not found (404).", idAccount);
                return AccountOriginResponse.builder().build();
            }

            if (response.statusCode() >= 400) {
                // Throwing RuntimeException triggers the Circuit Breaker failure count
                throw new RuntimeException("Account service returned status: " + response.statusCode());
            }

            return this.gson.fromJson(response.body(), AccountOriginResponse.class);

        } catch (IOException | InterruptedException e) {
            log.error("Connection error with account service: {}", e.getMessage());
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new RuntimeException("I/O communication failure", e);
        }
    }

    /**
     * Fallback triggered when:
     * 1. The circuit is OPEN
     * 2. The original method throws an exception
     */
    public AccountOriginResponse fallbackSearchAccount(String idAccountOrigin, Throwable e) {
        log.error("Fallback triggered for account {}. Reason: {}", idAccountOrigin, e.getMessage());

        // Returns an object with an error message indicating unavailability
        // so the business layer can handle it (e.g., prevent the transfer)
        return AccountOriginResponse.builder()
                .id(idAccountOrigin)
                .erro("Information temporarily unavailable")
                .build();
    }
}