package com.player.market.transfer.footballplayer.repository;

import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FootballPlayerRepository extends JpaRepository<FootballPlayer, UUID> {
}
