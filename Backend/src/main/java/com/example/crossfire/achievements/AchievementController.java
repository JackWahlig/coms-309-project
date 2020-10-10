package com.example.crossfire.achievements;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nick Robinson
 */
@RestController
public class AchievementController {

    @Autowired
    AchievementRepository achievementsRepository;

    /**
     * Used to save an achievement in the database
     *
     * @param  achievement the achievement being saved
     * @return the achievement that was just saved
     */
    @RequestMapping(method = RequestMethod.POST, path = "/achievements/new")
    public Achievements saveAchievement(@RequestBody Achievements achievement) {
        return achievementsRepository.save(achievement);
    }

    /**
     * Used to retrieve all achievements
     *
     * @return the list of all the achievements that exist
     */
    @RequestMapping(method = RequestMethod.GET, path = "/achievements")
    public List<Achievements> getAllAchievements() {
        List<Achievements> results = achievementsRepository.findAll();
        return results;
    }

    /**
     * Used to retrieve a specific achievement by id
     *
     * @param  achievementId the id of the desired achievement
     * @return the achievement being retrieved
     */
    @GetMapping("/achievements/{achievement_Id}")
    public Achievements findAchievementById(@PathVariable("achievement_Id") int achievementId) {
        return achievementsRepository.findByAchievementId(achievementId);
    }

    /**
     * Used to update attributes of an achievement
     * The updates object must contain id of the desired achievement to update
     *
     * @param  updates the json object that contains updated fields & id
     * @return the updated achievement
     */
    @PatchMapping("/achievements")
    public Achievements updateAchievement(@RequestBody Map<String, String> updates) {
        Achievements achievement = achievementsRepository.findByAchievementId(Integer.parseInt(updates.get("achievement_Id")));

        if (updates.get("achievement_Name") != null) {
            achievement.setAchievementName(updates.get("achievement_Name"));
        }
        if (updates.get("exp_Value") != null) {
            achievement.setExpValue(Integer.parseInt(updates.get("exp_Value")));
        }
        if (updates.get("description") != null) {
            achievement.setDescription(updates.get("description"));
        }

        achievementsRepository.save(achievement);
        return achievement;
    }

    /**
     * Used to delete a specific achievement by id
     *
     * @param  achievementId the id of the desired achievement to delete
     * @return a list of all the achievements still left
     */
    @DeleteMapping("/achievements/{achievement_Id}")
    public List<Achievements> deleteAchievementById(@PathVariable("achievement_Id") int achievementId) {
        Achievements achievement = achievementsRepository.findByAchievementId(achievementId);
        achievementsRepository.delete(achievement);
        List<Achievements> results = achievementsRepository.findAll();
        return results;
    }
}
