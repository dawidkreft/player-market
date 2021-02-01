package com.player.market.transfer.team.mapper;

import com.player.market.transfer.dto.CurrencyEnum;
import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.footballplayer.mapper.FootballPlayerMapper;
import com.player.market.transfer.team.entity.Team;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamMapper {
    public List<TeamDto> mapToDto(List<Team> teams) {
        return teams.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public TeamDto mapToDto(Team team) {
        return new TeamDto()
                .id(team.getId())
                .name(team.getName())
                .currency(CurrencyEnum.valueOf(team.getCurrency().toString()))
                .owner(new PlayerDto()
                        .name(team.getOwner().getName())
                        .id(team.getOwner().getId()))
                .players(team.getPlayers().stream().map(FootballPlayerMapper::mapToDto).collect(Collectors.toList()));
    }
}
