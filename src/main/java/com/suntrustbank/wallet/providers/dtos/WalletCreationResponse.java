package com.suntrustbank.wallet.providers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class WalletCreationResponse {
    private String firstName;

    private String lastName;

    private String merchantId;

    private String walletId;

    private String currency;

    private LocalDateTime createdAt;
}
