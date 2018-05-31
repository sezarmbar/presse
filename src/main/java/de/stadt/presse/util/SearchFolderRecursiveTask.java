package de.stadt.presse.util;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SearchFolderRecursiveTask {
  public static void main(String[] args) {

    String path = "D:\\pics\\Fotolia\\Baustellen";
    String thumpPath = "d:\\thump";
    int scaleHeight = 200;
    Search search = new Search(path,thumpPath,scaleHeight);
    ForkJoinPool pool = new ForkJoinPool();

  }
}

@Data
class Search  extends RecursiveTask{
  private static final long serialVersionUID = 1L;
  String path ;
  String thumpPath ;
  int scaleHeight ;
  static int counteOrg = 0;


  @Override
  protected Object compute() {

    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();
    final List<Search> tasks = new ArrayList<>();
    if (listOfFiles != null) {
      for (File file : listOfFiles) {
        if (file.isDirectory()) {
          createDirectory(thumpPath + "\\" + file.getName());
          Search search = new Search(path + "\\" + file.getName(), thumpPath + "\\" + file.getName(), scaleHeight);
          search.fork();
          tasks.add(search);
        } else if (file.isFile()) {
          String currentImagePath = path + "\\" + file.getName();
          String outputImagePath = thumpPath + "\\" + file.getName();
          imagePeocessing(currentImagePath, outputImagePath, scaleHeight);
        }
      }
    }
    return null;
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


  public Search(String path, String thumpPath, int scaleHeight) {
    this.path = path;
    this.thumpPath = thumpPath;
    this.scaleHeight = scaleHeight;
  }
}
