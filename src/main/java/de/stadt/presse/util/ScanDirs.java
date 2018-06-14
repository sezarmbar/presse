package de.stadt.presse.util;

import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.ImageRepository;
import de.stadt.presse.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//http://www.javacreed.com/java-fork-join-example/
//public class MainThread {
//  public static void main(String[] args) {
//
//    String path = "D:\\pics\\Fotolia\\Baustellen";
//    String thumpPath = "d:\\thump";
//    int scaleHeight = 200;
////    long lStartTime = System.nanoTime();
//
//    File folder = new File(path);
//    ScanDirs readDirs= new ScanDirs();
//    readDirs.scan(folder, thumpPath, scaleHeight);
//
////    long lEndTime = System.nanoTime();
////
////    long output = lEndTime - lStartTime;
////    System.out.println("Elapsed time in milliseconds: " + output / 1000000);
//  }
//}


class ScanDirs {

  @Autowired
  private
  ImageService imageService;


  private static final Logger LOGGER = LoggerFactory.getLogger(ScanDirs.class);
  private Image image;

  private void scan(final File file, String thumpPath, int scaleHeight) {

    if (file.isDirectory()) {
      createDirectory(thumpPath + "/" + file.getName());
    } else if (file.isFile()) {
      image = new Image();
      image.setImageName(file.getName());
      image.setImagePath(file.getPath());
      image.setImageAllKeywords(readImageMetadata(file.getPath(), thumpPath + "/" + file.getName()) +
        ";" + splitName(file.getName()));
      image.setImageType(file.getName().substring(file.getName().indexOf(".") + 1));
      if (resize(file.getPath(), thumpPath + "/" + file.getName(), scaleHeight)) {
        image.setImageThumpPath(thumpPath + "/" + file.getName());
      }
      imageService.save(image);
    }

    // Ignore files which are not files and dirs
    if (!file.isFile()) {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          ScanDirs readDirs = new ScanDirs();
          readDirs.scan(child, thumpPath + "/" + file.getName(), scaleHeight);
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

