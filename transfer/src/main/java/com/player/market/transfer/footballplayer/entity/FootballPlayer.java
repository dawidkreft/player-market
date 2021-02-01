package com.player.market.transfer.footballplayer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "FOOTBALL_PLAYER")
public class FootballPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "football_player_id")
    private UUID id;

    @NonNull
    @Column(name = "football_player_name")
    private String name;

    @NonNull
    @Column(name = "football_player_year_of_birth")
    private Integer yearOfBirth;

    @NonNull
    @Column(name = "football_player_start_of_career_date")
    private Instant startOfCareerDate;

    @NonNull
    @Column(name = "football_player_creation_date")
    private Instant creationDate;
}
