package com.transfer.api.integration.account;

import com.transfer.api.integration.account.response.AccountOriginResponse;

@FunctionalInterface
public interface Account {
    AccountOriginResponse searchSourceAccountData(final String idAccountOrigin);
}
