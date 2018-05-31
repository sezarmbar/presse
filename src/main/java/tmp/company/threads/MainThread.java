package tmp.company.threads;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.drew.metadata.Directory;
import de.stadt.presse.util.ImageProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainThread{
  public static void main(String[] args) {

    String path = "D:\\pics\\Fotolia\\Baustellen";
    String thumpPath = "d:\\thump";
    int scaleHeight = 200;
    long lStartTime = System.nanoTime();

    File folder = new File(path);
    System.out.println(DirSize.sizeOf(folder,thumpPath,scaleHeight));

    long lEndTime = System.nanoTime();

    long output = lEndTime - lStartTime;
    System.out.println("Elapsed time in milliseconds: " + output / 1000000);
  }
}


 class DirSize {

  private static final Logger LOGGER = LoggerFactory.getLogger(DirSize.class);

  public static long sizeOf(final File file,String thumpPath,int scaleHeight) {
    DirSize.LOGGER.debug("Computing size of: {}", file);

    if(file.isDirectory()){
      createDirectory(thumpPath+"\\"+file.getName());
    }else if(file.isFile()){
      imagePeocessing(file.getPath(), thumpPath+"\\"+file.getName(), scaleHeight);

    }
    long size = 0;

    // Ignore files which are not files and dirs
    if (file.isFile()) {
      size = file.length();
    } else {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          size += DirSize.sizeOf(child,thumpPath+"\\"+file.getName(),scaleHeight);
        }
      }
    }

    return size;
  }

   public static void imagePeocessing(String currentImagePath, String outputImagePath, int scaleHeight) {
     if (ImageProcessing.isImage(currentImagePath)) {
       ImageProcessing.readImageMetadata(currentImagePath);
       if (!Files.exists(Paths.get(outputImagePath))) {
         ImageProcessing.resize(currentImagePath, outputImagePath, 200);
       }
     }
   }


   /**
    * create Folder
    *
    * @param pathName: String path new Folder
    */
   public static void createDirectory(String pathName) {
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

