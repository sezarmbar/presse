package image.tmp;

import image.tmp.Metadata;

import java.io.File;

public class MetadataMain {
  public static void main(String[] args) {
    Metadata metadata = new Metadata();
    String filename = "E:\\Fotolia\\Feiertage\\Geschenke und Tasse_Masson_Fotolia.jpg";
    if(new File(filename).exists()){
      metadata.readAndDisplayMetadata(filename);
    }else {
      System.out.println("cannot find file: "+ filename);
    }
  }
}
