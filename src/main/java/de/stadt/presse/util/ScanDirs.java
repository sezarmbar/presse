package de.stadt.presse.util;


import de.stadt.presse.entity.Image;
import de.stadt.presse.entity.Keyword;
import de.stadt.presse.service.ImageService;
import de.stadt.presse.service.KeywordsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@NoArgsConstructor
public class ScanDirs extends RecursiveTask<Boolean> {
//
//  @Autowired
//  private ImageService imageService;
//  @Autowired
//  private KeywordsService keywordsService;
//
//  private int scaleHeightForGoogleVision, scaleHeight;
//  private String strText;
//  private String googleVisionLocalPath;
//  private File orgFolder;
//  private String orgThumpPath;
//
////  @Autowired
//  public ScanDirs(String folder, String thumpPath, String googleVisionLocalPath, Integer scaleHeight, Integer scaleHeightForGoogleVision, String strText ) {
//    this.googleVisionLocalPath = googleVisionLocalPath;
//    this.scaleHeight = scaleHeight;
//    this.scaleHeightForGoogleVision = scaleHeightForGoogleVision;
//    this.strText = strText;
//    this.orgFolder = new File(folder);
//    this.orgThumpPath = thumpPath;
//  }


  @Override
  protected Boolean compute() {
//
//    File file = orgFolder;
//    String thumpPath = orgThumpPath;
//
//    Image image = imageService.findByImagePath(file.getPath());
//
//    if (file.isDirectory()) {
//      createDirectory(thumpPath + "/" + file.getName());
//    } else if (file.isFile() && ImageProcessing.isImage(file.getPath())) {
//
//      String imageExtension = ImageProcessing.determineImageFormat(file.getPath());
//      if (image == null) {
//        if (imageExtension.equals("JPEG")) {
//           saveJPGImage(file, thumpPath);
//        } else if (imageExtension.equals("png")) {
//           savePNGImage(file, thumpPath);
//        }
//      } else if (!(new File(thumpPath + "/" + file.getName()).exists())) {
//
//        if (resize(file.getPath(), thumpPath + "/" + file.getName())) {
//          image.setImageThumpPath(thumpPath + "/" + file.getName());
//        }
//        image.setImageWatermarkName(addTextWatermark(file.getPath(), thumpPath, file.getName()));
//
//        imageService.save(image);
//        return true;
//
//      } else if (!image.isImageHaveMetadata() && !(new File(googleVisionLocalPath + "/" + file.getName()).exists())) {
//        resizeForGoogleVision(file.getPath(), googleVisionLocalPath + "/" + file.getName());
//      } else {
//        System.out.println("have a simaler image in database with this Data   : " + file.getPath());
//
//      }
//
//    } else {
//      //TODO add entity for not processed files
//      System.out.println("the file not image   : " + file.getPath());
//    }
//
//    // Ignore files which are not files and dirs
////    if (file.isFile()) {} else {
//    if (!file.isFile()) {
//      final File[] children = file.listFiles();
//      if (children != null) {
//        for (final File child : children) {
//          final ScanDirs firstWorker = new ScanDirs(child.getPath(), thumpPath + "/" + file.getName(), googleVisionLocalPath, scaleHeight, scaleHeightForGoogleVision, strText);
//          firstWorker.fork();
//        }
//      }
//    }
//
//
    return false;
  }
//
//
//  //TODO process png image and save to database
//  private boolean savePNGImage(File file, String thumpPath) {
//    ImageProcessing.copyImage(file, new File(thumpPath));
//    Image image = new Image();
//    image.setImageName(file.getName());
//    image.setImagePath(file.getPath());
//    image.setImageThumpPath(thumpPath + "/" + file.getName());
//
//    try {
//      imageService.save(image);
//      return true;
//    } catch (Exception e) {
//      System.out.println(" i cant save the PNG .....  " + e.getClass());
//    }
//    return false;
//  }
//
//  private boolean saveJPGImage(File file, String thumpPath) {
//    try {
//      Image image = new Image();
//      image.setImageName(file.getName());
//      image.setImagePath(file.getPath());
//      String metadataKeywords = readImageMetadata(file.getPath(), thumpPath + "/" + file.getName());
//
//      if (metadataKeywords == "null") {
//        image.setImageHaveMetadata(false);
//        image.setImageAllKeywords(splitName(file.getName()));
//        resizeForGoogleVision(file.getPath(), googleVisionLocalPath + "/" + file.getName());
//      } else {
//        image.setImageAllKeywords(metadataKeywords + ";" + splitName(file.getName()));
//        image.setImageHaveMetadata(true);
//      }
//
//      Set<String> keywordsSet = splitKeywordsToArray(image.getImageAllKeywords(), ";");
//      keywordsSet.forEach(key -> {
//        Set<String> keySet = splitKeywordsToArray(key, ",");
//        if (keySet.size() > 1) {
//          keySet.forEach(subKey ->
//            addKeywordToImageSet(subKey, image)
//          );
//        } else {
//          addKeywordToImageSet(key, image);
//        }
//      });
//
//      image.setImageType(file.getName().substring(file.getName().indexOf(".") + 1));
//
//      if (resize(file.getPath(), thumpPath + "/" + file.getName())) {
//        image.setImageThumpPath(thumpPath + "/" + file.getName());
//      } else {
//        File isFileExist = new File(thumpPath + "/" + file.getName());
//        if (isFileExist.exists()) {
//          image.setImageThumpPath(thumpPath + "/" + file.getName());
//        }
//      }
//
//      image.setImageWatermarkName(addTextWatermark(file.getPath(), thumpPath, file.getName()));
//
//      imageService.save(image);
//      return true;
//
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//
//  private void addKeywordToImageSet(String key, Image image) {
//    Keyword keyword, keywordCheck = new Keyword();
//    Keyword fendedKeyword = keywordsService.findByKeywordEn(key.toLowerCase());
//    if (fendedKeyword != null) {
//      keyword = fendedKeyword;
//      image.getKeywords().add(keyword);
//    } else {
//      keywordCheck = image.getKeywords().stream().filter(keyword1 -> keyword1.getKeywordEn().contains(key)).findAny().orElse(null);
//      keyword = new Keyword(key.toLowerCase());
//      if (checkIfLatinLetters(key) && keywordCheck == null && !"".equals(key)) {
//        image.getKeywords().add(keyword);
//      }
//    }
//
//  }
//
//
//  private String readImageMetadata(String currentImagePath, String outputImagePath) {
//    if (ImageProcessing.isImage(currentImagePath)) {
//      return ImageProcessing.readImageMetadata(currentImagePath);
//    }
//    return "null";
//  }
//
//  private boolean resize(String currentImagePath, String outputImagePath) {
//    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
//      return ImageProcessing.resize(currentImagePath, outputImagePath, scaleHeight);
//    }
//    return false;
//  }
//
//  private boolean resizeForGoogleVision(String currentImagePath, String outputImagePath) {
//    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
//      boolean isCompressed = ImageProcessing.compressImageThump(currentImagePath, outputImagePath);
//      if (isCompressed) {
//        return ImageProcessing.resize(outputImagePath, outputImagePath, scaleHeightForGoogleVision);
//      } else {
//        return false;
//      }
//    }
//    return false;
//  }
//
//  private String addTextWatermark(String sourceImagePath, String destImagePath, String fileName) {
//
//    fileName = fileName.substring(0, fileName.indexOf("."));
//    fileName = fileName + "-Watermark." + sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);
//    return ImageProcessing.addTextWatermark(strText, sourceImagePath, destImagePath + "/" + fileName);
//  }
//
//  private String splitName(String fileName) {
//    String string = fileName.replace("_", ";");
//    string = string.substring(0, string.indexOf("."));
//    return string;
//  }
//
//  private Set<String> splitKeywordsToArray(String keywords, String splitCharacter) {
//    return new HashSet<>(Arrays.asList(keywords.split(splitCharacter)));
//  }
//
//  /**
//   * check if text have a Latin letters
//   */
//  private boolean checkIfLatinLetters(String key) {
//    Pattern pattern = Pattern.compile(
//      "[" +                   //начало списка допустимых символов
//        "a-zA-ZäÄöÖüÜßé" +    //буквы русского алфавита
//        "\\d" +         //цифры
//        "\\s" +         //знаки-разделители (пробел, табуляция и т.д.)
//        "\\p{Punct}" +  //знаки пунктуации
//        "]" +                   //конец списка допустимых символов
//        "*");
//    Matcher matcher = pattern.matcher(key);
//    return matcher.matches();
//  }
//
//  /**
//   * create Folder
//   *
//   * @param pathName: String path new Folder
//   */
//  private void createDirectory(String pathName) {
//    Path path = Paths.get(pathName);
//    if (!Files.exists(path)) {
//      try {
//        Files.createDirectory(path);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
//  }
//

}

