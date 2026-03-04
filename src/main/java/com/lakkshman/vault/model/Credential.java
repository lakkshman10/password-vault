package main.java.com.lakkshman.vault.model;

import java.util.UUID;

public class Credential {

    private UUID id;
    private String serviceName;
    private String username;
    private String password;

    public Credential() {
        this.id = UUID.randomUUID();
    }

    public Credential(String serviceName, String username, String password) {
        this.id = UUID.randomUUID();
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}