package com.transfer.api.transferapi.integration.account;

import com.transfer.api.transferapi.integration.account.response.AccountOriginResponse;

@FunctionalInterface
public interface Account {
    AccountOriginResponse searchSourceAccountData(final String idAccountOrigin);
}
