package nl.meh.mmo.server.server.database.model;

import javax.persistence.*;
import java.io.Serializable;

import static nl.meh.mmo.server.server.database.DatabaseSettings.*;

@Entity
@Table(name = SERVER_TABLE_NAME, schema = "public"
        , indexes = {@Index(name = "server_index",columnList = SERVER_COLUMN_ADDRESS)}
)
public class Server  implements Serializable {

    @Id
    @Column(name = SERVER_COLUMN_ADDRESS, nullable = false, unique = true)
    private String address;

    @Column(name = SERVER_COLUMN_NAME, nullable = false, unique = true)
    private String serverName;

    @Column(name = SERVER_COLUMN_LOCAL, nullable = false)
    private String location;

    @Column(name = SERVER_COLUMN_MAX_USERS, nullable = false)
    private int maximumUsers;

    @Column(name = SERVER_COLUMN_CUR_USERS, nullable = false)
    private int connectedUsers;

    @Column(name = SERVER_COLUMN_ACTIVE, nullable = false)
    private boolean active;

    public Server(String address, String serverName, String location, int maximumUsers, int connectedUsers, boolean active) {
        this.address = address;
        this.serverName = serverName;
        this.location = location;
        this.maximumUsers = maximumUsers;
        this.connectedUsers = connectedUsers;
        this.active = active;
    }

    public Server() {
    }

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
