package main.java.com.lakkshman.vault.storage;

import com.google.gson.Gson;
import main.java.com.lakkshman.vault.model.VaultData;
import main.java.com.lakkshman.vault.security.KeyManager;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Arrays;

public class VaultStorage {

    public static void saveVault(VaultData vault,
                                 SecretKey key,
                                 byte[] salt,
                                 Path path) throws Exception {

        String json = new Gson().toJson(vault);
        byte[] plainBytes = json.getBytes();

        // Generate IV (12 bytes for GCM)
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(plainBytes);

        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {

            fos.write(salt);       // 16 bytes
            fos.write(iv);         // 12 bytes
            fos.write(encrypted);  // remaining bytes
        }
    }

    public static VaultData loadVault(String masterPassword,
                                      Path path) throws Exception {

        if (!Files.exists(path)) {
            return null;
        }

        byte[] fileBytes = Files.readAllBytes(path);

        // Extract salt (first 16 bytes)
        byte[] salt = Arrays.copyOfRange(fileBytes, 0, 16);

        // Extract IV (next 12 bytes)
        byte[] iv = Arrays.copyOfRange(fileBytes, 16, 28);

        // Extract encrypted data
        byte[] encrypted = Arrays.copyOfRange(fileBytes, 28, fileBytes.length);

        // Recreate key using stored salt
        KeyManager keyManager = new KeyManager();
        keyManager.unlockVault(masterPassword, salt);
        SecretKey key = keyManager.getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        try {
            byte[] decrypted = cipher.doFinal(encrypted);
            String json = new String(decrypted);
            return new Gson().fromJson(json, VaultData.class);

        } catch (AEADBadTagException e) {
            throw new RuntimeException("Invalid master password!");
        }
    }
}