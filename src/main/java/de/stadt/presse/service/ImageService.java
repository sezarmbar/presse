package de.stadt.presse.service;

import de.stadt.presse.entity.Image;
import de.stadt.presse.entity.Keyword;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.util.ImageProcessing;
import de.stadt.presse.util.ScanDirs;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageService {

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private KeywordsService keywordsService;

  @Autowired
  private ScanDirs task;

  private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
  private final ForkJoinPool pool = new ForkJoinPool(AVAILABLE_PROCESSORS);

  private int scaleHeightForGoogleVision, scaleHeight;

  private String strText;

  public Image save(Image image) {
    return imageRepository.save(image);
  }

  public Image findByImagePath(String imagePath) {
    return imageRepository.findByImagePath(imagePath);
  }

  public Optional<Image> findById(Long id) {
    return imageRepository.findById(id);
  }

  public List<Image> findAll() {
    return imageRepository.findAll();
  }

  private String googleVisionLocalPath;

  private String orgFolder, orgThumpPath;
  private final String IMAGWATERMARK = "-Watermark.";

  public boolean callSDirs(String folderPath, String thumpPath, String googleVisionLocalPath,
                           int scaleHeight, int scaleHeightForGoogleVision, String strText) {

    this.googleVisionLocalPath = googleVisionLocalPath;
    this.scaleHeight = scaleHeight;
    this.scaleHeightForGoogleVision = scaleHeightForGoogleVision;
    this.strText = strText;
    File folder = new File(folderPath);


    scanDirectory(folder, thumpPath);
    return true;
  }

  private void scanDirectory(final File file, String thumpPath) {

    Image image = imageRepository.findByImagePath(file.getPath());

    if (file.isDirectory()) {
      createDirectory(thumpPath + "/" + file.getName());
    } else if (file.isFile() && ImageProcessing.isImage(file.getPath())) {

      String imageExtension = ImageProcessing.determineImageFormat(file.getPath());

      if (image == null) {
//        if (imageExtension == "JPEG" ){
        if (Objects.equals(imageExtension, "JPEG")){
          saveJPGImage(file, thumpPath);
        } else {
          saveCopyImage(file, thumpPath);
        }
      } else if (!(new File(thumpPath + "/" + file.getName()).exists())) {

        if (resize(file.getPath(), thumpPath + "/" + file.getName())) {
          image.setImageThumpPath(thumpPath);
        }
        setImageWatermarkName(image, file, thumpPath);
        save(image);

      } else if (!image.isImageHaveMetadata() && !(new File(googleVisionLocalPath + "/" + file.getName()).exists())) {
        resizeForGoogleVision(file.getPath(), googleVisionLocalPath + "/" + file.getName());
      } else {
        System.out.println("have a simaler image in database with this Data   : " + file.getPath());
      }

    }
//    else {
      //TODO add entity for not processed files
//      System.out.println("the file not image   : " + file.getPath());
//    }

    //TODO handle errore keywordEN notblanck

    // Ignore files which are not files and dirs
//    if (file.isFile()) {} else {
    if (!file.isFile()) {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          scanDirectory(child, thumpPath + "/" + file.getName());
        }
      }
    }
  }

  private boolean saveCopyImage(File file, String thumpPath) {

    Image image = new Image();
    image.setImageName(file.getName());
    image.setImagePath(file.getPath());

    String outputImagePath = thumpPath + "/" + file.getName();
    if (new File(outputImagePath).exists()) {
      image.setImageThumpPath(thumpPath);
    } else if (resize(file.getPath(), outputImagePath)) {
      image.setImageThumpPath(thumpPath);
    }

    setImageWatermarkName(image, file, thumpPath);

    if (!new File(thumpPath + "/" + file.getName()).exists()) {
      ImageProcessing.copyImage(file, new File(thumpPath));
    }
    if (!new File(googleVisionLocalPath + "/" + file.getName()).exists()) {
      ImageProcessing.copyImage(file, new File(googleVisionLocalPath));
    }

    save(image);

    return true;
  }

  private boolean saveJPGImage(File file, String thumpPath) {
    try {
      Image image = new Image();
      image.setImageName(file.getName());
      image.setImagePath(file.getPath());
      String metadataKeywords = readImageMetadata(file.getPath(), thumpPath + "/" + file.getName());
      if (metadataKeywords.equalsIgnoreCase("null")) {
        image.setImageHaveMetadata(false);
        image.setImageAllKeywords(splitName(file.getName()));
        resizeForGoogleVision(file.getPath(), googleVisionLocalPath + "/" + file.getName());
      } else {
        image.setImageAllKeywords(metadataKeywords + ";" + splitName(file.getName()));
        image.setImageHaveMetadata(true);
      }

      Set<String> keywordsSet = splitKeywordsToArray(image.getImageAllKeywords(), ";");
      keywordsSet.forEach(key -> {
        Set<String> keySet = splitKeywordsToArray(key, ",");
        if (keySet.size() > 1) {
          keySet.forEach(subKey ->
            addKeywordToImageSet(subKey, image)
          );
        } else {
          addKeywordToImageSet(key, image);
        }
      });

      String fileExtension = FilenameUtils.getExtension(file.getName());
      image.setImageType(fileExtension);

      String outputImagePath = thumpPath + "/" + file.getName();
      if (new File(outputImagePath).exists()) {
        image.setImageThumpPath(thumpPath);
      } else if (resize(file.getPath(), outputImagePath)) {
        image.setImageThumpPath(thumpPath);
      }

      setImageWatermarkName(image, file, thumpPath);

      save(image);

      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void setImageWatermarkName(Image image, File file, String thumpPath) {
    String newFileName = changeImageNameForWatermark(file.getName());

    if (new File(thumpPath + "/" + newFileName).exists()) {
      image.setImageWatermarkName(newFileName);
    } else if (addTextWatermark(file.getPath(), thumpPath, newFileName)) {
      image.setImageWatermarkName(newFileName);
    }
  }

  private void addKeywordToImageSet(String key, Image image) {
    Keyword keyword, keywordCheck ;
    Keyword fendedKeyword = keywordsService.findByKeywordEn(key.toLowerCase());
    if (fendedKeyword != null) {
      keyword = fendedKeyword;
      image.getKeywords().add(keyword);
    } else {
      keywordCheck = image.getKeywords().stream().filter(keyword1 -> keyword1.getKeywordEn().contains(key)).findAny().orElse(null);
      keyword = new Keyword(key.toLowerCase());
      if (checkIfLatinLetters(key) && keywordCheck == null && !"".equals(key)) {
        image.getKeywords().add(keyword);
      }
    }

  }


  private String readImageMetadata(String currentImagePath, String outputImagePath) {
    if (ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.readImageMetadata(currentImagePath);
    }
    return "null";
  }

  private boolean resize(String currentImagePath, String outputImagePath) {
    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.resize(currentImagePath, outputImagePath, scaleHeight);
    }
    return false;
  }

  private boolean resizeForGoogleVision(String currentImagePath, String outputImagePath) {
    if (!Files.exists(Paths.get(outputImagePath)) && ImageProcessing.isImage(currentImagePath)) {
      boolean isCompressed = ImageProcessing.compressImageThump(currentImagePath, outputImagePath);
      if (isCompressed) {
        return ImageProcessing.resize(outputImagePath, outputImagePath, scaleHeightForGoogleVision);
      } else {
        return false;
      }
    }
    return false;
  }

  private boolean addTextWatermark(String sourceImagePath, String destImagePath, String fileName) {

//    fileName = fileName.substring(0, fileName.indexOf("."));
//    fileName = fileName + IMAGWATERMARK + sourceImagePath.substring(sourceImagePath.lastIndexOf(".") + 1);

    return ImageProcessing.addTextWatermark(strText, sourceImagePath, destImagePath + "/" + fileName);


  }

  private String changeImageNameForWatermark(String fileName) {
    String fileExtension = FilenameUtils.getExtension(new File(fileName).getName());
    fileName = fileName.substring(0, fileName.indexOf("."));
    return fileName + IMAGWATERMARK + fileExtension;
  }

  private String splitName(String fileName) {
    String string = fileName.replace("_", ";");
    string = string.substring(0, string.indexOf("."));
    return string;
  }

  private Set<String> splitKeywordsToArray(String keywords, String splitCharacter) {
    return new HashSet<>(Arrays.asList(keywords.split(splitCharacter)));
  }

  /**
   * check if text have a Latin letters
   */
  private boolean checkIfLatinLetters(String key) {
    Pattern pattern = Pattern.compile(
      "[" +                   //начало списка допустимых символов
        "a-zA-ZäÄöÖüÜßé" +    //буквы русского алфавита
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


  public List findByKeywords(String keywordEn) {

    List<String> list = Arrays.asList(keywordEn.split(","));
    return imageRepository.findAllKeyword(keywordEn, (long) list.size());

  }


}
