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

    String path = "D:\\pics\\Fotolia";
    String thumpPath = "d:\\thump";

    long lStartTime = System.nanoTime();

    File folder = new File(path);
    System.out.println(DirSize.sizeOf(folder));

    long lEndTime = System.nanoTime();

    long output = lEndTime - lStartTime;
    System.out.println("Elapsed time in milliseconds: " + output / 1000000);
  }
}


 class DirSize {

  private static final Logger LOGGER = LoggerFactory.getLogger(DirSize.class);

  public static long sizeOf(final File file) {
    DirSize.LOGGER.debug("Computing size of: {}", file);

    long size = 0;

    // Ignore files which are not files and dirs
    if (file.isFile()) {
      size = file.length();
    } else {
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          size += DirSize.sizeOf(child);
        }
      }
    }

    return size;
  }
}
