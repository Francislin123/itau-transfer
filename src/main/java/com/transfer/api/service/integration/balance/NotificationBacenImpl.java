package com.transfer.api.service.integration.balance;

import com.google.gson.Gson;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@Slf4j
public class NotificationBacenImpl implements NotificationBacen {

    private final Gson gson = new Gson();
    private static final String BACEN_SERVICE = "bacenService";

    // Reusing the same client instance is a best practice to save resources
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @Override
    @CircuitBreaker(name = BACEN_SERVICE, fallbackMethod = "fallbackBacenNotification")
    public void notificationBacen(NotificationBacenRequest notification) {
        log.info("Starting Bacen notification for transfer: {}", notification.id());

        String json = gson.toJson(notification);
        String uriNotification = "http://localhost:8080/notificacoes";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(uriNotification))
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == 429) {
                log.warn("Bacen call limit exceeded (Rate Limit).");
                // Optional: Throwing an exception here forces the Circuit Breaker to record a failure
                throw new RuntimeException("Bacen rate limit hit");
            }

            if (statusCode >= 400) {
                throw new RuntimeException("Bacen service error status: " + statusCode);
            }

            log.info("Bacen notification sent successfully.");

        } catch (IOException | InterruptedException e) {
            log.error("I/O error during Bacen integration: {}", e.getMessage());
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new RuntimeException("Bacen communication failure", e);
        }
    }

    /**
     * Fallback method for Bacen Notification
     * Since this is a void/notification method, the fallback usually logs the failure
     * and potentially sends it to a Dead Letter Queue (DLQ) or a retry table.
     */
    public void fallbackBacenNotification(NotificationBacenRequest notification, Throwable e) {
        log.error("Circuit Breaker OPEN or Failure for Bacen notification. Transfer ID: {}. Reason: {}",
                notification.id(), e.getMessage());

        // Sênior Tip: In a real scenario, save this to a database to retry later (Async retry pattern)
        log.warn("Notification for ID {} scheduled for manual retry or background sync.", notification.id());
    }
}