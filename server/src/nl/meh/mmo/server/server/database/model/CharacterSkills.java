package nl.meh.mmo.server.server.database.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

import static nl.meh.mmo.server.server.database.DatabaseSettings.*;

@Entity
@Table(name = CHARACTER_SKILLS_TABLE_NAME, schema = "public")
public class CharacterSkills implements Serializable {

    @Id
    @OneToOne(targetEntity = Character.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = CHARACTER_SKILLS_COLUMN_CHARACTER_NAME, nullable = false)
    private Character character;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_TOTAL, nullable = false)
    private int totalLevel;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_COMBAT, nullable = false)
    private int combatLevel;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_SKILL, nullable = false)
    private int skillLevel;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_ATTACK, nullable = false)
    private int attack;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_DEFENCE, nullable = false)
    private int defence;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_STRENGTH, nullable = false)
    private int strength;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_HIT_POINTS, nullable = false)
    private int hitpoints;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_MINING, nullable = false)
    private int mining;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_WOODCUTTING, nullable = false)
    private int woodcutting;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_SMITHING, nullable = false)
    private int smithing;

    @Column(name = CHARACTER_SKILLS_COLUMN_LEVEL_BARTERING, nullable = false)
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

    public CharacterSkills(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterSkills)) return false;

        CharacterSkills that = (CharacterSkills) o;

        return  attack == that.attack &&
                bartering == that.bartering &&
                combatLevel == that.combatLevel &&
                defence == that.defence &&
                hitpoints == that.hitpoints &&
                mining == that.mining &&
                skillLevel == that.skillLevel &&
                smithing == that.smithing &&
                strength == that.strength &&
                totalLevel == that.totalLevel &&
                woodcutting == that.woodcutting &&
                character.equals(that.character);

    }

    @Override
    public int hashCode() {
        int result = character.hashCode();
        result = 31 * result + totalLevel;
        result = 31 * result + combatLevel;
        result = 31 * result + skillLevel;
        result = 31 * result + attack;
        result = 31 * result + defence;
        result = 31 * result + strength;
        result = 31 * result + hitpoints;
        result = 31 * result + mining;
        result = 31 * result + woodcutting;
        result = 31 * result + smithing;
        result = 31 * result + bartering;
        return result;
    }
}
