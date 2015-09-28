package nl.marcelhollink.mmorpg.backend.server.database.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "characters", schema = "public")
public class Character implements Serializable{

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String characterName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "combat_level", nullable = false)
    private int combat_level;

    @Column(name = "skill_level", nullable = false)
    private int skill_level;

    @Column(name = "balance", nullable = false)
    private int balance;

    public Character(String characterName, String gender, int combat_level, int skill_level, int balance) {
        this.characterName = characterName;
        this.gender = gender;
        this.combat_level = combat_level;
        this.skill_level = skill_level;
        this.balance = balance;
    }

    public Character() {
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCombat_level() {
        return combat_level;
    }

    public void setCombat_level(int combat_level) {
        this.combat_level = combat_level;
    }

    public int getSkill_level() {
        return skill_level;
    }

    public void setSkill_level(int skill_level) {
        this.skill_level = skill_level;
    }
}
