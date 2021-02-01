package com.player.market.transfer.team.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.factory.FootballPlayerFactory;
import com.player.market.transfer.factory.PlayerFactory;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.repository.PlayerRepository;
import com.player.market.transfer.team.entity.Team;
import com.player.market.transfer.team.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static com.player.market.transfer.dbcleaner.DbCleaner.clearDatabase;
import static com.player.market.transfer.factory.TeamFactory.getTeamDto;
import static com.player.market.transfer.factory.TeamFactory.getTeamEntityForSave;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerSpringTest {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    FootballPlayerRepository footballPlayerRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void tearDown() {
        try {
            clearDatabase();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Initialize db exception");
        }
    }

    @Test
    public void shouldGetTeams() throws Exception {
        //given
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        FootballPlayer player1 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        FootballPlayer player2 = footballPlayerRepository.save(FootballPlayerFactory.getFootballPlayerEntity());
        Team teamEntityForSave = getTeamEntityForSave(owner, Arrays.asList(player1, player2));
        teamRepository.save(teamEntityForSave);

        //expect
        mockMvc.perform(get("/teams")
                .queryParam("ownerId", owner.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].currency").value("EUR"))
                .andExpect(jsonPath("$.[0].owner.name").value("test"));
    }

    @Test
    public void shouldSaveTeam() throws Exception {
        //given
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        PlayerDto playerDto = new PlayerDto().id(owner.getId()).name(owner.getName());
        TeamDto teamDto = getTeamDto(playerDto, Collections.emptyList());

        //expect
        mockMvc.perform(post("/teams")
                .queryParam("ownerId", owner.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(teamDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(teamDto.getName()));

        assertEquals(1, teamRepository.findAll().size());
    }

    @Test
    public void shouldRemoveTeam() throws Exception {
        //given
        Player owner = playerRepository.save(PlayerFactory.getPlayerEntity());
        PlayerDto playerDto = new PlayerDto().id(owner.getId()).name(owner.getName());
        Team teamEntityForSave = getTeamEntityForSave(owner, Collections.emptyList());
        Team team = teamRepository.save(teamEntityForSave);
        int amountOfTeams = teamRepository.findAll().size();

        //expect
        mockMvc.perform(delete("/teams/" + team.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(0, teamRepository.findAll().size());
        assertEquals(1, amountOfTeams);
    }
}
