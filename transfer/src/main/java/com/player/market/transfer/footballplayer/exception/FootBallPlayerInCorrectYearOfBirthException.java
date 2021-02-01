package com.player.market.transfer.footballplayer.exception;

import com.player.market.transfer.common.TransferBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FootBallPlayerInCorrectYearOfBirthException extends TransferBaseException {
    public FootBallPlayerInCorrectYearOfBirthException(String message, String code) {
        super(message, code);
    }
}
