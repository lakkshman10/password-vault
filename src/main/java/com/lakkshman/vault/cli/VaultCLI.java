package main.java.com.lakkshman.vault.cli;

import main.java.com.lakkshman.vault.model.Credential;
import main.java.com.lakkshman.vault.service.VaultService;

import java.util.List;
import java.util.Scanner;

public class VaultCLI {

    private final VaultService vaultService;
    private final Scanner scanner;

    public VaultCLI(VaultService vaultService) {
        this.vaultService = vaultService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("🔐 Welcome to Password Vault");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleAdd();
                case "2" -> handleList();
                case "3" -> handleSearch();
                case "4" -> handleDelete();
                case "5" -> {
                    vaultService.save();
                    System.out.println("Vault saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Add Credential");
        System.out.println("2. List Credentials");
        System.out.println("3. Search Credential");
        System.out.println("4. Delete Credential");
        System.out.println("5. Save & Exit");
        System.out.print("Enter choice: ");
    }

    private void handleAdd() {
        System.out.print("Service: ");
        String service = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        vaultService.addCredential(service, username, password);
        System.out.println("Credential added.");
    }

    private void handleList() {
        List<Credential> credentials = vaultService.getAllCredentials();

        if (credentials.isEmpty()) {
            System.out.println("No credentials found.");
            return;
        }

        for (Credential c : credentials) {
            System.out.println(c.getServiceName() + " | " + c.getUsername());
        }
    }

    private void handleSearch() {
        System.out.print("Enter service name: ");
        String service = scanner.nextLine();

        Credential credential = vaultService.findByService(service);

        if (credential == null) {
            System.out.println("Not found.");
        } else {
            System.out.println("Service: " + credential.getServiceName());
            System.out.println("Username: " + credential.getUsername());
            System.out.println("Password: " + credential.getPassword());
        }
    }

    private void handleDelete() {
        System.out.print("Enter service name to delete: ");
        String service = scanner.nextLine();

        boolean removed = vaultService.deleteCredential(service);

        if (removed) {
            System.out.println("Deleted successfully.");
        } else {
            System.out.println("Service not found.");
        }
    }
}