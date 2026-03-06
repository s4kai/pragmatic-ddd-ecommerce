package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.UploadVariantImagesCommand;
import com.sakai.ecommerce.shared.infra.mapper.UploadFileMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record UploadVariantImagesRequest(
        String sku,
        MultipartFile coverImage,
        List<MultipartFile> gallery
) {
    public UploadVariantImagesCommand toCommand(UUID productId) {
        return new UploadVariantImagesCommand(
                productId,
                sku,
                (coverImage == null || coverImage.isEmpty()) ? null : UploadFileMapper.toFileUpload(coverImage),
                gallery == null ? List.of() : gallery.stream()
                        .filter(file -> file != null && !file.isEmpty())
                        .map(UploadFileMapper::toFileUpload)
                        .toList()
        );
    }
}
