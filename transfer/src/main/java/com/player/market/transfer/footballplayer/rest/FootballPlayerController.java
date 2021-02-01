package com.player.market.transfer.footballplayer.rest;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.TransferFeeDto;
import com.player.market.transfer.footballplayer.service.FootballPlayerService;
import com.player.market.transfer.rest.FootballPlayersApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
public class FootballPlayerController implements FootballPlayersApi {

    private final FootballPlayerService playerService;

    @Autowired
    public FootballPlayerController(FootballPlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public ResponseEntity<FootballPlayerDto> getFootballPlayer(UUID id) {
        return new ResponseEntity<>(playerService.getPlayer(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TransferFeeDto> getFootballPlayerTransferFee(UUID id, UUID teamId) {
        return new ResponseEntity<>(playerService.calculateTransfer(id, teamId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<FootballPlayerDto>> getFootballPlayers(@Min(1) @Max(50) @Valid Integer sizeOnPage, @Min(0) @Valid Integer page) {
        return new ResponseEntity<>(playerService.getPlayers(sizeOnPage, page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removeFootballPlayer(UUID id) {
        playerService.removePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FootballPlayerDto> saveFootballPlayer(@Valid FootballPlayerDto footballPlayerDto) {
        return new ResponseEntity<>(playerService.save(footballPlayerDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FootballPlayerDto> updateFootballPlayer(@Valid FootballPlayerDto footballPlayerDto) {
        return new ResponseEntity<>(playerService.update(footballPlayerDto), HttpStatus.OK);
    }
}
