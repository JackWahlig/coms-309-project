package com.example.crossfire.players;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Players</code>
 * @author Zane Seuser
 */
@Repository
public interface PlayerRepository extends JpaRepository<Players, Integer> {

    /**
     * Used to retrieve all players in the database
     *
     * @return the list of all the players that exist
     */
    List<Players> findAll();

    /**
     * Used to save an player in the database
     *
     * @param  player the player being saved
     * @return the player that was just saved
     */
    Players save(Players player);

    /**
     * Used to retrieve a specific player by id
     *
     * @param  playerId the id of the desired player in the database
     * @return the player being retrieved
     */
    Players findByPlayerId(@Param("playerId") int playerId);

    /**
     * Used to retrieve a specific player by username
     *
     * @param  username the username of the desired player in the database
     * @return the player being retrieved
     */
    Players findByUsername(@Param("username") String username);

    /**
     * Used to retrieve a specific player by username & password
     *
     * @param  username the username of the desired player in the database
     * @param  password the password of the desired player in the database
     * @return the player being retrieved
     */
    Players findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
