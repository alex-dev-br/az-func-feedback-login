package br.com.fiap.feedback.core.domain;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String username;
    private String password;
    private UserType userType;

    public User(UUID uuid, String username, String password, UserType userType) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(String username, UserType userType) {
        this(null, username, null, userType);
    }

    public User(String username, String password) {
        this(null, username, password, null);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}
