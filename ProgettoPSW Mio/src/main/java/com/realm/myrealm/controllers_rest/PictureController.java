package com.realm.myrealm.controllers_rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/immagini")
public class PictureController {

    @GetMapping("/{folder}/{path}")
    public ResponseEntity getPic(@PathVariable("folder") String folder, @PathVariable("path") String path) {
        var pic = new ClassPathResource("immagini/" + folder + "/" + path);
        String extension = path.substring(path.lastIndexOf('.') + 1);
        byte[] b;
        try {
            b = StreamUtils.copyToByteArray(pic.getInputStream());
        } catch (IOException e) {
            return new ResponseEntity("Picture not found", HttpStatus.BAD_REQUEST);
        }
        switch (extension)
        {
            case "jpg":return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(b);
            case "png":return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(b);
            default:return new ResponseEntity("Picture not found", HttpStatus.BAD_REQUEST);
        }
    }
}