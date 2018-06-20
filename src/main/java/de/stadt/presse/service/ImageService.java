package de.stadt.presse.service;

import de.stadt.presse.entity.Image;
import de.stadt.presse.entity.Keyword;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.util.ImageProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageService {

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private KeywordsService keywordsService;

  public Image save(Image image) {
    return imageRepository.save(image);
  }

  public List<Image> findAll() {
    return imageRepository.findAll();
  }

  private String googleVisionLocalPath;

  public boolean scanDirs(String folderPath, String thumpPath, String googleVisionLocalPath, int scaleHeight, String strText) {
    this.googleVisionLocalPath = googleVisionLocalPath;
    File folder = new File(folderPath);
    scanDirectory(folder, thumpPath, scaleHeight, strText);
    return true;
  }
//https://www.callicoder.com/hibernate-spring-boot-jpa-many-to-many-mapping-example/

  private void scanDirectory(final File file, String thumpPath, int scaleHeight, String strText) {

    if (file.isDirectory()) {
      createDirectory(thumpPath + "/" + file.getName());
    } else if (file.isFile() && ImageProcessing.isImage(file.getPath())) {
      Image image = new Image();
      image.setImageName(file.getName());
      image.setImagePath(file.getPath());
      String metadataKeywords = readImageMetadata(file.getPath(), thumpPath + "/" + file.getName());

      if (metadataKeywords == "null") {
        image.setImageHaveMetadata(false);
        resizeForGoogleVision(file.getPath(), googleVisionLocalPath + "/" + file.getName(), 2000);
      } else {
        image.setImageHaveMetadata(true);
      }

      image.setImageAllKeywords(metadataKeywords + ";" + splitName(file.getName()));
      Set<String> keywordsSet = splitKeywordsToArray(metadataKeywords, ";");

      keywordsSet.forEach(key -> {
        Set<String> keySet = splitKeywordsToArray(key, ",");
        if (keySet.size() > 1) {
          keySet.forEach(subKey -> {
            addKeywordToImageSet(subKey, image);
          });
        } else {
          addKeywordToImageSet(key, image);
        }
      });

      image.setImageType(file.getName().substring(file.getName().indexOf(".") + 1));

      if (resize(file.getPath(), thumpPath + "/" + file.getName(), scaleHeight)) {
        image.setImageThumpPath(thumpPath + "/" + file.getName());
      } else {
        File isFileExist = new File(thumpPath + "/" + file.getName());
        if (isFileExist.exists()) {
          image.setImageThumpPath(thumpPath + "/" + file.getName());
        }
      }

      image.setImageWatermarkPath(addTextWatermark(strText, file.getPath(), thumpPath, file.getName()));

      save(image);
    }

    // Ignore files which are not files and dirs
//    if (file.isFile()) {} else {
    if (!file.isFile()) {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          scanDirectory(child, thumpPath + "/" + file.getName(), scaleHeight, strText);
        }
      }
    }
  }

  private void addKeywordToImageSet(String key, Image image) {
    Keyword keyword;
    Keyword fendedKeyword = keywordsService.findByKeywordEn(key.toLowerCase());
    if (fendedKeyword != null) {
      keyword = fendedKeyword;
    } else {
      keyword = new Keyword(key.toLowerCase());
    }
    if (checkIfLatinLetters(key)) {
      image.getKeywords().add(keyword);
    } else {
      System.out.println(key);
    }
  }


  private String readImageMetadata(String currentImagePath, String outputImagePath) {
    if (ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.readImageMetadata(currentImagePath);
    }
    return "null";
  }

  private boolean resize(String currentImagePath, String outputImagePath, int scaleHeight) {
    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.resize(currentImagePath, outputImagePath, 200);
    }
    return false;
  }

  private boolean resizeForGoogleVision(String currentImagePath, String outputImagePath, int scaleHeight) {
    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.resize(currentImagePath, outputImagePath, 200);
    }
    return false;
  }

  private String addTextWatermark(String text, String sourceImagePath, String destImagePath, String fileName) {

    fileName = fileName.substring(0, fileName.indexOf("."));
    fileName = fileName + "-Watermark." + sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);
    return ImageProcessing.addTextWatermark(text, sourceImagePath, destImagePath + "/" + fileName);
  }

  private String splitName(String fileName) {
    String string = fileName.replace("_", ";");
    string = string.substring(0, string.indexOf("."));
    return string;
  }

  private Set<String> splitKeywordsToArray(String keywords, String splitCharacter) {
    Set<String> keywordsArray = new HashSet<>(Arrays.asList(keywords.split(splitCharacter)));
    return keywordsArray;
  }

  /**
   * check if text Latin
   */
  private boolean checkIfLatinLetters(String key) {
    Pattern pattern = Pattern.compile(
      "[" +                   //начало списка допустимых символов
        "a-zA-ZäÄöÖüÜß" +    //буквы русского алфавита
        "\\d" +         //цифры
        "\\s" +         //знаки-разделители (пробел, табуляция и т.д.)
        "\\p{Punct}" +  //знаки пунктуации
        "]" +                   //конец списка допустимых символов
        "*");
    Matcher matcher = pattern.matcher(key);
    return matcher.matches();
  }

  /**
   * create Folder
   *
   * @param pathName: String path new Folder
   */
  private void createDirectory(String pathName) {
    Path path = Paths.get(pathName);
    if (!Files.exists(path)) {
      try {
        Files.createDirectory(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
