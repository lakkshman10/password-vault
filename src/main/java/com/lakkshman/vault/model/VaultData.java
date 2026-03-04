package main.java.com.lakkshman.vault.model;

import java.util.ArrayList;
import java.util.List;

public class VaultData {

    private final List<Credential> credentials;

    public VaultData() {
        this.credentials = new ArrayList<>();
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void addCredential(Credential credential) {
        this.credentials.add(credential);
    }
}