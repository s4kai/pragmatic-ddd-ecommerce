package com.sakai.ecommerce.shared.infra.services;

import com.sakai.ecommerce.shared.application.FileUpload;
import com.sakai.ecommerce.shared.application.services.StorageService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    @Override
    public String store(FileUpload file) {

        Path target = Paths.get("uploads")
                .resolve(UUID.randomUUID() + "_" + file.filename());

        try (InputStream in = file.content().get()) {
            Files.copy(in, target);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        return target.getFileName().toString();
    }

    @Override
    public List<String> storeAll(List<FileUpload> files) {
        return files.stream()
                .map(this::store)
                .toList();
    }

    @Override
    public void delete(String filename) {
        try {
            Files.deleteIfExists(Paths.get("uploads").resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll(List<String> filenames) {
        filenames.forEach(this::delete);
    }
}
