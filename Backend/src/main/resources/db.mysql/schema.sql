CREATE TABLE IF NOT EXISTS players (
   player_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(255),
   password VARCHAR(255),
   account_level INT NOT NULL,
   game_level INT NOT NULL,
   experience INT NOT NULL,
   shot_count INT NOT NULL,
   high_score INT NOT NULL,
   games_won INT NOT NULL,
   turret_color VARCHAR(10),
   ball_color VARCHAR(10),
   sound_status BOOLEAN,
   music_status BOOLEAN,
   tip_status BOOLEAN,
   color_blind_status BOOLEAN
);

CREATE TABLE IF NOT EXISTS achievements (
  achievement_id INT AUTO_INCREMENT PRIMARY KEY,
  achievement_name char(50),
  exp_value int,
  description char(100)
);

CREATE TABLE IF NOT EXISTS player_achievements (
  player_id INT NOT NULL REFERENCES players,
  achievement_id INT NOT NULL REFERENCES achievements
);

CREATE TABLE IF NOT EXISTS games (
  game_id INT AUTO_INCREMENT PRIMARY KEY,
  player_1 int,
  player_2 int,
  player_3 int,
  player_4 int,
  player_1_score int,
  player_2_score int,
  player_3_score int,
  player_4_score int,
  winner int
);

ALTER TABLE games ADD game_started boolean;
ALTER TABLE games ADD game_over boolean;
ALTER TABLE games ADD created_at datetime;