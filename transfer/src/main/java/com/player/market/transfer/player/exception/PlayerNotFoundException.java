package com.player.market.transfer.player.exception;

import com.player.market.transfer.common.TransferBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends TransferBaseException {
    public PlayerNotFoundException(String message, String code) {
        super(message, code);
    }
}
