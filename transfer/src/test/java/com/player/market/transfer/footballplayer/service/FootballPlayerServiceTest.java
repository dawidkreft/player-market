package com.player.market.transfer.footballplayer.service;

import com.player.market.transfer.dto.CurrencyEnum;
import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.dto.TransferFeeDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.exception.FootBallPlayerInCorrectYearOfBirthException;
import com.player.market.transfer.footballplayer.exception.FootBallPlayerInStartOfExperienceDateException;
import com.player.market.transfer.footballplayer.exception.FootballPlayerNotFoundException;
import com.player.market.transfer.footballplayer.mapper.FootballPlayerMapper;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.team.entity.Team;
import com.player.market.transfer.team.service.TeamService;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.player.market.transfer.factory.FootballPlayerFactory.getFootballPlayerEntity;
import static com.player.market.transfer.factory.FootballPlayerFactory.getFootballPlayerEntityWithId;
import static com.player.market.transfer.factory.FootballPlayerFactory.getTestFootballPlayerDtoForSave;
import static com.player.market.transfer.factory.TeamFactory.getTeamEntityForSave;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FootballPlayerServiceTest {

    private FootballPlayerService playerService;
    private FootballPlayerRepository playerRepository;
    private FootballPlayerMapper playerMapper;
    private TeamService teamService;

    @Before
    public void setUp() {
        playerMapper = new FootballPlayerMapper();
        playerRepository = Mockito.mock(FootballPlayerRepository.class);
        teamService = Mockito.mock(TeamService.class);
        playerService = new FootballPlayerService(playerRepository, playerMapper, teamService);
    }

    @Test
    public void shouldSave() {
        FootballPlayerDto testPlayer = getTestFootballPlayerDtoForSave();
        when(playerRepository.save(any())).thenReturn(new FootballPlayer(UUID.randomUUID(), testPlayer.getName(), testPlayer.getYearOfBirth(), Instant.now(), Instant.now()));

        FootballPlayerDto savedPlayer = playerService.save(testPlayer);

        Assertions.assertThat(savedPlayer.getId()).isNotNull();
        Assertions.assertThat(savedPlayer.getName()).isEqualTo(testPlayer.getName());
    }

    @Test(expected = FootBallPlayerInCorrectYearOfBirthException.class)
    public void shouldReturnExceptionWhenSavePlayerWithBadAge() {
        FootballPlayerDto testPlayer = getTestFootballPlayerDtoForSave();
        testPlayer.setYearOfBirth(LocalDate.now().getYear());

        playerService.save(testPlayer);
    }

    @Test(expected = FootBallPlayerInStartOfExperienceDateException.class)
    public void shouldReturnExceptionWhenSavePlayerWithBadExperience() {
        FootballPlayerDto testPlayer = getTestFootballPlayerDtoForSave();
        testPlayer.setDateOfStartCareer(OffsetDateTime.MAX);

        playerService.save(testPlayer);
    }

    @Test
    public void shouldFindById() {
        FootballPlayer footballPlayerEntity = getFootballPlayerEntityWithId();
        when(playerRepository.findById(any())).thenReturn(Optional.of(footballPlayerEntity));

        FootballPlayerDto savedPlayer = playerService.getPlayer(footballPlayerEntity.getId());

        Assertions.assertThat(savedPlayer.getId()).isNotNull();
        Assertions.assertThat(savedPlayer.getName()).isEqualTo(footballPlayerEntity.getName());
    }

    @Test(expected = FootballPlayerNotFoundException.class)
    public void shouldReturnExceptionWhenPlayerNotExists() {
        when(playerRepository.findById(any())).thenReturn(Optional.empty());

        playerService.getPlayer(UUID.randomUUID());
    }

    @Test
    public void shouldReturnListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(Arrays.asList(getFootballPlayerEntityWithId(), getFootballPlayerEntityWithId()));

        List<FootballPlayerDto> players = playerService.getPlayers(10, 0);

        Assertions.assertThat(players.size()).isEqualTo(2);
        Assertions.assertThat(players.get(0)).isNotNull();
        Assertions.assertThat(players.get(1)).isNotNull();
    }

    @Test
    public void shouldCalculateContractFee() {
        FootballPlayer player = getFootballPlayerEntity();
        player.setStartOfCareerDate(Instant.now().minusSeconds(15 * 30 * 60 * 60 * 24));
        TeamDto teamDest = new TeamDto();
        teamDest.id(UUID.randomUUID());
        teamDest.setCurrency(CurrencyEnum.EUR);
        Team sourceTeam = getTeamEntityForSave(new Player(), Lists.emptyList());

        when(playerRepository.findById(any())).thenReturn(Optional.of(player));
        when(teamService.findByPlayerId(any())).thenReturn(sourceTeam);
        when(teamService.getTeam(any())).thenReturn(teamDest);
        TransferFeeDto transferFeeDto = playerService.calculateTransfer(player.getId(), teamDest.getId());

        Assertions.assertThat(transferFeeDto).isNotNull();
        Assertions.assertThat(transferFeeDto.getContractFee()).isGreaterThan(0.0);
        Assertions.assertThat(transferFeeDto.getTransferFee()).isGreaterThan(0.0);
        Assertions.assertThat(transferFeeDto.getFootballPlayer().getId()).isEqualTo(player.getId());

    }

    @Test(expected = FootballPlayerNotFoundException.class)
    public void shouldThrowDuringDeleteNotExistsPlayer() {

        playerService.removePlayer(UUID.randomUUID());
    }
}
