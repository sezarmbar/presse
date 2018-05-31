package de.stadt.presse.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindFolders {

  static int counteOrg;
  static int counteSec;

  public static void main(String[] args) {


    String path = "D:\\pics\\Fotolia\\Baustellen";
    String pathThump = "d:\\thump";


    try {
      searchForImages(path, pathThump, 200);
    } finally {
      System.out.println(counteOrg);
    }

  }

  /**
   * search for Images
   *
   * @param path        String root path
   * @param thumpPath   String root thump path
   * @param scaleHeight int
   */
  public static void searchForImages(String path, String thumpPath, int scaleHeight)  {
    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();
    if (listOfFiles != null) {
        for (File file : listOfFiles) {
          if (file.isDirectory()) {
            createDirectory(thumpPath + "\\" + file.getName());
            searchForImages(path + "\\" + file.getName(), thumpPath + "\\" + file.getName(), scaleHeight);
          } else if (file.isFile()) {
            String currentImagePath = path + "\\" + file.getName();
            String outputImagePath = thumpPath + "\\" + file.getName();
            imagePeocessing(currentImagePath, outputImagePath, scaleHeight);
          }
        }
    }
  }
  private static void imagePeocessing(String currentImagePath, String outputImagePath, int scaleHeight) {
    if (ImageProcessing.isImage(currentImagePath)) {
      ImageProcessing.readImageMetadata(currentImagePath);
      if (!Files.exists(Paths.get(outputImagePath))) {
        counteOrg++;
        ImageProcessing.resize(currentImagePath, outputImagePath, 200);
      }
    }
  }


  /**
   * create Folder
   *
   * @param pathName: String path new Folder
   */
  private static void createDirectory(String pathName) {
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


