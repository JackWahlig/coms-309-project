package com.example.crossfire.games;

import javax.persistence.*;
import java.util.Date;

/**
 * Simple JavaBean domain object representing a game.
 *
 * @author Zane Seuser
 */
@Entity
@Table(name = "games")
public class Games {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Integer gameId;

    public void setGameId (Integer gameId){
        this.gameId = gameId;
    }

    @Column(name = "player_1")
    private Integer player1;

    public void setPlayer1(Integer player1) {
        this.player1 = player1;
    }

    @Column(name = "player_2")
    private Integer player2;

    public void setPlayer2(Integer player2) {
        this.player2 = player2;
    }

    @Column(name = "player_3")
    private Integer player3;

    public void setPlayer3(Integer player3) {
        this.player3 = player3;
    }

    @Column(name = "player_4")
    private Integer player4;

    public void setPlayer4(Integer player4) {
        this.player4 = player4;
    }

    @Column(name = "player_1_score")
    private Integer player1score;

    public void setPlayer1Score(Integer player1Score) {
        this.player1score = player1Score;
    }

    @Column(name = "player_2_score")
    private Integer player2score;

    public void setPlayer2Score(Integer player2Score) {
        this.player2score = player2Score;
    }

    @Column(name = "player_3_score")
    private Integer player3score;

    public void setPlayer3Score(Integer player3Score) {
        this.player3score = player3Score;
    }

    @Column(name = "player_4_score")
    private Integer player4score;

    public void setPlayer4Score(Integer player4Score) {
        this.player4score = player4Score;
    }

    @Column(name = "winner")
    private Integer winner;

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    @Column(name = "game_started")
    private Boolean gameStarted = false;

    public void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    @Column(name = "game_over")
    private Boolean gameOver = false;

    public void setGameOver(Boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Column(name = "created_at")
    private Date createdAt = new Date();

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getGameId() { return this.gameId; }

    public Integer getPlayer1() { return this.player1; }

    public Integer getPlayer2() { return this.player2; }

    public Integer getPlayer3() { return this.player3; }

    public Integer getPlayer4() { return this.player4; }

    public Integer getPlayer1score() { return this.player1score; }

    public Integer getPlayer2score() { return this.player2score; }

    public Integer getPlayer3score() { return this.player3score; }

    public Integer getPlayer4score() { return this.player4score; }

    public Integer getWinner() { return this.winner; }

    public Boolean getGameStarted() { return this.gameStarted; }

    public Boolean getGameOver() { return this.gameOver; }

    public Date getCreatedAt() { return this.createdAt; }

}
