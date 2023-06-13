package com.example.healthyplus.Database;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public PasswordHasher() {
    }

    public String hashPassword(String password) {
        try {
            // Create an instance of the SHA-256 hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the password string to bytes
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            // Return the hashed password as a string
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
