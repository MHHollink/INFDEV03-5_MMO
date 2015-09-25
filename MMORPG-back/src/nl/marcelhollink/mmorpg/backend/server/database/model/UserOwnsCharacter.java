package nl.marcelhollink.mmorpg.backend.server.database.model;

import javax.persistence.*;

@Entity
@Table(name = "user_owns_character", schema = "public")
public class UserOwnsCharacter {


    @Column(name = "username", nullable = false)
    private String username;

    @Id
    @Column(name = "character", nullable = false, unique = true)
    private String character;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
