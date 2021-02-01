package com.player.market.transfer.team.mapper;

import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.team.entity.Team;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.player.market.transfer.factory.PlayerFactory.getPlayerEntity;
import static com.player.market.transfer.factory.TeamFactory.getTeamEntityForSave;

@RunWith(MockitoJUnitRunner.class)
public class TeamMapperTest {

    private TeamMapper teamMapper;

    @Before
    public void setUp() {
        teamMapper = new TeamMapper();
    }

    @Test
    public void shouldMapToDto() {
        Team team = getTeamEntityForSave(getPlayerEntity(), Lists.emptyList());

        TeamDto mappedTeam = teamMapper.mapToDto(team);

        Assertions.assertThat(mappedTeam.getName()).isEqualTo(team.getName());
        Assertions.assertThat(mappedTeam.getCurrency().toString()).isEqualTo(team.getCurrency().toString());
        Assertions.assertThat(mappedTeam.getOwner().getName()).isEqualTo(team.getOwner().getName());
    }

    @Test
    public void shouldMapToDtoList() {
        Team team = getTeamEntityForSave(getPlayerEntity(), Lists.emptyList());

        List<TeamDto> mappedTeam = teamMapper.mapToDto(Arrays.asList(team));

        Assertions.assertThat(mappedTeam.get(0).getName()).isEqualTo(team.getName());
        Assertions.assertThat(mappedTeam.get(0).getCurrency().toString()).isEqualTo(team.getCurrency().toString());
        Assertions.assertThat(mappedTeam.get(0).getOwner().getName()).isEqualTo(team.getOwner().getName());
    }
}
