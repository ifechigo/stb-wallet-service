package com.suntrustbank.wallet.providers.dtos;

import com.suntrustbank.wallet.providers.dtos.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalletCreationRequest {
    @NotBlank(message = "first name must not be blank")
    private String firstName;

    @NotBlank(message = "last name must not be blank")
    private String lastName;

    @NotBlank(message = "Merchant must not be blank")
    private String merchantId;

    private String currency = Currency.NGN.name();
}
