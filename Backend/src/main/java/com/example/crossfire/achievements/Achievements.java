package com.example.crossfire.achievements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * Simple JavaBean domain object representing an achievement.
 * @author Nick Robinson
 */
@Entity
@Table(name = "achievements")
public class Achievements {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievementId")
    private Integer achievementId;
	
	public void setAchievementId (Integer achievementId){
		this.achievementId = achievementId;
	}

    @Column(name = "achievementName")
    private String achievementName;

    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }

    @Column(name = "expValue")
    private Integer expValue;

    public void setExpValue(Integer expValue) {
        this.expValue = expValue;
    }

    @Column(name = "description")
    private String description;
    
    public void setDescription(String description){
    	this.description = description;
    }
    
    public Integer getAchievementId() { return achievementId; }
    
    public String getAchievementName(){
    	return achievementName;
    }
    
    public Integer getExpValue(){
    	return expValue;
    }

    public String getDescription() { return description; }
}
