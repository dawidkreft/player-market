package com.player.market.transfer.player.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.repository.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.player.market.transfer.dbcleaner.DbCleaner.clearDatabase;
import static com.player.market.transfer.factory.PlayerFactory.getPlayerEntity;
import static com.player.market.transfer.factory.PlayerFactory.getTestPlayerDtoForSave;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class PlayerControllerSpringTest {

    @Autowired
    PlayerRepository playerRepository;
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
        playerRepository.save(getPlayerEntity());
        playerRepository.save(getPlayerEntity());

        //expect
        mockMvc.perform(get("/players"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("test"))
                .andExpect(jsonPath("$.[1].name").value("test"));
    }

    @Test
    public void shouldGetPlayer() throws Exception {
        //given
        Player player = playerRepository.save(getPlayerEntity());

        //expect
        mockMvc.perform(get("/players/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.id").value(player.getId().toString()));
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        //given
        Player player = playerRepository.save(getPlayerEntity());

        //expect
        mockMvc.perform(delete("/players/" + player.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(0, playerRepository.findAll().size());
    }

    @Test
    public void shouldSavePlayer() throws Exception {
        //given
        PlayerDto player = getTestPlayerDtoForSave();

        //expect
        mockMvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(player)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(player.getName()));

        assertEquals(1, playerRepository.findAll().size());
    }

    @Test
    public void shouldThrowDuringRemovingNotExistingPlayer() throws Exception {

        //expect
        mockMvc.perform(delete("/players/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
