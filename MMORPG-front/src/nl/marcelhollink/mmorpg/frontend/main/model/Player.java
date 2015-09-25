package nl.marcelhollink.mmorpg.frontend.main.model;

public class Player {

    String username;
    String firstName,lastName;

    double balance;
    String lastPayment;
    int monthsPayedFor;

    int characterSlots;

    String iban;

    String password = null;

    public Player(String username, String firstName, String lastName, double balance, String lastPayment, int monthsPayedFor, int characterSlots, String iban) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.lastPayment = lastPayment;
        this.monthsPayedFor = monthsPayedFor;;
        this.characterSlots = characterSlots;
        this.iban = iban;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
