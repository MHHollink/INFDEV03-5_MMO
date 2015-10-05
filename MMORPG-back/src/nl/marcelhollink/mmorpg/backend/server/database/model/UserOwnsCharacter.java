package nl.marcelhollink.mmorpg.backend.server.database.model;

import nl.marcelhollink.mmorpg.backend.server.database.DatabaseSettings;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

import static nl.marcelhollink.mmorpg.backend.server.database.DatabaseSettings.*;

@Entity
@Table(name = USER_OWNS_CHARACTER_TABLE_NAME, schema = "public")
public class UserOwnsCharacter implements Serializable
{
    @Id
    @Cascade(CascadeType.ALL)
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = UOC_USERNAME, nullable = false)
    private User username;

    @Id
    @OneToOne(targetEntity = Character.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = UOC_CHARACTER, nullable = false, unique = true)
    private Character character;

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserOwnsCharacter uocObject = (UserOwnsCharacter) object;

        return !(username != null ? !username.equals(uocObject.username) : uocObject.username != null)
                && !(character != null ? !character.equals(uocObject.character) : uocObject.character != null);
    }

    @Override
    public int hashCode() {
        int result;
        result = (character !=null? character.hashCode() : 0);
        result =31* result + (username !=null? username.hashCode() : 0);
        return result;
    }

}
