package com.sakai.ecommerce.shared.infra.mapper;

import com.sakai.ecommerce.shared.application.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Supplier;

public class UploadFileMapper {
    public static FileUpload toFileUpload(MultipartFile file) {
        try {
            return new FileUpload(
                    file.getOriginalFilename(),
                    UploadFileMapper.getFileSupplier(file),
                    file.getContentType(),
                    file.getSize()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to process file upload: " + file.getOriginalFilename(), e);
        }
    }

    private static Supplier<InputStream> getFileSupplier(MultipartFile file){
        Objects.requireNonNull(file, "file must not be null");

        final String filename = file.getOriginalFilename();

        return () -> {
            try { return file.getInputStream(); }
            catch (IOException e) {
                throw new UncheckedIOException("Unable to open InputStream for file: " + filename, e);
            }
        };
    }
}
