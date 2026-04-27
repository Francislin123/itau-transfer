package com.transfer.api.service;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.service.integration.client.Client;
import com.transfer.api.service.integration.client.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.transfer.api.util.CheckTransfer.checkTransfer;
import static com.transfer.api.util.CheckTransfer.isNotificationRequired;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolesForValidateTransferImpl implements RolesForValidateTransfer {

    private final Client client;
    private final Account account;
    private final NotificationBacen notificationBacen;

    @Override
    public TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO) {
        log.info("Starting orchestrated transfer validation for client: {}", transferRequestDTO.idCliente());

        // 0. Early Validation: Invalid Amount
        if (transferRequestDTO.valor() <= 0) {
            log.warn("Transfer denied: Invalid amount {}", transferRequestDTO.valor());
            return TransferResponseDTO.builder()
                    .msg("Invalid transfer amount")
                    .build();
        }

        List<String> accountIds = List.of(
                transferRequestDTO.conta().idOrigem(),
                transferRequestDTO.conta().idDestino()
        );

        try {
            // 🚀 PARALLEL DISPATCH
            var clientFuture = CompletableFuture.supplyAsync(() ->
                    this.client.validateIfTheClientExists(transferRequestDTO.idCliente()));

            List<AccountOriginResponse> accounts = searchAllAccountsData(accountIds);
            ClientResponse clientResponse = clientFuture.get(7, TimeUnit.SECONDS);

            // 1. Client Validation
            if (clientResponse.id() == null) {
                log.warn("Transfer denied: Client record not found.");
                return TransferResponseDTO.builder()
                        .msg("Client not found")
                        .build();
            }

            // 2. Account Extraction
            AccountOriginResponse sourceAccount = accounts.stream()
                    .filter(a -> Objects.equals(a.id(), transferRequestDTO.conta().idOrigem()))
                    .findFirst()
                    .orElse(null);

            AccountOriginResponse destAccount = accounts.stream()
                    .filter(a -> Objects.equals(a.id(), transferRequestDTO.conta().idDestino()))
                    .findFirst()
                    .orElse(null);

            // 3. Resource Existence Validation
            if (sourceAccount == null || destAccount == null || sourceAccount.id() == null) {
                log.warn("Transfer denied: One or both accounts not found.");
                return TransferResponseDTO.builder()
                        .msg("Account not found")
                        .build();
            }

            // 4. Business Rule Validation: Daily Limit
            if (checkTransfer(sourceAccount.limiteDiario(), transferRequestDTO.valor())) {
                log.warn("Transfer denied: Insufficient daily limit for account {}", sourceAccount.id());
                return TransferResponseDTO.builder()
                        .limiteDiario(sourceAccount.limiteDiario())
                        .msg("Daily limit exceeded")
                        .build();
            }

            // 5. Async Notification
            if (isNotificationRequired(transferRequestDTO, sourceAccount)) {
                CompletableFuture.runAsync(() ->
                        this.notificationBacen.notificationBacen(NotificationBacenRequest.builder()
                                .valor(transferRequestDTO.valor())
                                .conta(transferRequestDTO.conta())
                                .build())
                ).exceptionally(ex -> {
                    log.error("Failed to notify Bacen: {}", ex.getMessage());
                    return null;
                });
            }

            // 6. Success
            return TransferResponseDTO.builder()
                    .limiteDiario(sourceAccount.limiteDiario())
                    .idTransferencia(UUID.randomUUID().toString())
                    .msg("Transfer completed successfully.")
                    .build();

        } catch (Exception e) {
            log.error("Orchestration error: {}", e.getMessage());
            throw new RuntimeException("Orchestration failed", e);
        }
    }

    private List<AccountOriginResponse> searchAllAccountsData(List<String> accountIds) {
        log.info("Initiating parallel lookup for {} accounts", accountIds.size());

        List<CompletableFuture<AccountOriginResponse>> futures = accountIds.stream()
                .map(accountId -> CompletableFuture.supplyAsync(() ->
                        this.account.searchSourceAccountData(accountId)))
                .toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(10, TimeUnit.SECONDS);
            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } catch (Exception e) {
            log.error("Error during parallel account lookup: {}", e.getMessage());
            throw new RuntimeException("Parallel account lookup failed", e);
        }
    }
}