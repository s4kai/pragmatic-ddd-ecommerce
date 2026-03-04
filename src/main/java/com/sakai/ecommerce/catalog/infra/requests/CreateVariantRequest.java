package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.CreateVariantCommand;
import com.sakai.ecommerce.shared.infra.mapper.UploadFileMapper;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record CreateVariantRequest(
        String sku,
        String name,
        MultipartFile coverImage,
        BigDecimal price,
        String currency,
        List<MultipartFile> gallery,
        Map<String, Object> details
) {
    public CreateVariantCommand toCommand() {
        return new CreateVariantCommand(
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
