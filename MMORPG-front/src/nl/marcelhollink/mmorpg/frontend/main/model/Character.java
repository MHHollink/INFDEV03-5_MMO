package nl.marcelhollink.mmorpg.frontend.main.model;


public class Character {

    private String characterName;
    private String gender;

    private int balance;

    private int totalLevel;
    private int combatLevel;
    private int skillLevel;
    private int attack;
    private int defence;
    private int strength;
    private int hitpoints;
    private int mining;
    private int woodcutting;
    private int smithing;
    private int bartering;

    public Character(String characterName, String gender, int balance) {
        this.characterName = characterName;
        this.gender = gender;
        this.balance = balance;
    }

    public void setAllLevels(int attack, int defence, int strength, int hitpoints, int mining, int woodcutting, int smithing, int bartering) {
        this.attack = attack;
        this.defence = defence;
        this.strength = strength;
        this.hitpoints = hitpoints;
        this.mining = mining;
        this.woodcutting = woodcutting;
        this.smithing = smithing;
        this.bartering = bartering;

        this.combatLevel = attack+defence+strength;
        this.skillLevel = mining+woodcutting+smithing+bartering;

        this.totalLevel = combatLevel + skillLevel + hitpoints;
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
    public String toString() {
        return "Character{" +
                "characterName='" + characterName + '\'' +
                ", gender='" + gender + '\'' +
                ", balance=" + balance +
                ", totalLevel=" + totalLevel +
                ", combatLevel=" + combatLevel +
                ", skillLevel=" + skillLevel +
                ", attack=" + attack +
                ", defence=" + defence +
                ", strength=" + strength +
                ", hitpoints=" + hitpoints +
                ", mining=" + mining +
                ", woodcutting=" + woodcutting +
                ", smithing=" + smithing +
                ", bartering=" + bartering +
                '}';
    }
}
