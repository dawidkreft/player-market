package com.player.market.transfer.common;

import com.player.market.transfer.dto.Error;
import lombok.Getter;

@Getter
public class TransferBaseException extends RuntimeException {

    private final com.player.market.transfer.dto.Error error;

    public TransferBaseException(String message, String code) {
        this.error = new Error().message(message).code(code);
    }
}
