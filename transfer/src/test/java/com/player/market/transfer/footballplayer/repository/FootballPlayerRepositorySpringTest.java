package com.player.market.transfer.footballplayer.repository;

import com.player.market.transfer.factory.FootballPlayerFactory;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FootballPlayerRepositorySpringTest {

    @Autowired
    FootballPlayerRepository footballPlayerRepository;

    @Test
    public void shouldSaveAndFindById() {
        FootballPlayer testPlayer = FootballPlayerFactory.getFootballPlayerEntity();

        FootballPlayer savedPlayer = footballPlayerRepository.save(testPlayer);
        footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());

        Optional<FootballPlayer> player = footballPlayerRepository.findById(savedPlayer.getId());
        List<FootballPlayer> allPlayers = footballPlayerRepository.findAll();

        assertEquals(testPlayer.getId(), player.get().getId());
        assertEquals(testPlayer.getName(), player.get().getName());
        assertEquals(2, allPlayers.size());
    }
}
