package com.example.crossfire.achievements;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository class for <code>Achievements</code>
 * @author Nick Robinson
 */
@Repository
public interface AchievementRepository extends JpaRepository<Achievements, Integer>{

    /**
     * Used to retrieve all achievements in the database
     *
     * @return the list of all the achievements that exist
     */
	List<Achievements> findAll();


    /**
     * Used to save an achievement in the database
     *
     * @param  achievement the achievement being saved
     * @return the achievement that was just saved
     */
    Achievements save(Achievements achievement);

    /**
     * Used to retrieve a specific achievement by id
     *
     * @param  achievement_Id the id of the desired achievement in the database
     * @return the achievement being retrieved
     */
    Achievements findByAchievementId(@Param("achievement_Id") int achievement_Id);

}
