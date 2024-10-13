package com.akhrullo.webchat.filestorage;

import com.akhrullo.webchat.encryption.EncryptionService;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {
    private static final String STORAGE_PATH = "storage";

    private final EncryptionService encryptionService;

    public String saveFile(@NotNull MultipartFile file, String fileName, User receiver) {
        Path rootPath = getGeneratedPath(fileName);
        String encryptedFilePath = encryptionService.encryptForUser(rootPath.toString(), receiver);

        try {
            Files.copy(file.getInputStream(), rootPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error occurred while saving file [{}] at [{}]: {}", fileName, rootPath, e.getMessage());
            throw new WebChatApiException("Error saving file");
        }

        return encryptedFilePath;
    }

    public FileSystemResource loadFile(@NotNull String encryptedFilePath, User user) {
        String decryptedFilename;

        try {
            decryptedFilename = encryptionService.decryptForUser(encryptedFilePath, user);
            log.info("File [{}] decrypted successfully for user [{}]", encryptedFilePath, user.getId());
        } catch (Exception e) {
            log.error("Error occurred while decrypting file [{}] for user [{}]: {}", encryptedFilePath, user.getId(), e.getMessage(), e);
            throw new WebChatApiException("Error decrypting file");
        }

        FileSystemResource resource = new FileSystemResource(decryptedFilename);

        if (!resource.exists()) {
            log.warn("File [{}] not found after decryption", decryptedFilename);
            throw new WebChatApiException("File not found");
        }

        return resource;
    }

    public Path getGeneratedPath(String generatedName) {
        Path dailyPath = getPaths();
        if (!Files.exists(dailyPath)) {
            try {
                Files.createDirectories(dailyPath);
            } catch (IOException e) {
                log.error("Error occurred while creating directory [{}]: {}", dailyPath, e.getMessage(), e);
                throw new WebChatApiException("Error creating directory");
            }
        }
        return dailyPath.resolve(generatedName);
    }

    private Path getPaths() {
        LocalDate now = LocalDate.now();
        return Paths.get(STORAGE_PATH,
                String.valueOf(now.getYear()),
                String.valueOf(now.getMonthValue()),
                String.valueOf(now.getDayOfMonth()));
    }
}
