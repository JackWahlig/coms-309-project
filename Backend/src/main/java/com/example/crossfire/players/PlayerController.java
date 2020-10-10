package com.example.crossfire.players;

import com.example.crossfire.achievements.Achievements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Zane Seuser
 */
@RestController
class PlayerController {

    @Autowired
    PlayerRepository playersRepository;

    /**
     * Used to register a player. Will check the player for a
     * username that doesn't already exist.
     *
     * @param player the player that is trying to register
     * @return player if register is successful, otherwise error message
     */
    @PostMapping("/players/register")
    public Object registerPlayer(@RequestBody Players player) {
        Players existingPlayer = playersRepository.findByUsername(player.getUsername());
        if (existingPlayer == null) {
            playersRepository.save(player);
            return player;
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("registration", "failure");
            return map;
        }
    }

    /**
     * Used to login a player. Will check the player for a correct
     * username and password
     *
     * @param player the player that is trying to login
     * @return player if login is successful, otherwise error message
     */
    @PostMapping("/players/login")
    public Object loginPlayer(@RequestBody Players player) {
        Players existingPlayer = playersRepository.findByUsernameAndPassword(player.getUsername(), player.getPassword());
        if (existingPlayer == null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("playerInvalid", "true");
            return map;
        } else {
            return existingPlayer;
        }
    }

    /**
     * Used to retrieve all players
     *
     * @return the list of all the players that exist
     */
    @GetMapping(path = "/players")
    public List<Players> getAllPlayers() {
        List<Players> results = playersRepository.findAll();
        return results;
    }

    /**
     * Used to retrieve a specific player by id
     *
     * @param  playerId the id of the desired player
     * @return the player being retrieved
     */
    @GetMapping("/players/{playerId}")
    public Players findPlayerById(@PathVariable("playerId") int playerId) {
        return playersRepository.findByPlayerId(playerId);
    }

    /**
     * Used to update attributes of a player.
     * The updates object must contain id of the desired player to update
     *
     * @param  updates the json object that contains updated fields & id
     * @return the updated player
     */
    @PatchMapping("/players")
    public Players updatePlayer(@RequestBody Map<String, String> updates) {
        Players player = playersRepository.findByPlayerId(Integer.parseInt(updates.get("playerId")));

        if (updates.get("username") != null) {
            player.setUsername(updates.get("username"));
        }
        if (updates.get("password") != null) {
            player.setPassword(updates.get("password"));
        }
        if (updates.get("accountLevel") != null) {
            player.setAccountLevel(Integer.parseInt(updates.get("accountLevel")));
        }
        if (updates.get("gameLevel") != null) {
            player.setGameLevel(Integer.parseInt(updates.get("gameLevel")));
        }
        if (updates.get("experience") != null) {
            player.setExperience(Integer.parseInt(updates.get("experience")));
        }
        if (updates.get("shotCount") != null) {
            player.setShotCount(Integer.parseInt(updates.get("shotCount")));
        }
        if (updates.get("highScore") != null) {
            player.setHighScore(Integer.parseInt(updates.get("highScore")));
        }
        if (updates.get("gamesWon") != null) {
            player.setGamesWon(Integer.parseInt(updates.get("gamesWon")));
        }
        if (updates.get("turretColor") != null) {
            player.setTurretColor(updates.get("turretColor"));
        }
        if (updates.get("ballColor") != null) {
            player.setBallColor(updates.get("ballColor"));
        }
        if (updates.get("soundStatus") != null) {
            player.setSoundStatus(Boolean.parseBoolean(updates.get("soundStatus")));
        }
        if (updates.get("musicStatus") != null) {
            player.setMusicStatus(Boolean.parseBoolean(updates.get("musicStatus")));
        }
        if (updates.get("tipStatus") != null) {
            player.setTipStatus(Boolean.parseBoolean(updates.get("tipStatus")));
        }
        if (updates.get("colorBlindStatus") != null) {
            player.setColorBlindStatus(Boolean.parseBoolean(updates.get("colorBlindStatus")));
        }

        playersRepository.save(player);
        return player;
    }

    /**
     * Used to delete a specific player by id
     *
     * @param  playerId the id of the desired player to delete
     * @return a list of all the players still left
     */
    @DeleteMapping("/players/{playerId}")
    public List<Players> deletePlayerById(@PathVariable("playerId") int playerId) {
        Players player = playersRepository.findByPlayerId(playerId);
        playersRepository.delete(player);
        List<Players> results = playersRepository.findAll();
        return results;
    }

    /**
     * Used to get achievements of a specific player by player id
     *
     * @param  playerId the id of the player
     * @return a list of all the achievements achieved by that player
     */
    @GetMapping(path = "/players/{playerId}/achievements")
    public Set<Achievements> getPlayerAchievements(@PathVariable("playerId") int playerId) {
        Players player = playersRepository.findByPlayerId(playerId);
        Set<Achievements> results = player.achievements();
        return results;
    }

}