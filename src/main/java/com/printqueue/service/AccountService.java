
package com.printqueue.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class AccountService {

    private final Properties accounts = new Properties();
    private final Path filePath = Paths.get(
            System.getProperty("user.home"),
            ".printqueue",
            "accounts.properties"
    );

    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    public AccountService() {
        loadAccounts();
    }

    private void loadAccounts() {
        try {
            Files.createDirectories(filePath.getParent());

            if (Files.exists(filePath)) {
                try (InputStream input = Files.newInputStream(filePath)) {
                    accounts.load(input);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load accounts.", e);
        }
    }

    private void saveAccounts() {
        try (OutputStream output = Files.newOutputStream(filePath)) {
            accounts.store(output, "PrintQueue Account Data");
        } catch (IOException e) {
            throw new RuntimeException("Unable to save accounts.", e);
        }
    }

    public boolean hasOwnerAccount() {
        return accounts.getProperty("owner.hash") != null;
    }

    public synchronized void setupOwner(String username, char[] password) {
        if (hasOwnerAccount()) {
            throw new IllegalStateException("Owner account already exists.");
        }

        validate(username, password);
        saveAccount("owner", username, password);
    }

    public synchronized void registerCustomer(String username, char[] password) {
        validate(username, password);

        String key = customerKey(username);

        if (accounts.getProperty(key + ".hash") != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        saveAccount(key, username, password);
    }

    public boolean authenticateOwner(String username, char[] password) {
        return authenticate("owner", username, password);
    }

    public boolean authenticateCustomer(String username, char[] password) {
        return authenticate(customerKey(username), username, password);
    }

    private void saveAccount(String key, String username, char[] password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            byte[] hash = hashPassword(password, salt);

            accounts.setProperty(key + ".username", username.trim());
            accounts.setProperty(key + ".salt",
                    Base64.getEncoder().encodeToString(salt));
            accounts.setProperty(key + ".hash",
                    Base64.getEncoder().encodeToString(hash));

            saveAccounts();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create account.", e);
        }
    }

    private boolean authenticate(String key, String username, char[] password) {
        try {
            String storedUsername = accounts.getProperty(key + ".username");
            String storedSalt = accounts.getProperty(key + ".salt");
            String storedHash = accounts.getProperty(key + ".hash");

            if (storedUsername == null || storedSalt == null || storedHash == null) {
                return false;
            }

            if (!storedUsername.equalsIgnoreCase(username.trim())) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(storedSalt);
            byte[] expectedHash = Base64.getDecoder().decode(storedHash);
            byte[] actualHash = hashPassword(password, salt);

            return java.security.MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        }
    }

    private byte[] hashPassword(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(
                password, salt, ITERATIONS, KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    private String customerKey(String username) {
        return "customer." + username.trim().toLowerCase();
    }

    private void validate(String username, char[] password) {
        if (username == null ||
                !username.trim().matches("[a-zA-Z0-9_]{3,20}")) {
            throw new IllegalArgumentException(
                    "Username must be 3-20 characters (letters, numbers or underscore)."
            );
        }

        if (password == null || password.length < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }
    }
}
