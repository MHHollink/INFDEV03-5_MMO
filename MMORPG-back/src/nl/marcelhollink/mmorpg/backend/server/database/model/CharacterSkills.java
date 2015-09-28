package nl.marcelhollink.mmorpg.backend.server.database.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "character_skills", schema = "public")
public class CharacterSkills implements Serializable {

    @Id
    @OneToOne(targetEntity = Character.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "character_name", nullable = false)
    private Character character;

    @Column(name = "total_level", nullable = false)
    private int totalLevel;

    @Column(name = "combat_level", nullable = false)
    private int combatLevel;

    @Column(name = "skill_level", nullable = false)
    private int skillLevel;

    @Column(name = "attack_level", nullable = false)
    private int attack;

    @Column(name = "defence_level", nullable = false)
    private int defence;

    @Column(name = "strength_level", nullable = false)
    private int strength;

    @Column(name = "hitpoints_level", nullable = false)
    private int hitpoints;

    @Column(name = "mining_level", nullable = false)
    private int mining;

    @Column(name = "woodcutting_level", nullable = false)
    private int woodcutting;

    @Column(name = "smithing_level", nullable = false)
    private int smithing;

    @Column(name = "bartering_level", nullable = false)
    private int bartering;

    public CharacterSkills(Character character, int totalLevel, int combatLevel, int skillLevel, int attack, int defence, int strength, int hitpoints, int mining, int woodcutting, int smithing, int bartering) {
        this.character = character;
        this.totalLevel = totalLevel;
        this.combatLevel = combatLevel;
        this.skillLevel = skillLevel;
        this.attack = attack;
        this.defence = defence;
        this.strength = strength;
        this.hitpoints = hitpoints;
        this.mining = mining;
        this.woodcutting = woodcutting;
        this.smithing = smithing;
        this.bartering = bartering;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(int totalLevel) {
        this.totalLevel = totalLevel;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getMining() {
        return mining;
    }

    public void setMining(int mining) {
        this.mining = mining;
    }

    public int getWoodcutting() {
        return woodcutting;
    }

    public void setWoodcutting(int woodcutting) {
        this.woodcutting = woodcutting;
    }

    public int getSmithing() {
        return smithing;
    }

    public void setSmithing(int smithing) {
        this.smithing = smithing;
    }

    public int getBartering() {
        return bartering;
    }

    public void setBartering(int bartering) {
        this.bartering = bartering;
    }
}
