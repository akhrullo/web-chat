package com.akhrullo.webchat.encryption;

import com.akhrullo.webchat.encryption.keymanagement.KeyManagementService;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Service to handle message encryption and decryption using hybrid cryptography
 * (AES for message encryption, RSA for key encryption).
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageEncryptionService {

    private final KeyManagementService keyManagementService;
    private static final String AES_ALGORITHM = "AES";
    private static final String RSA_ALGORITHM = "RSA";
    private static final int AES_KEY_SIZE = 256;

    /**
     * Generates a new AES-256 key for encrypting messages.
     *
     * @return SecretKey AES key for encryption.
     */
    public SecretKey generateAesKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGen.init(AES_KEY_SIZE); // AES-256
            return keyGen.generateKey();
        } catch (Exception e) {
            log.error("Failed to generate AES key.", e);
            throw new WebChatApiException("AES key generation failed.");
        }
    }

    /**
     * Encrypts a message using AES-256 symmetric encryption.
     *
     * @param message The plaintext message to encrypt.
     * @param aesKey The AES key used for encryption.
     * @return Encrypted message as a Base64-encoded string.
     */
    public String encryptMessageWithAes(String message, SecretKey aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("AES encryption failed.", e);
            throw new WebChatApiException("AES encryption failed.");
        }
    }

    /**
     * Decrypts a message using AES-256 symmetric decryption.
     *
     * @param encryptedMessage The Base64-encoded encrypted message.
     * @param aesKey The AES key used for decryption.
     * @return The decrypted plaintext message.
     */
    public String decryptMessageWithAes(String encryptedMessage, SecretKey aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.error("AES decryption failed.", e);
            throw new WebChatApiException("AES decryption failed.");
        }
    }

    /**
     * Encrypts an AES key using the recipient's RSA public key (asymmetric encryption).
     *
     * @param aesKey The AES key to encrypt.
     * @param publicKey The recipient's RSA public key.
     * @return Encrypted AES key as a Base64-encoded string.
     */
    public String encryptAesKeyWithRsa(SecretKey aesKey, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedKey = cipher.doFinal(aesKey.getEncoded());
            return Base64.getEncoder().encodeToString(encryptedKey);
        } catch (Exception e) {
            log.error("RSA encryption of AES key failed.", e);
            throw new WebChatApiException("RSA encryption of AES key failed.");
        }
    }

    /**
     * Decrypts an AES key using the recipient's RSA private key (asymmetric decryption).
     *
     * @param encryptedAesKey The Base64-encoded encrypted AES key.
     * @param privateKey The recipient's RSA private key.
     * @return The decrypted AES key.
     */
    public SecretKey decryptAesKeyWithRsa(String encryptedAesKey, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedKeyBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedAesKey));
            return new SecretKeySpec(decryptedKeyBytes, AES_ALGORITHM);
        } catch (Exception e) {
            log.error("RSA decryption of AES key failed.", e);
            throw new WebChatApiException("RSA decryption of AES key failed.");
        }
    }

    /**
     * Encrypts a message for a specific recipient by combining AES encryption (for the message)
     * and RSA encryption (for the AES key).
     *
     * @param message The plaintext message to encrypt.
     * @param recipient The user who will receive the encrypted message.
     * @return A combined string containing the encrypted AES key and the encrypted message.
     */
    public String encryptMessageForUser(String message, User recipient) {
        // Generate a new AES-256 key for this message
        SecretKey aesKey = generateAesKey();

        // Encrypt the message using AES-256
        String encryptedMessage = encryptMessageWithAes(message, aesKey);

        // Retrieve the recipient's RSA public key
        PublicKey publicKey = keyManagementService.getPublicKeyForUser(recipient);

        // Encrypt the AES key using the recipient's RSA public key
        String encryptedAesKey = encryptAesKeyWithRsa(aesKey, publicKey);

        // Send both encrypted AES key and encrypted message to the recipient
        return encryptedAesKey + ":" + encryptedMessage;
    }

    /**
     * Decrypts a message for a specific recipient by first decrypting the AES key using RSA,
     * and then using the AES key to decrypt the message.
     *
     * @param encryptedData The combined string containing the encrypted AES key and the encrypted message.
     * @param recipient The user receiving the message.
     * @return The decrypted plaintext message.
     */
    public String decryptMessageForUser(String encryptedData, User recipient) {
        // Split the encrypted data into encrypted AES key and encrypted message
        String[] parts = encryptedData.split(":");
        String encryptedAesKey = parts[0];
        String encryptedMessage = parts[1];

        // Retrieve the recipient's RSA private key
        PrivateKey privateKey = keyManagementService.getPrivateKeyForUser(recipient);

        // Decrypt the AES key using the recipient's RSA private key
        SecretKey aesKey = decryptAesKeyWithRsa(encryptedAesKey, privateKey);

        // Decrypt the message using the decrypted AES key
        return decryptMessageWithAes(encryptedMessage, aesKey);
    }
}