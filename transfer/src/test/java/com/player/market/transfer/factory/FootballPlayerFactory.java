package com.player.market.transfer.factory;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

public class FootballPlayerFactory {

    public static FootballPlayer getFootballPlayerEntity() {
        return new FootballPlayer(null, "name", 2015, Instant.now().minusSeconds(100000), Instant.now());
    }

    public static FootballPlayerDto getTestFootballPlayerDtoForSave() {
        return new FootballPlayerDto().id(null).name("name").yearOfBirth(2015).dateOfStartCareer(OffsetDateTime.now().minusMonths(5));
    }

    public static FootballPlayer getFootballPlayerEntityWithId() {
        FootballPlayer playerEntity = getFootballPlayerEntity();
        playerEntity.setId(UUID.randomUUID());
        return playerEntity;
    }
}
