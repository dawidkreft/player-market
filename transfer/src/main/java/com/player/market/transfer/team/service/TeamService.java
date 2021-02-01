package com.player.market.transfer.team.service;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.service.PlayerService;
import com.player.market.transfer.team.entity.Currency;
import com.player.market.transfer.team.entity.Team;
import com.player.market.transfer.team.exception.TeamNotFoundException;
import com.player.market.transfer.team.mapper.TeamMapper;
import com.player.market.transfer.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class TeamService {

    private final PlayerService playerService;
    private final FootballPlayerRepository footballPlayerRepository;
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public List<TeamDto> getTeams(UUID ownerId, Integer sizeOnPage, Integer page) {
        Pageable pageable = PageRequest.of(page, sizeOnPage);
        return teamMapper.mapToDto(teamRepository.findAllByOwnerId(ownerId, pageable));
    }

    public TeamDto getTeam(UUID teamId) {
        return teamMapper.mapToDto(findOrThrow(teamId));
    }

    public Team findByPlayerId(UUID playerId) {
        Optional<Team> result = teamRepository.findByPlayersId(playerId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new TeamNotFoundException("Team not found", "code");
    }

    public void remove(UUID id) {
        findOrThrow(id);
        log.info("Removing Team with the following id:" + id.toString());
        teamRepository.deleteById(id);
    }

    private Team findOrThrow(UUID id) {
        Optional<Team> result = teamRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new TeamNotFoundException("Team not found", "code");
    }

    public TeamDto saveTeam(UUID ownerId, TeamDto teamDto) {
        Player player = playerService.getPlayer(ownerId);
        Team team = Team.builder()
                .name(teamDto.getName())
                .creationDate(Instant.now())
                .owner(player)
                .currency(Currency.valueOf(teamDto.getCurrency().toString()))
                .players(collectFootballPlayer(teamDto.getPlayers())).build();
        team = teamRepository.save(team);
        return teamMapper.mapToDto(team);
    }

    private List<FootballPlayer> collectFootballPlayer(List<FootballPlayerDto> players) {
        List<FootballPlayer> result = new ArrayList<>();
        players.forEach(it -> result.add(footballPlayerRepository.findById(it.getId()).get()));
        return result;
    }
}
