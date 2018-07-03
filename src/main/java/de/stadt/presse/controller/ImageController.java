package de.stadt.presse.controller;



import de.stadt.presse.entity.Image;
import de.stadt.presse.entity.RequestsTable;
import de.stadt.presse.repository.RequestsTableRepository;
import de.stadt.presse.service.ImageService;
import de.stadt.presse.service.RequestsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class ImageController {

  @Autowired
    private
  ImageService imageService;

  @Autowired
  private
  RequestsTableService requestsTableService;

  @Autowired
  private
  RequestsTableRepository requestsTableRepository;

  @PostMapping("/insert")
  public Image insert(@Valid @RequestBody Image image) {

    return imageService.save(image);
  }

  @PostMapping("/scan")
  public ResponseEntity scanDiryctory(
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
    return new ResponseEntity<>(
      imageService.scanDirs(folder,thumpPath,googleVisionLocalPath,scaleHeight,scaleHeightForGoogleVision,strText)
      , HttpStatus.OK);

  }


  @PostMapping("/scanreq")
  public ResponseEntity<?> post(@RequestBody RequestsTable requestsTable, BindingResult result) {
    RequestsTable savedRequest = requestsTableService.save(requestsTable);
    if (result.hasErrors()) {
      savedRequest.setStatus(String.valueOf(HttpStatus.BAD_REQUEST));
      requestsTableService.save(savedRequest);
      return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
    }
    savedRequest.setStatus(String.valueOf(HttpStatus.OK));
    requestsTableService.save(savedRequest);
    return new ResponseEntity<>(imageService.scanDirs(requestsTable.getFolder(),requestsTable.getThumpPath(),requestsTable.getGoogleVisionLocalPath(),requestsTable.getScaleHeight(),
      requestsTable.getScaleHeightForGoogleVision(),requestsTable.getStrText()), HttpStatus.OK);
  }

}
