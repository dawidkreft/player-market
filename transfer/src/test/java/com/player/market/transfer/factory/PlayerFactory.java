package com.player.market.transfer.factory;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.entity.Player;

import java.time.Instant;
import java.util.UUID;

public class PlayerFactory {

    public static Player getPlayerEntity() {
        return new Player(null, "test", Instant.now());
    }

    public static PlayerDto getTestPlayerDtoForSave() {
        return new PlayerDto().id(null).name("name");
    }

    public static Player getPlayerEntityWithId() {
        Player playerEntity = getPlayerEntity();
        playerEntity.setId(UUID.randomUUID());
        return playerEntity;
    }

}
