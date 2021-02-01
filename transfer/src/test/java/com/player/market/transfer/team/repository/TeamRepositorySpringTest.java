package com.player.market.transfer.team.repository;

import com.player.market.transfer.factory.FootballPlayerFactory;
import com.player.market.transfer.factory.PlayerFactory;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.repository.PlayerRepository;
import com.player.market.transfer.team.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.player.market.transfer.factory.TeamFactory.getTeamEntityForSave;
import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeamRepositorySpringTest {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    FootballPlayerRepository footballPlayerRepository;

    @Test
    public void shouldSave() {
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        Team teamEntityForSave = getTeamEntityForSave(owner, Collections.emptyList());

        Team team = teamRepository.save(teamEntityForSave);

        Assertions.assertNotNull(team.getId());
        assertEquals(team.getOwner(), owner);
        assertEquals(team.getName(), teamEntityForSave.getName());
        assertEquals(team.getCurrency(), teamEntityForSave.getCurrency());
    }

    @Test
    public void shouldSaveWithFootballPlayers() {
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        FootballPlayer player1 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        FootballPlayer player2 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        Team teamEntityForSave = getTeamEntityForSave(owner, Arrays.asList(player1, player2));

        Team team = teamRepository.save(teamEntityForSave);

        Assertions.assertNotNull(team.getId());
        assertEquals(team.getOwner(), owner);
        assertEquals(team.getName(), teamEntityForSave.getName());
        assertEquals(team.getCurrency(), teamEntityForSave.getCurrency());
        assertEquals(2, team.getPlayers().size());
    }

    @Test
    public void shouldFindByOwnerId() {
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        FootballPlayer player1 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        FootballPlayer player2 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        Team teamEntityForSave = getTeamEntityForSave(owner, Arrays.asList(player1, player2));
        Team team = teamRepository.save(teamEntityForSave);

        List<Team> teams = teamRepository.findAllByOwnerId(owner.getId(), PageRequest.of(0, 10));

        assertEquals(team.getName(), teams.get(0).getName());
        assertEquals(1, teams.size());
    }
}
