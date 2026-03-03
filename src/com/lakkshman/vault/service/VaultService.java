package com.lakkshman.vault.service;

import com.lakkshman.vault.model.Credential;
import com.lakkshman.vault.model.VaultData;
import com.lakkshman.vault.storage.VaultStorage;

import javax.crypto.SecretKey;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VaultService {

    private VaultData vaultData;
    private final SecretKey secretKey;
    private final byte[] salt;
    private final Path vaultPath;

    public VaultService(VaultData vaultData,
                        SecretKey secretKey,
                        byte[] salt,
                        Path vaultPath) {

        this.vaultData = vaultData;
        this.secretKey = secretKey;
        this.salt = salt;
        this.vaultPath = vaultPath;
    }

    public void addCredential(String service, String username, String password) {
        vaultData.getCredentials().add(
                new Credential(service, username, password)
        );
    }

    public List<Credential> getAllCredentials() {
        return new ArrayList<>(vaultData.getCredentials());
    }

    public Credential findByService(String service) {
        return vaultData.getCredentials()
                .stream()
                .filter(c -> c.getServiceName().equalsIgnoreCase(service))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteCredential(String service) {
        return vaultData.getCredentials()
                .removeIf(c -> c.getServiceName().equalsIgnoreCase(service));
    }

    public void save() {
        try {
            VaultStorage.saveVault(vaultData, secretKey, salt, vaultPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save vault", e);
        }
    }
}