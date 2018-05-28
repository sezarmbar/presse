package de.stadt.presse.controller;



import de.stadt.presse.entity.Image;
import de.stadt.presse.exceptions.ResourceNotFoundException;
import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

//  // Update a Image
//  @PutMapping("/image/{id}")
//  public Image updateImage(@PathVariable(value = "id") Long imageId,
//                         @Valid @RequestBody Image imageDetails) {
//
//    Image image = imageRepository.findById(imageId)
//      .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
//
//    image.setImageKeywords(imageDetails.getImageKeywords());
//
//    return imageRepository.save(image);
//  }

  @DeleteMapping("/image/{id}")
  public ResponseEntity<?> deleteImage(@PathVariable(value = "id") Long imageId) {
    Image image = imageRepository.findById(imageId)
      .orElseThrow(() -> new ResourceNotFoundException("Image!", "ID", imageId));

    imageRepository.delete(image);

    return ResponseEntity.ok().build();
  }
}
