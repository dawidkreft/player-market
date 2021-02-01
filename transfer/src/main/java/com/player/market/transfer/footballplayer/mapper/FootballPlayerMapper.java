package com.player.market.transfer.footballplayer.mapper;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class FootballPlayerMapper {

    public static FootballPlayerDto mapToDto(FootballPlayer player) {
        return new FootballPlayerDto()
                .id(player.getId())
                .name(player.getName())
                .yearOfBirth(player.getYearOfBirth())
                .dateOfStartCareer(OffsetDateTime.ofInstant(player.getStartOfCareerDate(), ZoneId.systemDefault()));
    }

    public List<FootballPlayerDto> mapToDto(List<FootballPlayer> players) {
        List<FootballPlayerDto> result = new ArrayList<FootballPlayerDto>();
        players.forEach(it -> result.add(mapToDto(it)));
        return result;
    }
}
