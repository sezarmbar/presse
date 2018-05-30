package de.stadt.presse.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindFoldersAndChickIfImage {

  static int countOrg = 0;
  static int countSec = 0;

  public static void main(String[] args) {


//    createDirectory("e:/mahmoud");

    String path = "E:\\";
    String pathThump = "d:\\thump";


    namePrint(path, pathThump, "", "");

  }

  /**
   * search in folders
   * */
  public static void namePrint(String path,String thumpPath, String space, String dir) throws NullPointerException {
    File folder = new File(path);
    String spa = space + dir + "  ";
    File[] listOfFiles = folder.listFiles();
    try {

      if (listOfFiles != null) {
        for (File file : listOfFiles) {
          if(file.isDirectory()){
            createDirectory(thumpPath+"\\"+ file.getName() );
            namePrint(path + "\\" + file.getName(),thumpPath + "\\" + file.getName(), spa, " |");
          }else if(file.isFile()){
//            System.out.println(spa + file.getName());
//            System.out.println("  " + path + "/" + file.getName()+" **** "+thumpPath + "/" + file.getName());
          }
        }
      }
    } catch (NullPointerException e) {
      System.out.println("path not found ... redirect to e:");
//      namePrint("e:", "", "");
    } finally {
      //TODO
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


//  public static void namePrint(String path, String space, String dir) throws NullPointerException {
//    File folder = new File(path);
//    String spa = space + dir + "  ";
//    File[] listOfFiles = folder.listFiles();
//    try {
//
//      if (listOfFiles != null) {
//        for (File file : listOfFiles) {
//          if(file.isDirectory()){
//            System.out.println(spa + file.getName() + "  ---->  " + path);
//            namePrint(path + "/" + file.getName() , spa, " |");
//          }else if(file.isFile()){
//            System.out.print(spa + file.getName());
//            System.out.println("  " + path + "/" + file.getName());
//          }
//        }
//      }
//    } catch (NullPointerException e) {
//      System.out.println("path not found ... redirect to e:");
////      namePrint("e:", "", "");
//    }
//  }

}


