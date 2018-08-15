package de.stadt.presse.controller;



import de.stadt.presse.entity.Image;
import de.stadt.presse.entity.RequestsTable;
import de.stadt.presse.repository.RequestsTableRepository;
import de.stadt.presse.service.ImageService;
import de.stadt.presse.service.RequestsTableService;
import de.stadt.presse.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ImageController {

  @Autowired
    private
  ImageService imageService;

  @Autowired
    private
  StorageService storageService;

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
      imageService.callSDirs(folder,thumpPath,googleVisionLocalPath,scaleHeight,scaleHeightForGoogleVision,strText)
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
    return new ResponseEntity<>(imageService.callSDirs  (requestsTable.getFolder(),requestsTable.getThumpPath(),requestsTable.getGoogleVisionLocalPath(),requestsTable.getScaleHeight(),
      requestsTable.getScaleHeightForGoogleVision(),requestsTable.getStrText()), HttpStatus.OK);
  }

  @GetMapping("/findByKey")
  public ResponseEntity<List<Image>> get(@RequestParam("keywordEn") String keywordEn ) {
    List<Image> images = null;
    if (!(keywordEn == "")) {
      images = imageService.findByKeywords(keywordEn);
      return new ResponseEntity<List<Image>>(images, HttpStatus.OK);
    }
    return new ResponseEntity<List<Image>>(images, HttpStatus.OK);
  }

//
//  @GetMapping("/getallfiles")
//  public ResponseEntity<List<String>> getListFiles(Model model) {
//    List<String> fileNames = files
//      .stream().map(fileName -> MvcUriComponentsBuilder
//        .fromMethodName(ImageController.class, "getFile", path ,fileName).build().toString())
//      .collect(Collectors.toList());
//    System.out.println();
//
//    return ResponseEntity.ok().body(fileNames);
//  }

  @GetMapping("/files/{id:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile( @PathVariable("id") Long id){
    Optional<Image> image = imageService.findById(id);
    Image existImage = null;
    if(image.isPresent()){
      existImage = image.get();
      System.out.println(existImage);
    Resource file = storageService.loadFile(existImage.getImageThumpPath(),existImage.getImageName());
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
      .body(file);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }


}
