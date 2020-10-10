package com.example.crossfire.games;

public class GameService {

    /**
     * Used to determine if a specific player is
     * in a specific game
     *
     * @param game the game being checked
     * @param playerId the playerId we are checking for
     * @return boolean that says whether or not that player is in the game
     */
    public boolean playerIsInGame(Games game, Integer playerId) {
        if (game.getPlayer1() == playerId) {
            return true;
        } else if (game.getPlayer2() == playerId) {
            return true;
        } else if (game.getPlayer3() == playerId) {
            return true;
        } else if (game.getPlayer4() == playerId) {
            return true;
        }
        return false;
    }

}
