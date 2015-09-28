package nl.marcelhollink.mmorpg.backend.server.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "servers", schema = "public")
public class Server  implements Serializable {

    @Id
    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "name", nullable = false, unique = true)
    private String serverName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "max_users", nullable = false)
    private int maximumUsers;

    @Column(name = "current_users", nullable = false)
    private int connectedUsers;

    @Column(name = "active", nullable = false)
    private boolean active;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaximumUsers() {
        return maximumUsers;
    }

    public void setMaximumUsers(int maximumUsers) {
        this.maximumUsers = maximumUsers;
    }

    public int getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(int connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
