package com.transfer.api.service;

import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.service.integration.client.response.ClientResponse;
import com.transfer.api.service.integration.transfer.TransferClient;
import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.integration.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.transfer.api.util.CheckTransfer.checkTransfer;
import static com.transfer.api.util.CheckTransfer.isaBoolean;

@Service
public class RolesForValidateTransferImpl implements RolesForValidateTransfer {

    @Autowired
    private Client client;

    @Autowired
    private Account account;

    @Autowired
    private TransferClient transferClient;

    @Autowired
    private NotificationBacen notificationBacen;

    public TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO) {

        final ClientResponse clientResponse = this.client.validateIfTheClientExists(transferRequestDTO.getIdCliente());

        if (clientResponse.getId() == null) {
            return TransferResponseDTO.builder().build();
        }

        final AccountOriginResponse accountOrigin =
                this.account.searchSourceAccountData(transferRequestDTO.getConta().getIdOrigem());

        final boolean checkTransfer = checkTransfer(accountOrigin.getLimiteDiario(), transferRequestDTO.getValor());

        if (checkTransfer) {
            return TransferResponseDTO.builder().limiteDiario(accountOrigin.getLimiteDiario()).build();
        }

        String idTransferFinal = "";

        if (isaBoolean(transferRequestDTO, accountOrigin)) {

            idTransferFinal = this.transferClient.transferSend(transferRequestDTO);

            this.notificationBacen.notificationBacen(NotificationBacenRequest.builder()
                    .valor(transferRequestDTO.getValor())
                    .conta(transferRequestDTO.getConta())
                    .build());
        }
        return TransferResponseDTO.builder()
                .limiteDiario(accountOrigin.getLimiteDiario())
                .idTransferencia(idTransferFinal)
                .build();
    }
}
