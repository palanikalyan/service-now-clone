package com.requestmanagement.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

/**
 * Run this once to generate a secure JWT secret key
 */
public class GenerateJwtSecret {
    public static void main(String[] args) {
        // Generate a secure key for HS256
        byte[] secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();

        // Encode to Base64 for easy storage in properties file
        String base64Secret = Base64.getEncoder().encodeToString(secretKey);

        System.out.println("Generated secure JWT secret (Base64 encoded):");
        System.out.println(base64Secret);
        System.out.println("\nAdd this to your application.properties:");
        System.out.println("app.jwtSecret=" + base64Secret);
        System.out.println("\nKey length: " + (secretKey.length * 8) + " bits");
    }
}