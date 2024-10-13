package com.akhrullo.webchat.attachment;

import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.filestorage.FileStorageService;
import com.akhrullo.webchat.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * Implementation of {@link AttachmentService}
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentMapper mapper;
    private final FileStorageService fileStorageService;
    private final AttachmentRepository attachmentRepository;

    @Override
    public AttachmentDto saveAttachment(MultipartFile file, User receiver) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String generatedName = System.currentTimeMillis() + "." + extension;

        String savedFilePath = fileStorageService.saveFile(file, generatedName, receiver);

        Attachment attachment = Attachment.builder()
                .originalName(originalFilename)
                .generatedName(generatedName)
                .type(file.getContentType())
                .path(savedFilePath)
                .size(file.getSize())
                .build();

        attachmentRepository.save(attachment);

        return mapper.toDto(attachment);
    }

    @Override
    public AttachmentDto getAttachment(Long id, User user) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(WebChatApiException::attachmentNotFound);

        FileSystemResource resource = fileStorageService.loadFile(attachment.getPath(), user);
        AttachmentDto dto = mapper.toDto(attachment);
        dto.setPath(resource.getPath());

        return dto;
    }
}
