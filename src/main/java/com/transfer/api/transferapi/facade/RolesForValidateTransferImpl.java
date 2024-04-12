package com.transfer.api.transferapi.facade;

import com.transfer.api.transferapi.controller.request.TransferRequestDTO;
import com.transfer.api.transferapi.controller.response.TransferResponseDTO;
import com.transfer.api.transferapi.integration.account.Account;
import com.transfer.api.transferapi.integration.account.response.AccountOriginResponse;
import com.transfer.api.transferapi.integration.balance.NotificationBacen;
import com.transfer.api.transferapi.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.transferapi.integration.client.Client;
import com.transfer.api.transferapi.integration.client.response.ClientResponse;
import com.transfer.api.transferapi.integration.transfer.TransferClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.transfer.api.transferapi.util.CheckTransfer.checkTransfer;
import static com.transfer.api.transferapi.util.CheckTransfer.isaBoolean;

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

        final AccountOriginResponse accountOriginResponse =
                this.account.searchSourceAccountData(transferRequestDTO.getConta().getIdOrigem());

        final boolean checkTransfer = checkTransfer(
                accountOriginResponse.getLimiteDiario(), transferRequestDTO.getValor());

        if (isaBoolean(transferRequestDTO, checkTransfer, accountOriginResponse)) {

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
