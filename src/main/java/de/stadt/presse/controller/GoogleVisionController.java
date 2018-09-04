package de.stadt.presse.controller;

import de.stadt.presse.entity.RequestsTable;
import de.stadt.presse.service.GoogleVisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vision")
public class GoogleVisionController {

  @Autowired
  GoogleVisionService googleVisionService;

  @PostMapping("/get")
  public void post() {
    googleVisionService.findAllImagesWithoutKeywords();
  }

}
