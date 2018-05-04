package image.tmp;
import org.apache.sanselan.ImageReadException;

import java.io.File;
import java.io.IOException;

public class ImageMain {
  public static void main(String[] args) throws IOException, ImageReadException {
    File sourceimage = new File("c:\\images2.jpg");

//    final IImageMetadata metadata =  Sanselan.getMetadata(sourceimage);
//
//    System.out.println(metadata);

    MetadataExample.metadataExample(sourceimage);

  }
}
