package com.player.market.transfer.player.service;

import com.player.market.transfer.dto.PlayerDto;
import com.player.market.transfer.player.entity.Player;
import com.player.market.transfer.player.exception.PlayerNotFoundException;
import com.player.market.transfer.player.mapper.PlayerMapper;
import com.player.market.transfer.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerDto> getPlayers(Integer sizeOnPage, Integer page) {
        Pageable pageable = PageRequest.of(page, sizeOnPage);
        Page<Player> all = playerRepository.findAll(pageable);
        return playerMapper.mapToDto(all.getContent());
    }

    public PlayerDto save(PlayerDto player) {
        Player result = Player.builder()
                .name(player.getName())
                .creationDate(Instant.now())
                .build();
        result = playerRepository.save(result);
        log.debug("Player with data: " + result.toString() + " saved");
        return playerMapper.mapToDto(result);
    }

    public PlayerDto getPlayerDto(UUID id) {
        return playerMapper.mapToDto(findOrThrow(id));
    }

    public Player getPlayer(UUID id) {
        return findOrThrow(id);
    }

    public void removePlayer(UUID id) {
        findOrThrow(id);
        log.info("Removing player with the following id:" + id.toString());
        playerRepository.deleteById(id);
    }

    public PlayerDto update(PlayerDto player) {
        Player existingPlayer = findOrThrow(player.getId());
        existingPlayer.setName(player.getName());
        playerRepository.save(existingPlayer);
        log.info("Player with data: " + existingPlayer.toString() + " updated");
        return playerMapper.mapToDto(existingPlayer);
    }

    private Player findOrThrow(UUID id) {
        Optional<Player> result = playerRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new PlayerNotFoundException("Player not found", "code");
    }
}
