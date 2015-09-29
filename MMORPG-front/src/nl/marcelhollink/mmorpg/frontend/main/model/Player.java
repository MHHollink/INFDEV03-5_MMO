package nl.marcelhollink.mmorpg.frontend.main.model;

import java.util.ArrayList;

public class Player {

    String username;
    String firstName,lastName;

    double balance;
    String lastPayment;
    int monthsPayedFor;

    int characterSlots;

    String iban;

    private ArrayList<Character> characters;

    public Player(String username, String firstName, String lastName, double balance, String lastPayment, int monthsPayedFor, int characterSlots, String iban) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.lastPayment = lastPayment;
        this.monthsPayedFor = monthsPayedFor;;
        this.characterSlots = characterSlots;
        this.iban = iban;

        characters = new ArrayList<Character>();
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getBalance() {
        return balance;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public int getMonthsPayedFor() {
        return monthsPayedFor;
    }

    public int getCharacterSlots() {
        return characterSlots;
    }

    public String getIban() {
        return iban;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public void setMonthsPayedFor(int monthsPayedFor) {
        this.monthsPayedFor = monthsPayedFor;
    }

    public void setCharacterSlots(int characterSlots) {
        this.characterSlots = characterSlots;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void addCharacters(Character character) {
        characters.add(character);
    }

    @Override
    public String toString() {
        return "Player{" +
                "characters=" + characters +
                ", iban='" + iban + '\'' +
                ", characterSlots=" + characterSlots +
                ", monthsPayedFor=" + monthsPayedFor +
                ", lastPayment='" + lastPayment + '\'' +
                ", balance=" + balance +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
