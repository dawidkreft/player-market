package com.player.market.transfer.player.mapper;

import com.player.market.transfer.dto.PlayerDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    public PlayerDto mapToDto(com.player.market.transfer.player.entity.Player player) {
        return new PlayerDto().id(player.getId()).name(player.getName());
    }

    public List<PlayerDto> mapToDto(List<com.player.market.transfer.player.entity.Player> players) {
        List<PlayerDto> result = new ArrayList<PlayerDto>();
        players.forEach(it -> result.add(mapToDto(it)));
        return result;
    }
}
