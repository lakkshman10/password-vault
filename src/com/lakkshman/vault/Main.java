package com.lakkshman.vault;

import com.lakkshman.vault.model.Credential;
import com.lakkshman.vault.model.VaultData;
import com.lakkshman.vault.security.KeyManager;
import com.lakkshman.vault.storage.VaultStorage;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("vault.dat");
        VaultData vault;
        try {
            vault = VaultStorage.loadVault("MyMasterPassword", path);
        } catch (RuntimeException e) {
            System.out.println("❌ Wrong master password!");
            return;
        }

        if (vault == null) {
            vault = new VaultData();
        }

        Credential gmail = new Credential(
                "Gmail",
                "lakkshman@gmail.com",
                "myPassword123"
        );

        vault.addCredential(gmail);

        // We need salt from key manager only if creating new vault
        KeyManager keyManager = new KeyManager();
        keyManager.initializeVault("MyMasterPassword");

        VaultStorage.saveVault(
                vault,
                keyManager.getSecretKey(),
                keyManager.getSalt(),
                path
        );

        System.out.println("Vault updated.");
        System.out.println("Existing credentials count: "
                + vault.getCredentials().size());
    }
}