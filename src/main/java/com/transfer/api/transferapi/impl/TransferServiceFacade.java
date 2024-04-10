package com.transfer.api.transferapi.impl;

import com.transfer.api.transferapi.integration.account.Account;
import com.transfer.api.transferapi.integration.account.response.AccountOriginResponse;
import com.transfer.api.transferapi.integration.balance.NotificationBacen;
import com.transfer.api.transferapi.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.transferapi.integration.client.Client;
import com.transfer.api.transferapi.integration.client.response.ClientResponse;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.integration.transfer.TransferClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.transfer.api.transferapi.util.CheckTransfer.checkTransfer;

@Service
@Slf4j
public class TransferServiceFacade implements TransferFacade {

    @Autowired
    private Client client;

    @Autowired
    private Account account;

    @Autowired
    private TransferClient transferClient;

    @Autowired
    private NotificationBacen notificationBacen;

    @Override
    public TransferResponseDTO makeTransfer(final TransferRequestDTO transferRequestDTO) {

        log.info("init to makeTransfer method");

        final ClientResponse clientResponse = this.client.validateIfTheClientExists(transferRequestDTO.getIdCliente());

        if (clientResponse.getId() == null) {
            return TransferResponseDTO.builder().build();
        }

        final AccountOriginResponse accountOriginResponse =
                this.account.searchSourceAccountData(transferRequestDTO.getConta().getIdOrigem());

        if (accountOriginResponse.getId() == null) {
            return TransferResponseDTO.builder().build();
        }

        final boolean checkTransfer =
                checkTransfer(accountOriginResponse.getLimiteDiario(), transferRequestDTO.getValor());

        if (checkTransfer
                && accountOriginResponse.isAtivo()
                && accountOriginResponse.getSaldo() > transferRequestDTO.getValor()) {

            String idTransfer = this.transferClient.transferClientSend(transferRequestDTO);

            this.notificationBacen.notificationBacen(NotificationBacenRequest.builder()
                    .valor(transferRequestDTO.getValor())
                    .conta(transferRequestDTO.getConta())
                    .build());

            return TransferResponseDTO.builder().idTransferencia(idTransfer).build();

        } else {
            return TransferResponseDTO.builder().valor(accountOriginResponse.getLimiteDiario()).build();
        }
    }
}