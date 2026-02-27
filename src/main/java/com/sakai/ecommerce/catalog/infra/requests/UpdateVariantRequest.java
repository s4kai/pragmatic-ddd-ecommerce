package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.UpdateVariantCommand;
import com.sakai.ecommerce.shared.infra.mapper.UploadFileMapper;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UpdateVariantRequest(
        UUID variantId,
        String sku,
        String name,
        MultipartFile coverImage,
        BigDecimal price,
        String currency,
        List<MultipartFile> gallery,
        Map<String, Object> details
) {
    public UpdateVariantCommand toCommand() {
        return new UpdateVariantCommand(
                variantId,
                sku,
                name,
                coverImage == null ? null : UploadFileMapper.toFileUpload(coverImage),
                price,
                currency,
                gallery == null ? List.of() : gallery.stream()
                        .map(UploadFileMapper::toFileUpload)
                        .toList(),
                details
        );
    }
}
