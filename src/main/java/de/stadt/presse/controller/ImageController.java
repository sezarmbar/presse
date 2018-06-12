package de.stadt.presse.controller;



import de.stadt.presse.entity.Image;
import de.stadt.presse.exceptions.ResourceNotFoundException;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.util.ScanDirs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageController {

  @Autowired
  private
  ImageRepository imageRepository;

  // Get All Images
  @GetMapping("/image")
  public List<Image> getAllImages() {
    return imageRepository.findAll();
  }

  @GetMapping("/scan")
  public void scanDiryctory() {
    String path = "d:/pics/Fotolia/Baustellen";
    String thumpPath = "d:/thump";
    int scaleHeight = 200;

    File folder = new File(path);
    ScanDirs readDirs= new ScanDirs();
    readDirs.scan(folder, thumpPath, scaleHeight);

  }

  // Create a new Image
  @PostMapping("/image")
  public Image createImage(@Valid @RequestBody Image image) {
    return imageRepository.save(image);
  }

  // Get a Single Image
  @GetMapping("/image/{id}")
  public Image getImageById(@PathVariable(value = "id") Long imageId) {
    return imageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
  }


  @DeleteMapping("/image/{id}")
  public ResponseEntity<?> deleteImage(@PathVariable(value = "id") Long imageId) {
    Image image = imageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Image!", "ID", imageId));

    imageRepository.delete(image);

    return ResponseEntity.ok().build();
  }
}
