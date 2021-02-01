package com.player.market.transfer.footballplayer.mapper;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.player.market.transfer.factory.FootballPlayerFactory.getFootballPlayerEntity;

@RunWith(MockitoJUnitRunner.class)
public class FootballPlayerMapperTest {


    private FootballPlayerMapper playerMapper;

    @Before
    public void setUp() {
        playerMapper = new FootballPlayerMapper();
    }

    @Test
    public void shouldMapToDto() {
        FootballPlayer player = getFootballPlayerEntity();

        FootballPlayerDto mappedPlayer = playerMapper.mapToDto(player);

        Assertions.assertThat(mappedPlayer.getId()).isEqualTo(player.getId());
        Assertions.assertThat(mappedPlayer.getName()).isEqualTo(player.getName());
    }

    @Test
    public void shouldMapToDtoList() {
        FootballPlayer player = getFootballPlayerEntity();

        List<FootballPlayerDto> mappedPlayers = playerMapper.mapToDto(Arrays.asList(player));

        Assertions.assertThat(mappedPlayers.get(0).getId()).isEqualTo(player.getId());
        Assertions.assertThat(mappedPlayers.get(0).getName()).isEqualTo(player.getName());
    }
}
