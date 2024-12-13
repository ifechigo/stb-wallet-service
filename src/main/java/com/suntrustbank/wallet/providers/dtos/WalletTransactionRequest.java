package com.suntrustbank.wallet.providers.dtos;

import com.suntrustbank.wallet.providers.dtos.enums.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class WalletTransactionRequest {
    @NotBlank(message = "transaction reference  must not be blank")
    private String transactionReference;

    @NotBlank(message = "walletId must not be blank")
    @Pattern(regexp = "\\d{10}", message = "walletId must be exactly 10 digits")
    private String walletId;

    @NotNull(message = "amount must not be null")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0.00")
    private BigDecimal amount;

    private String currency = Currency.NGN.name();
}
