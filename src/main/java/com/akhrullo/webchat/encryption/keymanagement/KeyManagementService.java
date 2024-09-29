package com.akhrullo.webchat.encryption.keymanagement;

import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.user.UserKeys;
import com.akhrullo.webchat.user.UserKeysRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Service responsible for generating, encrypting, and managing RSA key pairs
 * for users. It also handles AES encryption of private keys for secure storage.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeyManagementService {

    private final UserKeysRepository userKeysRepository;
    private static final String AES_ALGORITHM = "AES";

    @Value("${aes.master-key}")
    private String aesMasterKey;
    private SecretKey secretKey;

    private KeyPair keyPair;

    /**
     * Initializes the service by generating the AES key and RSA key pair.
     */
    @PostConstruct
    public void init() {
        this.secretKey = generateAESKey();
        this.keyPair = generateRsaKeyPair();
    }

    /**
     * Generates an RSA key pair (public and private keys).
     *
     * @return a {@link KeyPair} containing both the public and private RSA keys
     */
    private KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            log.error("Failed to generate RSA key pair: RSA algorithm not available.", e);
            throw new WebChatApiException("Failed to generate RSA key pair: RSA algorithm not available");
        }
    }

    /**
     * Generates an AES key from a base64-encoded master key.
     *
     * @return a {@link SecretKey} used for AES encryption
     */
    private SecretKey generateAESKey() {
        byte[] decodedKey = Base64.getDecoder().decode(aesMasterKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES_ALGORITHM);
    }

    /**
     * Generates RSA key pairs for a given user and stores them securely in the database.
     * The private key is encrypted using AES encryption.
     *
     * @param user the {@link User} for whom the key pair is generated
     */
    @Transactional
    public void generateAndStoreKeysForUser(User user) {
        // Encrypt private key
        byte[] encryptedPrivateKey = encryptPrivateKey(keyPair.getPrivate().getEncoded(), secretKey);

        // Save to DB
        UserKeys userKeys = new UserKeys();
        userKeys.setUser(user);
        userKeys.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        userKeys.setEncryptedPrivateKey(Base64.getEncoder().encodeToString(encryptedPrivateKey));

        userKeysRepository.save(userKeys);
    }

    /**
     * Encrypts the private RSA key using AES-256 encryption.
     *
     * @param privateKeyBytes the private key in byte format
     * @param aesKey          the AES key used for encryption
     * @return encrypted private key as a byte array
     */
    private byte[] encryptPrivateKey(byte[] privateKeyBytes, SecretKey aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return cipher.doFinal(privateKeyBytes);
        } catch (Exception e) {
            log.error("RSA encryption failed.", e);
            throw new WebChatApiException("RSA encryption failed: Unable to encrypt data.");
        }
    }

    /**
     * Retrieves and decrypts the private RSA key for a given user from the database.
     *
     * @param user the {@link User} for whom the private key is retrieved
     * @return the decrypted {@link PrivateKey}
     */
    @Transactional
    public PrivateKey getPrivateKeyForUser(User user) {
        UserKeys userKeys = userKeysRepository.findByUser(user)
                .orElseThrow(() -> new WebChatApiException("User keys not found"));

        // Decrypt the private key
        byte[] decryptedPrivateKeyBytes = decryptPrivateKey(
                Base64.getDecoder().decode(userKeys.getEncryptedPrivateKey()), secretKey);

        try {
            KeyFactory keyFactory = getRsa();
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decryptedPrivateKeyBytes));
        } catch (Exception e) {
            log.error("Failed to generate private key.", e);
            throw new WebChatApiException("Failed to generate private key");
        }
    }

    /**
     * Retrieves an RSA {@link KeyFactory} for generating RSA keys.
     *
     * @return an RSA {@link KeyFactory}
     */
    private KeyFactory getRsa() {
        try {
            return KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to initialize RSA keyFactory", e);
            throw new WebChatApiException("Failed to initialize RSA keyFactory");
        }
    }

    /**
     * Decrypts the private RSA key using AES-256 encryption.
     *
     * @param encryptedPrivateKeyBytes the encrypted private key in byte format
     * @param aesKey                   the AES key used for decryption
     * @return decrypted private key as a byte array
     */
    private byte[] decryptPrivateKey(byte[] encryptedPrivateKeyBytes, SecretKey aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            return cipher.doFinal(encryptedPrivateKeyBytes);
        } catch (Exception e) {
            log.error("RSA decryption failed.", e);
            throw new WebChatApiException("RSA decryption failed: Unable to decrypt data.");
        }
    }

    /**
     * Retrieves the public RSA key for a given user from the database.
     *
     * @param user the {@link User} for whom the public key is retrieved
     * @return the {@link PublicKey}
     */
    @Transactional
    public PublicKey getPublicKeyForUser(User user) {
        UserKeys userKeys = userKeysRepository.findByUser(user)
                .orElseThrow(() -> new WebChatApiException("User keys not found"));

        // Rebuild the PublicKey object
        byte[] publicKeyBytes = Base64.getDecoder().decode(userKeys.getPublicKey());

        try {
            KeyFactory keyFactory = getRsa();
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            log.error("Failed to generate public key.", e);
            throw new WebChatApiException("Failed to generate public key");
        }
    }
}

