package com.player.market.transfer.footballplayer.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.player.market.transfer.dbcleaner.DbCleaner.clearDatabase;
import static com.player.market.transfer.factory.FootballPlayerFactory.getFootballPlayerEntity;
import static com.player.market.transfer.factory.FootballPlayerFactory.getTestFootballPlayerDtoForSave;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class FootballPlayerControllerSpringTest {

    @Autowired
    FootballPlayerRepository playerRepository;
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
    public void shouldGetPlayers() throws Exception {
        //given
        playerRepository.save(getFootballPlayerEntity());
        playerRepository.save(getFootballPlayerEntity());

        //expect
        mockMvc.perform(get("/football-players"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[1].name").value("name"));
    }

    @Test
    public void shouldGetPlayer() throws Exception {
        //given
        FootballPlayer player = playerRepository.save(getFootballPlayerEntity());

        //expect
        mockMvc.perform(get("/football-players/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.id").value(player.getId().toString()));
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        //given
        FootballPlayer player = playerRepository.save(getFootballPlayerEntity());

        //expect
        mockMvc.perform(delete("/football-players/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(0, playerRepository.findAll().size());
    }

    @Test
    public void shouldSavePlayer() throws Exception {
        //given
        FootballPlayerDto player = getTestFootballPlayerDtoForSave();

        //expect
        mockMvc.perform(post("/football-players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(player)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.yearOfBirth").value(player.getYearOfBirth()));

        assertEquals(1, playerRepository.findAll().size());
    }

    @Test
    public void shouldThrowDuringRemovingNotExistingPlayer() throws Exception {
        //expect
        mockMvc.perform(delete("/football-players/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
