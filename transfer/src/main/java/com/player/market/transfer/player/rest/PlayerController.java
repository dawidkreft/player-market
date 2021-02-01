package com.player.market.transfer.player.rest;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.service.PlayerService;
import com.player.market.transfer.rest.PlayersApi;
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
public class PlayerController implements PlayersApi {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public ResponseEntity<PlayerDto> getPlayer(UUID id) {
        return new ResponseEntity<>(playerService.getPlayerDto(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PlayerDto>> getPlayers(@Min(1) @Max(50) @Valid Integer sizeOnPage, @Min(0) @Valid Integer page) {
        return new ResponseEntity<>(playerService.getPlayers(sizeOnPage, page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removePlayer(UUID id) {
        playerService.removePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PlayerDto> savePlayer(@Valid PlayerDto player) {
        return new ResponseEntity<>(playerService.save(player), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PlayerDto> updatePlayer(@Valid PlayerDto player) {
        return new ResponseEntity<>(playerService.update(player), HttpStatus.OK);
    }
}
