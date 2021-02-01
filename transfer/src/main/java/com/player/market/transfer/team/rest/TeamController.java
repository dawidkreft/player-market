package com.player.market.transfer.team.rest;

import com.player.market.transfer.dto.TeamDto;
import com.player.market.transfer.rest.TeamsApi;
import com.player.market.transfer.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class TeamController implements TeamsApi {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public ResponseEntity<List<TeamDto>> getTeams(@NotNull @Valid UUID ownerId, @Min(1) @Max(50) @Valid Integer sizeOnPage, @Min(0) @Valid Integer page) {
        return new ResponseEntity<>(teamService.getTeams(ownerId, sizeOnPage, page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removeTeam(UUID id) {
        teamService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TeamDto> saveTeam(@NotNull @Valid UUID ownerId, @Valid TeamDto teamDto) {
        return new ResponseEntity<>(teamService.saveTeam(ownerId, teamDto), HttpStatus.CREATED);
    }
}
