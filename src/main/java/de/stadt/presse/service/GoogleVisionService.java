package de.stadt.presse.service;

import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.util.KeywordDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;


@Service
public class GoogleVisionService {

  @Autowired
  private KeywordDetection keywordDetection;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ImageRepository imageRepository;

  private String googleVisonFileLocation = "D:\\googleVision\\";

  private Set<String> keywords = new HashSet<>();


  private Set<Image> images = new HashSet<>();


  public void findAllImagesWithoutKeywords() {
    images = imageRepository.findByImageHaveMetadata(false);
    forEachImage();
  }

  public void forEachImage() {
    images.forEach(image -> {
      getKeywords(googleVisonFileLocation + image.getImageName(),image);
    });
  }


  public void getKeywords(String imageLocation,Image image) {
    Path imagePath = Paths.get(imageLocation);

    try {
      keywords = keywordDetection.startDetection(imagePath);
      imageService.addKeywordsForEach(keywords,image);
      image.setImageHaveMetadata(true);
      String keywordsString =image.getImageAllKeywords()+";"+ String.join(";",keywords) ;
      image.setImageAllKeywords(keywordsString);
      imageService.save(image);
    } catch (IOException | GeneralSecurityException e) {
      e.printStackTrace();
    }
  }
}
