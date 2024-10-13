package com.akhrullo.webchat.attachment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Attachment entities.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
