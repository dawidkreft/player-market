package com.player.market.transfer.player.mapper;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.entity.Player;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.player.market.transfer.factory.PlayerFactory.getPlayerEntity;

@RunWith(MockitoJUnitRunner.class)
public class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @Before
    public void setUp() {
        playerMapper = new PlayerMapper();
    }

    @Test
    public void shouldMapToDto() {
        Player player = getPlayerEntity();

        PlayerDto mappedPlayer = playerMapper.mapToDto(player);

        Assertions.assertThat(mappedPlayer.getId()).isEqualTo(player.getId());
        Assertions.assertThat(mappedPlayer.getName()).isEqualTo(player.getName());
    }

    @Test
    public void shouldMapToDtoList() {
        Player player = getPlayerEntity();

        List<PlayerDto> mappedPlayers = playerMapper.mapToDto(Arrays.asList(player));

        Assertions.assertThat(mappedPlayers.get(0).getId()).isEqualTo(player.getId());
        Assertions.assertThat(mappedPlayers.get(0).getName()).isEqualTo(player.getName());
    }
}
