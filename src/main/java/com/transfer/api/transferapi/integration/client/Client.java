package com.transfer.api.transferapi.integration.client;

import com.transfer.api.transferapi.integration.client.response.ClientResponse;

@FunctionalInterface
public interface Client {
    ClientResponse validateIfTheClientExists(final String idClient);
}
