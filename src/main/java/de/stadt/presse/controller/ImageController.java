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
  public boolean scanDiryctory(
                               //  D:\pics\Fotolia\Gesundheit & Essen & Sport
                               @RequestParam("folder") String folder ,
//                              d:/thump
                               @RequestParam("thumpPath") String thumpPath,
//                               d:/googleVision
                               @RequestParam("googleVisionLocalPath") String googleVisionLocalPath,
//                                150
                               @RequestParam("scaleHeight") Integer scaleHeight,
                               @RequestParam("scaleHeightForGoogleVision") Integer scaleHeightForGoogleVision,
//                               some Text "Oldenburg"
                               @RequestParam("strText") String strText) {
    return imageService.scanDirs(folder,thumpPath,googleVisionLocalPath,scaleHeight,scaleHeightForGoogleVision,strText);

  }


}
