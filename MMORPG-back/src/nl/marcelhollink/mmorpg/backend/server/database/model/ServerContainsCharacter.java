package nl.marcelhollink.mmorpg.backend.server.database.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "server_contains_character", schema = "public")
public class ServerContainsCharacter implements Serializable {

    @Id
    @ManyToOne(targetEntity = Server.class)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "server_address", nullable = false)
    private Server address;

    @Id
    @OneToOne(targetEntity = Character.class)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "character_name", nullable = false, unique = true)
    private Character character;

    public Server getAddress() {
        return address;
    }

    public void setAddress(Server address) {
        this.address = address;
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

        ServerContainsCharacter sccObject = (ServerContainsCharacter) object;

        return !(address != null ? !address.equals(sccObject.address) : sccObject.address != null)
                && !(character != null ? !character.equals(sccObject.character) : sccObject.character != null);
    }

    @Override
    public int hashCode() {
        int result;
        result = (character !=null ? character.hashCode() : 0);
        result = 31 * result + (address !=null ? address.hashCode() : 0);
        return result;
    }

}
