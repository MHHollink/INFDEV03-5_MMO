package nl.marcelhollink.mmorpg.backend.server.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "server_contains_character", schema = "public")
public class ServerContainsCharacter {

    @Column(name = "address", nullable = false)
    private String address;

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String character;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
