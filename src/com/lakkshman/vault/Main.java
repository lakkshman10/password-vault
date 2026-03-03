package com.lakkshman.vault;

import com.lakkshman.vault.cli.VaultCLI;
import com.lakkshman.vault.model.VaultData;
import com.lakkshman.vault.security.KeyManager;
import com.lakkshman.vault.service.VaultService;
import com.lakkshman.vault.storage.VaultStorage;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Path vaultPath = Path.of("vault.dat");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter master password: ");
        String masterPassword = scanner.nextLine();

        VaultData vault;
        SecretKey secretKey;
        byte[] salt;

        if (Files.exists(vaultPath)) {

            vault = VaultStorage.loadVault(masterPassword, vaultPath);

            byte[] fileBytes = Files.readAllBytes(vaultPath);
            salt = new byte[16];
            System.arraycopy(fileBytes, 0, salt, 0, 16);

            KeyManager keyManager = new KeyManager();
            keyManager.unlockVault(masterPassword, salt);
            secretKey = keyManager.getSecretKey();

            System.out.println("Vault unlocked successfully.");

        } else {

            System.out.println("Creating new vault...");
            vault = new VaultData();

            KeyManager keyManager = new KeyManager();
            keyManager.initializeVault(masterPassword);
            secretKey = keyManager.getSecretKey();
            salt = keyManager.getSalt();
        }

        VaultService service =
                new VaultService(vault, secretKey, salt, vaultPath);

        VaultCLI cli = new VaultCLI(service);
        cli.start();
    }
}