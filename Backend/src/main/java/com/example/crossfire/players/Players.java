package com.example.crossfire.players;

import com.example.crossfire.achievements.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple JavaBean domain object representing a players.
 *
 * @author Zane Seuser
 */
@Entity
@Table(name = "players")
public class Players {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Integer playerId;

    @NotEmpty
    @Column(name = "username")
    private String username;

    public void setUsername(String username) { this.username = username; }

    @NotEmpty
    @Column(name = "password")
    private String password;

    public void setPassword(String password) { this.password = password; }

    @Column(name = "account_level")
    private Integer accountLevel = 1;

    public void setAccountLevel(Integer accountLevel) { this.accountLevel = accountLevel; }

    @Column(name = "game_level")
    private Integer gameLevel = 1;

    public void setGameLevel(Integer gameLevel) { this.gameLevel = gameLevel; }

    @Column(name = "experience")
    private Integer experience = 0;

    public void setExperience(Integer experience) { this.experience = experience; }

    @Column(name = "shot_count")
    private Integer shotCount = 0;

    public void setShotCount(Integer shotCount) { this.shotCount = shotCount; }

    @Column(name = "high_score")
    private Integer highScore = 0;

    public void setHighScore(Integer highScore) { this.highScore = highScore; }

    @Column(name = "games_won")
    private Integer gamesWon = 0;

    public void setGamesWon(Integer gamesWon) { this.gamesWon = gamesWon; }

    @Column(name = "turret_color")
    private String turretColor = "#000000";

    public void setTurretColor(String turretColor) { this.turretColor = turretColor; }

    @Column(name = "ball_color")
    private String ballColor = "#000000";

    public void setBallColor(String ballColor) { this.ballColor = ballColor; }

    @Column(name = "sound_status")
    private Boolean soundStatus = true;

    public void setSoundStatus(boolean soundStatus) { this.soundStatus = soundStatus; }

    @Column(name = "music_status")
    private Boolean musicStatus = true;

    public void setMusicStatus(boolean musicStatus) { this.musicStatus = musicStatus; }

    @Column(name = "tip_status")
    private Boolean tipStatus = false;

    public void setTipStatus(boolean tipStatus) { this.tipStatus = tipStatus; }

    @Column(name = "color_blind_status")
    private Boolean colorBlindStatus = false;

    public void setColorBlindStatus(boolean colorBlindStatus) { this.colorBlindStatus = colorBlindStatus; }

    public Integer getPlayerId() { return this.playerId; }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() { return this.password; }

    public Integer getAccountLevel() { return this.accountLevel; }

    public Integer getGameLevel() { return this.gameLevel; }

    public Integer getExperience() { return this.experience; }

    public Integer getShotCount() { return this.shotCount; }

    public Integer getHighScore() { return this.highScore; }

    public Integer getGamesWon() { return this.gamesWon; }

    public String getTurretColor() { return this.turretColor; }

    public String getBallColor() { return this.ballColor; }

    public Boolean getSoundStatus() { return this.soundStatus; }

    public Boolean getMusicStatus() { return this.musicStatus; }

    public Boolean getTipStatus() { return this.tipStatus; }

    public Boolean getColorBlindStatus() { return this.colorBlindStatus; }

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "player_achievements",
            joinColumns = { @JoinColumn(name = "player_id") },
            inverseJoinColumns = { @JoinColumn(name = "achievement_id") }
    )
    Set<Achievements> achievements = new HashSet<>();

    public Set<Achievements> achievements() { return this.achievements; }

}
