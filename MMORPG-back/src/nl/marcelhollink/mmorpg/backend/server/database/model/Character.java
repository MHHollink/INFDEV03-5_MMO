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

    @Column(name = "balance", nullable = false)
    private double balance;

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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
