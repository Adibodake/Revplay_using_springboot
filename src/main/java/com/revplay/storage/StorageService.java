package com.revplay.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String saveAudio(MultipartFile file);
    String saveImage(MultipartFile file);
}
