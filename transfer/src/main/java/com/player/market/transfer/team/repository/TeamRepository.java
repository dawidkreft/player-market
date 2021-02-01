package com.player.market.transfer.team.repository;

import com.player.market.transfer.team.entity.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    List<Team> findAllByOwnerId(UUID id, Pageable pageable);

    Optional<Team> findByPlayersId(UUID playerId);
}
