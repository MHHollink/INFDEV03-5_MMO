package nl.marcelhollink.mmorpg.frontend.main.server.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "servers", schema = "public")
public class Server {

    @Id
    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "name", nullable = false, unique = true)
    private String serverName;

    @Column(name = "local", nullable = false)
    private String location;

    @Column(name = "max_users", nullable = false)
    private int maximumUsers;

    @Column(name = "connected", nullable = false)
    private int connectedUsers;


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
}