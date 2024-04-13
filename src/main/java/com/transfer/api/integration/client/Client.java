package com.transfer.api.integration.client;

import com.transfer.api.integration.client.response.ClientResponse;

@FunctionalInterface
public interface Client {
    ClientResponse validateIfTheClientExists(final String idClient);
}
