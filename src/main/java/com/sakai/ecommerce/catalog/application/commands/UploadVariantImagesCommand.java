package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.shared.application.FileUpload;

import java.util.List;
import java.util.UUID;

public record UploadVariantImagesCommand(
        UUID productId,
        String sku,
        FileUpload coverImage,
        List<FileUpload> gallery
) {}
