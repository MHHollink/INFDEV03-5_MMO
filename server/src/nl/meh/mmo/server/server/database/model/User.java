package nl.meh.mmo.server.server.database.model;

import javax.persistence.*;
import java.io.Serializable;

import static nl.meh.mmo.server.server.database.DatabaseSettings.*;

@Entity
@Table(name = USER_TABLE_NAME
        , indexes = {@Index(name = "users_index", columnList = USER_COLUMN_USERNAME)}
)
public class User implements Serializable{

    @Id
    @Column(name = USER_COLUMN_USERNAME, unique = true, nullable = false)
    private String username;

    @Column(name = USER_COLUMN_FIRST_NAME, nullable = false)
    private String fName;

    @Column(name = USER_COLUMN_LAST_NAME, nullable = false)
    private String lName;

    @Column(name = USER_COLUMN_SLOTS, nullable = false)
    private int slots;

    @Column(name = USER_COLUMN_BALANCE, nullable = false)
    private double balance;

    @Column(name = USER_COLUMN_LAST_PAYED)
    private String lastPayment;

    @Column(name = USER_COLUMN_DAYS_LEFT)
    private int daysLeft;

    @Column(name = USER_COLUMN_IBAN)
    private String iban;

    @Column(name = USER_COLUMN_PASSWORD, nullable = false)
    private String password;

    public User(String username, String fName, String lName, int slots, double balance, String lastPayment, int daysLeft, String iban, String password) {
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.slots = slots;
        this.balance = balance;
        this.lastPayment = lastPayment;
        this.daysLeft = daysLeft;
        this.iban = iban;
        this.password = password;
    }

    public User() {
    }

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

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(String lastPayment) {
        this.lastPayment = lastPayment;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
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
