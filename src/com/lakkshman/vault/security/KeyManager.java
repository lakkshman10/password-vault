package com.lakkshman.vault.security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class KeyManager {

    private SecretKey secretKey;
    private byte[] salt;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128; // bits

    public void initializeVault(String masterPassword) {
        try {
            // Generate random salt (16 bytes)
            salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            deriveKey(masterPassword);

        } catch (Exception e) {
            throw new RuntimeException("Error initializing vault", e);
        }
    }
    public void unlockVault(String masterPassword, byte[] storedSalt) {
        try {
            this.salt = storedSalt;
            deriveKey(masterPassword);

        } catch (Exception e) {
            throw new RuntimeException("Error unlocking vault", e);
        }
    }

    private void deriveKey(String masterPassword) throws Exception {

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec spec = new PBEKeySpec(
                masterPassword.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getSalt() {
        return salt;
    }
}