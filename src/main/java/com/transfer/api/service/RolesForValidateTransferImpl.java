package com.transfer.api.service;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.service.integration.client.Client;
import com.transfer.api.service.integration.client.response.ClientResponse;
import com.transfer.api.service.integration.transfer.TransferClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.transfer.api.util.CheckTransfer.checkTransfer;
import static com.transfer.api.util.CheckTransfer.isaBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolesForValidateTransferImpl implements RolesForValidateTransfer {

    private final Client client;
    private final Account account;
    private final TransferClient transferClient;
    private final NotificationBacen notificationBacen;

    private static final String TRANSFER_SERVICE = "transferOrchestrator";

    @Override
    public TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO) {

        log.info("Starting orchestrated transfer validation for client: {}", transferRequestDTO.idCliente());

        try {
            // 🚀 PARALLEL EXECUTION: Running Client and Account checks at the same time
            var clientFuture = CompletableFuture.supplyAsync(() ->
                    this.client.validateIfTheClientExists(transferRequestDTO.idCliente()));

            var accountFuture = CompletableFuture.supplyAsync(() ->
                    this.account.searchSourceAccountData(transferRequestDTO.conta().originId()));

            // Wait for both with a global timeout
            CompletableFuture.allOf(clientFuture, accountFuture).get(7, TimeUnit.SECONDS);

            ClientResponse clientResponse = clientFuture.join();
            AccountOriginResponse accountOrigin = accountFuture.join();

            // 1. Validation Logic
            if (clientResponse.id() == null) {
                log.warn("Transfer denied: Client record not found.");
                return TransferResponseDTO.builder().msg("Client not found").build();
            }

            if (checkTransfer(accountOrigin.limiteDiario(), transferRequestDTO.valor())) {
                log.warn("Transfer denied: Insufficient daily limit.");
                return TransferResponseDTO.builder()
                        .limiteDiario(accountOrigin.limiteDiario())
                        .msg("Daily limit exceeded")
                        .build();
            }

            // 2. Execution
            String idTransferFinal = this.transferClient.transferSend(transferRequestDTO);

            // 3. Post-processing (Asynchronous notification)
            if (isaBoolean(transferRequestDTO, accountOrigin)) {
                this.notificationBacen.notificationBacen(NotificationBacenRequest.builder()
                        .valor(transferRequestDTO.valor())
                        .conta(transferRequestDTO.conta())
                        .build());
            }

            return TransferResponseDTO.builder()
                    .limiteDiario(accountOrigin.limiteDiario())
                    .idTransferencia(idTransferFinal)
                    .build();

        } catch (Exception e) {
            log.error("Orchestration error: {}", e.getMessage());
            // Rethrowing so the Circuit Breaker can track the failure
            throw new RuntimeException("Orchestration failed", e);
        }
    }
}