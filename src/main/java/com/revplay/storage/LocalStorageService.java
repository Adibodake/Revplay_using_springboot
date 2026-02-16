package com.revplay.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path audioDir;
    private final Path imageDir;

    public LocalStorageService(
            @Value("${app.storage.audio-dir}") String audioDir,
            @Value("${app.storage.image-dir}") String imageDir
    ) {
        this.audioDir = Paths.get(audioDir).toAbsolutePath().normalize();
        this.imageDir = Paths.get(imageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.audioDir);
            Files.createDirectories(this.imageDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directories", e);
        }
    }

    @Override
    public String saveAudio(MultipartFile file) {
        return save(file, audioDir, "audio");
    }

    @Override
    public String saveImage(MultipartFile file) {
        return save(file, imageDir, "image");
    }

    private String save(MultipartFile file, Path dir, String type) {
        if (file == null || file.isEmpty()) return null;

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";

        int dot = original.lastIndexOf(".");
        if (dot >= 0) ext = original.substring(dot);

        String filename = UUID.randomUUID() + ext;

        try {
            Path target = dir.resolve(filename).normalize();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            // We will serve files via /files/** endpoints:
            return type.equals("audio")
                    ? "/files/audio/" + filename
                    : "/files/images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + original, e);
        }
    }
}
