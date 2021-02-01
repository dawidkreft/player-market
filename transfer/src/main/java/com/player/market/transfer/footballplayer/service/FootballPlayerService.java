package com.player.market.transfer.footballplayer.service;

import com.player.market.transfer.dto.FootballPlayerDto;
import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.dto.TransferFeeDto;
import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.footballplayer.exception.FootBallPlayerInCorrectYearOfBirthException;
import com.player.market.transfer.footballplayer.exception.FootBallPlayerInStartOfExperienceDateException;
import com.player.market.transfer.footballplayer.exception.FootballPlayerNotFoundException;
import com.player.market.transfer.footballplayer.mapper.FootballPlayerMapper;
import com.player.market.transfer.footballplayer.repository.FootballPlayerRepository;
import com.player.market.transfer.team.entity.Currency;
import com.player.market.transfer.team.entity.Team;
import com.player.market.transfer.team.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.player.market.transfer.common.CurrencyCalculator.calculate;

@Service
@Slf4j
@AllArgsConstructor
public class FootballPlayerService {

    private static final double PLAYER_VALUE = 100000.0;
    private static final double TEAM_COMMISSION_VALUE = 0.1;
    private final FootballPlayerRepository playerRepository;
    private final FootballPlayerMapper playerMapper;
    private final TeamService teamService;

    public List<FootballPlayerDto> getPlayers(Integer sizeOnPage, Integer page) {
        Pageable pageable = PageRequest.of(page, sizeOnPage);
        return playerMapper.mapToDto(playerRepository.findAll(pageable).getContent());
    }

    public FootballPlayerDto save(FootballPlayerDto player) {
        validateAge(player.getYearOfBirth());
        validateExperience(player.getDateOfStartCareer());
        FootballPlayer result = FootballPlayer.builder()
                .name(player.getName())
                .yearOfBirth(player.getYearOfBirth())
                .startOfCareerDate(player.getDateOfStartCareer().toInstant())
                .creationDate(Instant.now())
                .build();
        result = playerRepository.save(result);
        log.debug("Player with data: " + result.toString() + " saved");
        return FootballPlayerMapper.mapToDto(result);
    }

    private void validateExperience(OffsetDateTime dateOfStartCareer) {
        if (Instant.now().isBefore(dateOfStartCareer.toInstant())) {
            throw new FootBallPlayerInStartOfExperienceDateException("Incorrect data", "code");
        }
    }

    private void validateAge(Integer yearOfBirth) {
        if (LocalDate.now().getYear() - yearOfBirth <= 0) {
            throw new FootBallPlayerInCorrectYearOfBirthException("Incorrect data", "code");
        }
    }

    public FootballPlayerDto getPlayer(UUID id) {
        return playerMapper.mapToDto(findOrThrow(id));
    }

    public void removePlayer(UUID id) {
        findOrThrow(id);
        log.info("Removing player with the following id:" + id.toString());
        playerRepository.deleteById(id);
    }

    public FootballPlayerDto update(FootballPlayerDto player) {
        validateAge(player.getYearOfBirth());
        validateExperience(player.getDateOfStartCareer());
        FootballPlayer existingPlayer = findOrThrow(player.getId());
        existingPlayer.setName(player.getName());
        playerRepository.save(existingPlayer);
        log.info("Player with data: " + existingPlayer.toString() + " updated");
        return playerMapper.mapToDto(existingPlayer);
    }

    private FootballPlayer findOrThrow(UUID id) {
        Optional<FootballPlayer> result = playerRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new FootballPlayerNotFoundException("Player not found", "code");
    }

    public TransferFeeDto calculateTransfer(UUID footballPlayerId, UUID teamId) {
        FootballPlayerDto player = getPlayer(footballPlayerId);
        double transferFee = calculateTransferFee(player);
        double teamCommission = transferFee * TEAM_COMMISSION_VALUE;
        TeamDto teamDest = teamService.getTeam(teamId);
        Team sourceTeam = teamService.findByPlayerId(footballPlayerId);
        double contractFeeInCurrentCurrency = calculate(Currency.valueOf(teamDest.getCurrency().toString()), sourceTeam.getCurrency(), teamCommission + transferFee);
        return new TransferFeeDto()
                .transferFee(transferFee)
                .teamCommision(teamCommission)
                .contractFee(contractFeeInCurrentCurrency)
                .footballPlayer(player)
                .destinationTeam(teamDest)
                .currency(teamDest.getCurrency());
    }

    private double calculateTransferFee(FootballPlayerDto player) {
        // validateAge and validateExperience protect before incorrect value
        int months = Period.between(player.getDateOfStartCareer().toLocalDate(), LocalDate.now()).getMonths();
        int age = LocalDate.now().getYear() - player.getYearOfBirth();
        return (months * PLAYER_VALUE) / age;
    }
}
