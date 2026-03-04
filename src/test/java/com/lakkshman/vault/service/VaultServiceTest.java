package test.java.com.lakkshman.vault.service;

import main.java.com.lakkshman.vault.model.VaultData;
import main.java.com.lakkshman.vault.service.VaultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VaultServiceTest {

    private VaultService vaultService;

    @BeforeEach
    void setUp() {
        VaultData vaultData = new VaultData();
        vaultService = new VaultService(vaultData);
    }

    @Test
    void testAddCredential() {
        vaultService.addCredential("Gmail", "user1", "pass123");

        assertEquals(1, vaultService.getAllCredentials().size());
    }

    @Test
    void testFindByService() {
        vaultService.addCredential("GitHub", "dev", "secret");

        var credential = vaultService.findByService("github");

        assertNotNull(credential);
        assertEquals("dev", credential.getUsername());
    }

    @Test
    void testDeleteCredential() {
        vaultService.addCredential("Twitter", "user", "123");

        boolean removed = vaultService.deleteCredential("twitter");

        assertTrue(removed);
        assertEquals(0, vaultService.getAllCredentials().size());
    }

    @Test
    void testFindNonExistingService() {
        var credential = vaultService.findByService("unknown");

        assertNull(credential);
    }
}