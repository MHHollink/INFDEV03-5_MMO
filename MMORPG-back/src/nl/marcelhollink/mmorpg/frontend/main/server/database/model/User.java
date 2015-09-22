package nl.marcelhollink.mmorpg.frontend.main.server.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String fName;

    @Column(name = "last_name", nullable = false)
    private String lName;

    @Column(name = "character_slots", nullable = false)
    private int slots;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "last_payment")
    private String lastPayment;

    @Column(name = "payed_until")
    private String payedUntil;

    @Column(name = "iban", unique = true)
    private String iban;

    @Column(name = "password", nullable = false)
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getPayedUntil() {
        return payedUntil;
    }

    public void setPayedUntil(String payedUntil) {
        this.payedUntil = payedUntil;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
