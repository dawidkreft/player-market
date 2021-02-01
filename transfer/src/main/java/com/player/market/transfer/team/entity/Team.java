package com.player.market.transfer.team.entity;

import com.player.market.transfer.footballplayer.entity.FootballPlayer;
import com.player.market.transfer.player.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "TEAM")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "team_id")
    private UUID id;

    @NonNull
    @Column(name = "team_name")
    private String name;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_onwer_id", referencedColumnName = "player_id")
    private Player owner;

    /**
     * I think that, the number of players is limited by law, for ex. 20players,
     * so I decided for eager fetch
     */
    @NonNull
    @Column(name = "team_players")
    @OneToMany(fetch = FetchType.EAGER)
    private List<FootballPlayer> players;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "team_currency")
    private Currency currency;

    @NonNull
    @Column(name = "team_creation_date")
    private Instant creationDate;
}
