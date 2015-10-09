package nl.meh.mmo.server.server.database.model;

import javax.persistence.*;
import java.io.Serializable;

import static nl.meh.mmo.server.server.database.DatabaseSettings.*;

@Entity
@Table(name = CHARACTER_TABLE_NAME, schema = "public"
        , indexes = {@Index(name = "char_index", columnList = CHARACTER_COLUMN_NAME)}
)
public class Character implements Serializable{

    @Id
    @Column(name = CHARACTER_COLUMN_NAME, nullable = false, unique = true)
    private String characterName;

    @Column(name = CHARACTER_COLUMN_GENDER, nullable = false)
    private String gender;

    @Column(name = CHARACTER_COLUMN_BALANCE, nullable = false)
    private int balance;

    public Character(String characterName, String gender, int balance) {
        this.characterName = characterName;
        this.gender = gender;
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
}
