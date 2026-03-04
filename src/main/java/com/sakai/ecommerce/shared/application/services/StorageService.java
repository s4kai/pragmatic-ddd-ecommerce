package com.sakai.ecommerce.shared.application.services;

import com.sakai.ecommerce.shared.application.FileUpload;

import java.util.List;

public interface StorageService {
    String store(FileUpload file);
    List<String> storeAll(List<FileUpload> files);
    void delete(String filename);
    void deleteAll(List<String> filenames);
}
