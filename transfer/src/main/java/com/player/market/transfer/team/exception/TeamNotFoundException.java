package com.player.market.transfer.team.exception;

import com.player.market.transfer.common.TransferBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TeamNotFoundException extends TransferBaseException {
    public TeamNotFoundException(String message, String code) {
        super(message, code);
    }
}
