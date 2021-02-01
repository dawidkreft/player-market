package com.player.market.transfer.player.service;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.exception.PlayerNotFoundException;
import com.player.market.transfer.player.mapper.PlayerMapper;
import com.player.market.transfer.player.repository.PlayerRepository;
import org.assertj.core.api.Assertions;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    private PlayerService playerService;
    private PlayerRepository playerRepository;
    private PlayerMapper playerMapper;

    @Before
    public void setUp() {
        playerMapper = new PlayerMapper();
        playerRepository = Mockito.mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepository, playerMapper);
    }

    @Test
    public void shouldSave() {
        PlayerDto testPlayerDtoForSave = getTestPlayerDtoForSave();
        when(playerRepository.save(Mockito.any())).thenReturn(new com.player.market.transfer.player.entity.Player(UUID.randomUUID(), getTestPlayerDtoForSave().getName(), Instant.now()));

        PlayerDto savedPlayer = playerService.save(testPlayerDtoForSave);

        Assertions.assertThat(savedPlayer.getId()).isNotNull();
        Assertions.assertThat(savedPlayer.getName()).isEqualTo(testPlayerDtoForSave.getName());
    }

    @Test
    public void shouldFindById() {
        Player testPlayer = getPlayerEntityWithId();
        when(playerRepository.findById(Mockito.any())).thenReturn(Optional.of(testPlayer));

        PlayerDto savedPlayer = playerService.getPlayerDto(testPlayer.getId());

        Assertions.assertThat(savedPlayer.getId()).isNotNull();
        Assertions.assertThat(savedPlayer.getName()).isEqualTo(testPlayer.getName());
    }

    @Test(expected = PlayerNotFoundException.class)
    public void shouldReturnExceptionWhenPlayerNotExists() {
        when(playerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        playerService.getPlayer(UUID.randomUUID());
    }

    @Test
    public void shouldReturnListOfPlayers() {
        when(playerRepository.findAll()).thenReturn(Arrays.asList(getPlayerEntityWithId(), getPlayerEntityWithId()));

        List<PlayerDto> players = playerService.getPlayers(10, 0);

        Assertions.assertThat(players.size()).isEqualTo(2);
        Assertions.assertThat(players.get(0)).isNotNull();
        Assertions.assertThat(players.get(1)).isNotNull();
    }

    @Test(expected = PlayerNotFoundException.class)
    public void shouldThrowDuringDeleteNotExistsPlayer() {

        playerService.removePlayer(UUID.randomUUID());
    }
}
