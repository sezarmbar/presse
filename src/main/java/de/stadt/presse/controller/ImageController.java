package de.stadt.presse.controller;



import de.stadt.presse.entity.Image;
import de.stadt.presse.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class ImageController {

  @Autowired
    private
  ImageService imageService;


  @PostMapping("/insert")
  public Image insert(@Valid @RequestBody Image image) {

    return imageService.save(image);
  }

  @PostMapping("/scan")
  public boolean scanDiryctory(@RequestParam("folder") String folder,
                               @RequestParam("thumpPath") String thumpPath,
                               @RequestParam("scaleHeight") Integer scaleHeight) {
    return imageService.scanDirs(folder,thumpPath,scaleHeight);

  }


}
