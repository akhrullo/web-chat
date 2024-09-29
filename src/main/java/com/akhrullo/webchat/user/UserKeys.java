package com.akhrullo.webchat.user;

import com.akhrullo.webchat.model.audit.AuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the public and encrypted private key pair for a user.
 * This entity stores the RSA public key and the AES-encrypted private key.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Entity
@Table(name = "user_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserKeys extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "encrypted_private_key", nullable = false, columnDefinition = "TEXT")
    private String encryptedPrivateKey;
}
