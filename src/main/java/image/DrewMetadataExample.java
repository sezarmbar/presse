package image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.iptc.IptcDirectory;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class DrewMetadataExample {
  public static void main(String[] args) {
    try {
      File filename = new File("c:/images2.jpg");
      Metadata metadata = ImageMetadataReader.readMetadata(filename);
      List<String> keyword = metadata.getFirstDirectoryOfType(IptcDirectory.class).getKeywords();
      System.out.println(keyword);
    } catch (ImageProcessingException e) {
      System.out.println(e);
    } catch (IOException ioE) {
      System.out.println(ioE);
    }
  }
}
