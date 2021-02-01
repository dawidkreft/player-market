package com.player.market.transfer.factory;

import com.player.market.transfer.dto.CurrencyEnum;
import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.team.entity.Currency;
import com.player.market.transfer.team.entity.Team;

import java.time.Instant;
import java.util.List;

public class TeamFactory {

    public static Team getTeamEntityForSave(Player owner, List<FootballPlayer> players) {
        return Team.builder().creationDate(Instant.now()).currency(Currency.EUR).name("name").owner(owner).players(players).build();
    }

    public static TeamDto getTeamDto(PlayerDto owner, List<FootballPlayerDto> players) {
        return new TeamDto().name("name").owner(owner).players(players).currency(CurrencyEnum.EUR);
    }
}
