package com.player.market.transfer.player.repository;

import com.player.market.transfer.factory.PlayerFactory;
import com.player.market.transfer.player.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerRepositorySpringTest {

    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void shouldSaveAndFindById() {
        Player testPlayer = PlayerFactory.getPlayerEntity();

        Player savedPlayer = playerRepository.save(testPlayer);
        playerRepository.save(PlayerFactory.getPlayerEntity());

        Optional<Player> Player = playerRepository.findById(savedPlayer.getId());
        List<Player> allPlayers = playerRepository.findAll();

        assertEquals(testPlayer.getId(), Player.get().getId());
        assertEquals(testPlayer.getName(), Player.get().getName());
        assertEquals(2, allPlayers.size());
    }
}
