package com.player.market.transfer.team.service;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.service.PlayerService;
import com.player.market.transfer.team.entity.Team;
import com.player.market.transfer.team.exception.TeamNotFoundException;
import com.player.market.transfer.team.mapper.TeamMapper;
import com.player.market.transfer.team.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.player.market.transfer.factory.PlayerFactory.getPlayerEntityWithId;
import static com.player.market.transfer.factory.PlayerFactory.getTestPlayerDtoForSave;
import static com.player.market.transfer.factory.TeamFactory.getTeamDto;
import static com.player.market.transfer.factory.TeamFactory.getTeamEntityForSave;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    private PlayerService playerService;
    private FootballPlayerRepository footballPlayerRepository;
    private TeamRepository teamRepository;
    private TeamMapper teamMapper;
    private TeamService teamService;

    @Before
    public void setUp() {
        teamMapper = new TeamMapper();
        teamRepository = Mockito.mock(TeamRepository.class);
        footballPlayerRepository = Mockito.mock(FootballPlayerRepository.class);
        playerService = Mockito.mock(PlayerService.class);
        teamService = new TeamService(playerService, footballPlayerRepository, teamRepository, teamMapper);
    }

    @Test
    public void shouldSave() {
        PlayerDto testPlayerDtoForSave = getTestPlayerDtoForSave();
        testPlayerDtoForSave.setId(UUID.randomUUID());
        TeamDto teamDto = getTeamDto(testPlayerDtoForSave, Lists.emptyList());
        Player owner = Player.builder()
                .name(testPlayerDtoForSave.getName())
                .id(testPlayerDtoForSave.getId())
                .creationDate(Instant.now())
                .build();
        Team savedTeam = getTeamEntityForSave(owner, Lists.emptyList());
        savedTeam.setId(UUID.randomUUID());
        when(playerService.getPlayer(Mockito.any())).thenReturn(owner);
        when(teamRepository.save(Mockito.any())).thenReturn(savedTeam);

        TeamDto savedTeamDto = teamService.saveTeam(testPlayerDtoForSave.getId(), teamDto);

        Assertions.assertThat(savedTeamDto.getId()).isNotNull();
        Assertions.assertThat(savedTeamDto.getName()).isEqualTo(teamDto.getName());
    }

    @Test
    public void shouldReturnListOfPlayers() {
        Player player = getPlayerEntityWithId();
        when(teamRepository.findAllByOwnerId(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(getTeamEntityForSave(player, Lists.emptyList()), getTeamEntityForSave(player, Lists.emptyList())));

        List<TeamDto> teams = teamService.getTeams(player.getId(), 10, 0);

        Assertions.assertThat(teams.size()).isEqualTo(2);
        Assertions.assertThat(teams.get(0)).isNotNull();
        Assertions.assertThat(teams.get(1)).isNotNull();
    }

    @Test(expected = TeamNotFoundException.class)
    public void shouldThrowDuringDeleteNotExistsTeam() {

        teamService.remove(UUID.randomUUID());
    }

    @Test(expected = TeamNotFoundException.class)
    public void shouldReturnExceptionWhenTeamNotExists() {
        when(teamRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        teamService.getTeam(UUID.randomUUID());
    }
}
