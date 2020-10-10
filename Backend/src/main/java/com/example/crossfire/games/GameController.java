package com.example.crossfire.games;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zane Seuser
 */
@RestController
class GameController {

    @Autowired
    GameRepository gameRepository;

    //GameService service;

    /**
     * Used remove add a player to a game. Will add
     * the player to the first open position in the oldest
     * game that has not yet been started. It will not add
     * a player to a game that said player is already in.
     *
     * @param  playerId the id of the player joining a game
     * @return the game that the player joined
     */
    @GetMapping("/games/join-game/{playerId}")
    public Games joinGame(@PathVariable("playerId") int playerId) {
        List<Games> games = gameRepository.findAllByGameStartedEqualsOrderByCreatedAt(false);
        for (int i = 0; i < games.size(); i++) {
            //if (!service.playerIsInGame(games.get(i), playerId)) {
                if (games.get(i).getPlayer1() == null) {
                    games.get(i).setPlayer1(playerId);
                    gameRepository.save(games.get(i));
                    return games.get(i);
                } else if (games.get(i).getPlayer2() == null) {
                    games.get(i).setPlayer2(playerId);
                    gameRepository.save(games.get(i));
                    return games.get(i);
                } else if (games.get(i).getPlayer3() == null) {
                    games.get(i).setPlayer3(playerId);
                    gameRepository.save(games.get(i));
                    return games.get(i);
                } else if (games.get(i).getPlayer4() == null) {
                    games.get(i).setPlayer4(playerId);
                    gameRepository.save(games.get(i));
                    return games.get(i);
                }
            //}
        }
        Games game = new Games();
        game.setPlayer1(playerId);
        gameRepository.save(game);
        return game;
    }

    /**
     * Used remove a player from a game
     *
     * @param  gameId the id of the game that the player will be removed from
     * @param  playerId the id of the player being removed
     * @return the game that the player was removed from
     */
    @PostMapping("/games/leave-game/{gameId}/{playerId}")
    public Games leaveGame(@PathVariable("gameId") int gameId, @PathVariable("playerId") int playerId) {
        Games game = gameRepository.findByGameId(gameId);
        if (game.getPlayer1() == playerId) {
            game.setPlayer1(null);
        } else if (game.getPlayer2() == playerId) {
            game.setPlayer2(null);
        } else if (game.getPlayer3() == playerId) {
            game.setPlayer3(null);
        } else if (game.getPlayer4() == playerId) {
            game.setPlayer4(null);
        }
        gameRepository.save(game);
        return game;
    }

    /**
     * Used to start a specific game by id
     *
     * @param  gameId the id of the desired game to start
     * @return the game being started
     */
    @PostMapping("/games/start-game/{gameId}")
    public Games startGame(@PathVariable("gameId") int gameId) {
        Games game = gameRepository.findByGameId(gameId);
        game.setGameStarted(true);
        return gameRepository.save(game);
    }

    /**
     * Used to end a specific game by id
     *
     * @param  gameId the id of the desired game to end
     * @return the game being ended
     */
    @PostMapping("/games/end-game/{gameId}")
    public Games endGame(@PathVariable("gameId") int gameId) {
        Games game = gameRepository.findByGameId(gameId);
        game.setGameOver(true);
        return gameRepository.save(game);
    }

    /**
     * Used to save a game in the database
     *
     * @return the game that was just saved
     */
    @PostMapping("/games/new")
    public Games createGame() {
        Games game = new Games();
        return gameRepository.save(game);
    }

    /**
     * Used to retrieve all games
     *
     * @return the list of all the games that exist
     */
    @GetMapping("/games")
    public List<Games> getAllGames() {
        return gameRepository.findAll();
    }

    /**
     * Used to retrieve a specific game by id
     *
     * @param  gameId the id of the desired game
     * @return the game being retrieved
     */
    @GetMapping("/games/{gameId}")
    public Games getGameById(@PathVariable("gameId") int gameId) {
        return gameRepository.findByGameId(gameId);
    }

    /**
     * Used to update attributes of a game
     * The updates object must contain id of the desired game to update
     *
     * @param  updates the json object that contains updated fields & id
     * @return the updated game
     */
    @PatchMapping("/games")
    public Games updateAchievement(@RequestBody Map<String, String> updates) {
        Games game = gameRepository.findByGameId(Integer.parseInt(updates.get("gameId")));
        if (updates.get("player1") != null) {
            game.setPlayer1(Integer.parseInt(updates.get("player1")));
        }
        if (updates.get("player2") != null) {
            game.setPlayer2(Integer.parseInt(updates.get("player2")));
        }
        if (updates.get("player3") != null) {
            game.setPlayer3(Integer.parseInt(updates.get("player3")));
        }
        if (updates.get("player4") != null) {
            game.setPlayer4(Integer.parseInt(updates.get("player4")));
        }
        if (updates.get("player1Score") != null) {
            game.setPlayer1Score(Integer.parseInt(updates.get("player1Score")));
        }
        if (updates.get("player2Score") != null) {
            game.setPlayer2Score(Integer.parseInt(updates.get("player2Score")));
        }
        if (updates.get("player3Score") != null) {
            game.setPlayer3Score(Integer.parseInt(updates.get("player3Score")));
        }
        if (updates.get("player4Score") != null) {
            game.setPlayer4Score(Integer.parseInt(updates.get("player4Score")));
        }
        if (updates.get("winner") != null) {
            game.setWinner(Integer.parseInt(updates.get("winner")));
        }
        gameRepository.save(game);
        return game;
    }

    /**
     * Used to delete a specific game by id
     *
     * @param  gameId the id of the desired game to delete
     * @return a list of all the games still left
     */
    @DeleteMapping("/games/{gameId}")
    public List<Games> deleteGameById(@PathVariable("gameId") int gameId) {
        Games game = gameRepository.findByGameId(gameId);
        gameRepository.delete(game);
        return gameRepository.findAll();
    }

}
