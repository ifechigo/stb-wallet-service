package com.suntrustbank.wallet.providers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class WalletBalanceResponse {
    private String walletId;
    private String currency;
    private BigDecimal balance;
}
