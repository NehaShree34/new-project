
package com.example.imageupload.controller;

import com.example.imageupload.model.ImageFile;
import com.example.imageupload.repository.ImageFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageFileController {

    @Autowired
    private ImageFileRepository imageFileRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (imageFileRepository.count() >= 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Maximum 5 images allowed.");
        }

        ImageFile image = new ImageFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        imageFileRepository.save(image);
        return ResponseEntity.ok("Image uploaded successfully.");
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        List<Map<String, Object>> imageList = new ArrayList<>();
        imageFileRepository.findAll().forEach(img -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", img.getId());
            map.put("fileName", img.getFileName());
            map.put("url", "/api/images/" + img.getId());
            imageList.add(map);
        });
        return ResponseEntity.ok(imageList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<ImageFile> opt = imageFileRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        ImageFile image = opt.get();
        String fileType = (image.getFileType() == null || image.getFileType().isBlank())
                ? "application/octet-stream"
                : image.getFileType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileType))
                .body(image.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        imageFileRepository.deleteById(id);
        return ResponseEntity.ok("Image deleted.");
    }
}
