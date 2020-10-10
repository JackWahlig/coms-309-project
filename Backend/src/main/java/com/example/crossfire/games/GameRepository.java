package com.example.crossfire.games;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Games</code>
 * @author Zane Seuser
 */
@Repository
public interface GameRepository extends JpaRepository<Games, Integer> {

    /**
     * Used to retrieve all games in the database
     *
     * @return the list of all the games that exist
     */
    List<Games> findAll();

    /**
     * Used to save an game in the database
     *
     * @param  game the game being saved
     * @return the game that was just saved
     */
    Games save(Games game);

    /**
     * Used to retrieve a specific game by id
     *
     * @param  gameId the id of the desired game in the database
     * @return the game being retrieved
     */
    Games findByGameId(@Param("gameId") int gameId);

    /**
     * Used to retrieve a list of all games in the database where
     * gameStarted equals @param gameStarted ordered by
     * createdAt.
     *
     * @param  gameStarted the desired value of the gameStarted field
     * @return the list of games
     */
    List<Games> findAllByGameStartedEqualsOrderByCreatedAt(Boolean gameStarted);

}
