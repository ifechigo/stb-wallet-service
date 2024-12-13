package com.suntrustbank.wallet.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseMessage {

    SUCCESSFUL("success"),
    FAILED("failed");

    private final String value;
}
