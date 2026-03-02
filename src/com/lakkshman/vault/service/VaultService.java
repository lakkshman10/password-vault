package com.lakkshman.vault.service;

import com.lakkshman.vault.model.Credential;
import com.lakkshman.vault.model.VaultData;
import com.lakkshman.vault.security.KeyManager;
import com.lakkshman.vault.storage.VaultStorage;

import javax.crypto.SecretKey;
import java.io.Console;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class VaultService {

    private static final Path VAULT_PATH = Paths.get("vault.dat");

    private VaultData vault;
    private SecretKey secretKey;
    private byte[] salt;

    public void run() throws Exception {

        String masterPassword = readMasterPassword();
        unlockOrCreateVault(masterPassword);

        startMenuLoop();

        saveVault();
        System.out.println("Vault saved. Goodbye.");
    }

    private void unlockOrCreateVault(String masterPassword) throws Exception {

        if (Files.exists(VAULT_PATH)) {

            vault = VaultStorage.loadVault(masterPassword, VAULT_PATH);

            byte[] fileBytes = Files.readAllBytes(VAULT_PATH);
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
    }

    private void startMenuLoop() {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("\n==== PASSWORD VAULT ====");
            System.out.println("1. Add Credential");
            System.out.println("2. List Credentials");
            System.out.println("3. Search Credential");
            System.out.println("4. Delete Credential");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleAdd(scanner);
                    break;
                case "2":
                    handleList();
                    break;
                case "3":
                    handleSearch(scanner);
                    break;
                case "4":
                    handleDelete(scanner);
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void handleAdd(Scanner scanner) {

        System.out.print("Service Name: ");
        String service = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        addCredential(service, username, password);

        System.out.println("Credential added.");
    }
    private void handleList() {

        if (vault.getCredentials().isEmpty()) {
            System.out.println("No credentials stored.");
            return;
        }

        for (Credential c : vault.getCredentials()) {
            System.out.println("--------------------");
            System.out.println("Service : " + c.getServiceName());
            System.out.println("Username: " + c.getUsername());
            System.out.println("Password: " + c.getPassword());
        }
    }

    private void handleSearch(Scanner scanner) {

        System.out.print("Enter service name to search: ");
        String service = scanner.nextLine();

        for (Credential c : vault.getCredentials()) {
            if (c.getServiceName().equalsIgnoreCase(service)) {
                System.out.println("Found:");
                System.out.println("Username: " + c.getUsername());
                System.out.println("Password: " + c.getPassword());
                return;
            }
        }

        System.out.println("Service not found.");
    }

    private void handleDelete(Scanner scanner) {

        System.out.print("Enter service name to delete: ");
        String service = scanner.nextLine();

        vault.getCredentials().removeIf(
                c -> c.getServiceName().equalsIgnoreCase(service)
        );

        System.out.println("If existed, credential deleted.");
    }
    private String readMasterPassword() {

        Console console = System.console();

        if (console != null) {
            char[] passwordChars = console.readPassword("Enter master password: ");
            String password = new String(passwordChars);
            Arrays.fill(passwordChars, ' '); // wipe memory
            return password;
        } else {
            // Fallback for IDE
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter master password: ");
            return scanner.nextLine();
        }
    }

    public void addCredential(String serviceName,
                              String username,
                              String password) {

        Credential credential = new Credential(serviceName, username, password);
        vault.addCredential(credential);
    }

    private void saveVault() throws Exception {

        VaultStorage.saveVault(
                vault,
                secretKey,
                salt,
                VAULT_PATH
        );
    }
}