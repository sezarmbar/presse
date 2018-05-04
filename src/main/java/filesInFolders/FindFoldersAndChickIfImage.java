package filesInFolders;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;

public class FindFoldersAndChickIfImage {
  public static void main(String[] args) {

//    String filepath = "E:/Fotolia/Natur & Tier & Umwelt/Wolfi mit Thermometer_javier brosch - Fotolia.com.jpg";
//    File f = new File(filepath);
//    MimetypesFileTypeMap mediatype = new MimetypesFileTypeMap();
//    mediatype.addMimeTypes("image/psd psd");
//    String mimetype= mediatype.getContentType(f);
//
//    String type = mimetype.split("/")[0];
//    if(type.equals("image"))
//      System.out.println("It's an image");
//    else
//      System.out.println("It's NOT an image");

    String path = "e:";

      namePrint(path, "", "");

  }

  public static void namePrint(String path, String space, String dir) throws NullPointerException {
    File folder = new File(path);
    String spa = space + dir + "  ";
    File[] listOfFiles = folder.listFiles();
    try {
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isDirectory()) {
          System.out.println(spa + listOfFiles[i].getName() + "  ---->  " + path);
          namePrint(path + "/" + listOfFiles[i].getName(), spa, " |");
        } else if (listOfFiles[i].isFile()) {
          System.out.print(spa + listOfFiles[i].getName());
          System.out.println("  " + path + "/" + listOfFiles[i].getName());
        }
      }
    }catch (NullPointerException e){
      System.out.println("path not found ... redirect to e:");
//      namePrint("e:", "", "");
    }
  }


    public static boolean isImage (File file){
      System.out.println(file.getName());
      return true;
    }
  }
