package de.stadt.presse.service;

import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.util.ImageProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

  @Autowired
  private ImageRepository imageRepository;

  public Image save(Image image) {
    return imageRepository.save(image);
  }

  public List<Image> findAll() {
    return imageRepository.findAll();
  }


  public boolean scanDirs() {
    String path = "d:/pics/Fotolia/Baustellen/Bilder zur Ansicht";
    String thumpPath = "d:/thump";
    int scaleHeight = 200;
    File folder = new File(path);

    scanDirectory(folder, thumpPath, scaleHeight);
    return true;
  }


  private void scanDirectory(final File file, String thumpPath, int scaleHeight) {

    if (file.isDirectory()) {
      createDirectory(thumpPath + "/" + file.getName());
    } else if (file.isFile()) {
      Image image = new Image();
      image.setImageName(file.getName());
      image.setImagePath(file.getPath());
      image.setImageKeywords(readImageMetadata(file.getPath(), thumpPath + "/" + file.getName()) +
        ";" + splitName(file.getName()));
      image.setImageType(file.getName().substring(file.getName().indexOf(".") + 1));

      if (resize(file.getPath(), thumpPath + "/" + file.getName(), scaleHeight)) {
        image.setImageThumpPath(thumpPath + "/" + file.getName());
      } else {
        File isFileExist = new File(thumpPath + "/" + file.getName());
        if (isFileExist.exists()) {image.setImageThumpPath(thumpPath + "/" + file.getName());}
      }
      save(image);
    }

    // Ignore files which are not files and dirs
//    if (file.isFile()) {} else {
    if (!file.isFile()) {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          scanDirectory(child, thumpPath + "/" + file.getName(), scaleHeight);
        }
      }
    }
  }

  private String readImageMetadata(String currentImagePath, String outputImagePath) {
    if (ImageProcessing.isImage(currentImagePath)) {
      return ImageProcessing.readImageMetadata(currentImagePath);
    }
    return "null";
  }

  private boolean resize(String currentImagePath, String outputImagePath, int scaleHeight) {
    if (!Files.exists(Paths.get(outputImagePath))) {
      return ImageProcessing.resize(currentImagePath, outputImagePath, 200);
    }
    return false;
  }

  private String splitName(String fileName) {
    String string = fileName.replace("_", ";");
    string = string.substring(0, string.indexOf("."));
    return string;
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
