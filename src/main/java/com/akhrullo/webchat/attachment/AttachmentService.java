package com.akhrullo.webchat.attachment;

import com.akhrullo.webchat.user.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for handling attachment-related operations.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface AttachmentService {

    /**
     * Saves the given file as an attachment for the specified receiver.
     *
     * @param file the file to be saved
     * @param receiver the user who will receive the attachment
     * @return the saved attachment as a DTO
     */
    AttachmentDto saveAttachment(MultipartFile file, User receiver);

    /**
     * Retrieves the attachment with the given ID, decrypting it for the specified user.
     *
     * @param id the ID of the attachment to be retrieved
     * @param user the user who is allowed to access the attachment
     * @return the retrieved attachment as a DTO
     */
    AttachmentDto getAttachment(Long id, User user);
}
