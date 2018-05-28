package de.stadt.presse.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/processing")
public class ImageProcessingController {


  // Create a new Image
  @PostMapping("/indexing")
  public void createImage(@Valid @RequestBody String path) {

  }
}
