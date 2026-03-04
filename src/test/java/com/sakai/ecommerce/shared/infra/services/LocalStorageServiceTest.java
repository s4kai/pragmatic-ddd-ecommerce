package com.sakai.ecommerce.shared.infra.services;

import com.sakai.ecommerce.shared.application.FileUpload;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalStorageServiceTest {

    private LocalStorageService service;
    private Path uploadsDir;

    @BeforeEach
    void setUp() throws IOException {
        service = new LocalStorageService();
        uploadsDir = Paths.get("uploads");
        Files.createDirectories(uploadsDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (!Files.exists(uploadsDir)) return;
        
        try (var paths = Files.walk(uploadsDir)) {
            paths.sorted()
                 .forEach(this::clearPaths);
        }
    }

    private void clearPaths(Path path){
        try { Files.delete(path); }
        catch (IOException ignored) {}
    }

    @Test
    void shouldStoreFile() {
        var file = new FileUpload("test.txt", () -> new ByteArrayInputStream("test content".getBytes()), "text/plain", 12);

        var filename = service.store(file);

        assertNotNull(filename);
        assertTrue(filename.contains("test.txt"));
        assertTrue(Files.exists(uploadsDir.resolve(filename)));
    }

    @Test
    void shouldStoreMultipleFiles() {
        var file1 = new FileUpload("file1.txt", () -> new ByteArrayInputStream("content1".getBytes()), "text/plain", 8);
        var file2 = new FileUpload("file2.txt", () -> new ByteArrayInputStream("content2".getBytes()), "text/plain", 8);

        var filenames = service.storeAll(List.of(file1, file2));

        assertEquals(2, filenames.size());
        filenames.forEach(filename -> assertTrue(Files.exists(uploadsDir.resolve(filename))));
    }

    @Test
    void shouldDeleteFile() {
        var file = new FileUpload("test.txt", () -> new ByteArrayInputStream("test".getBytes()), "text/plain", 4);
        var filename = service.store(file);

        service.delete(filename);

        assertFalse(Files.exists(uploadsDir.resolve(filename)));
    }

    @Test
    void shouldDeleteMultipleFiles() {
        var file1 = new FileUpload("file1.txt", () -> new ByteArrayInputStream("content1".getBytes()), "text/plain", 8);
        var file2 = new FileUpload("file2.txt", () -> new ByteArrayInputStream("content2".getBytes()), "text/plain", 8);
        var filenames = service.storeAll(List.of(file1, file2));

        service.deleteAll(filenames);

        filenames.forEach(filename -> assertFalse(Files.exists(uploadsDir.resolve(filename))));
    }

    @Test
    void shouldNotThrowWhenDeletingNonExistentFile() {
        assertDoesNotThrow(() -> service.delete("non-existent.txt"));
    }
}
