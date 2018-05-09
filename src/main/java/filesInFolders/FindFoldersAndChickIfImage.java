package filesInFolders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindFoldersAndChickIfImage {
  public static void main(String[] args) {

//    createDirectory("e:/mahmoud");

    String path = "E:";

    namePrint(path, "", "");

  }

  private static void namePrint(String path, String space, String dir) throws NullPointerException {
    File folder = new File(path);
    String spa = space + dir + "  ";
    File[] listOfFiles = folder.listFiles();
    try {

      if (listOfFiles != null) {
        for (File file : listOfFiles) {
          if(file.isDirectory()){
            System.out.println(spa + file.getName() + "  ---->  " + path);
            namePrint(path + "/" + file.getName() , spa, " |");
          }else if(file.isFile()){
            System.out.print(spa + file.getName());
            System.out.println("  " + path + "/" + file.getName());
          }
        }
      }
    } catch (NullPointerException e) {
      System.out.println("path not found ... redirect to e:");
//      namePrint("e:", "", "");
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


  public static boolean isImage(File file) {
    System.out.println(file.getName());
    return true;
  }
}
