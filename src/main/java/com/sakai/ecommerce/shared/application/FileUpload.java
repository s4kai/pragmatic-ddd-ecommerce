package com.sakai.ecommerce.shared.application;

import java.io.InputStream;
import java.util.function.Supplier;

public record FileUpload(
        String filename,
        Supplier<InputStream> content,
        String contentType,
        long size
) {}
