package tmp.company.threads;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainFolesSize{
  public static void main(String[] args) {

    String path = "D:\\pics\\Fotolia";
    long lStartTime = System.nanoTime();

    File folder = new File(path);
    System.out.println(FoldesSize.sizeOf(folder));

    long lEndTime = System.nanoTime();

    long output = lEndTime - lStartTime;
    System.out.println("Elapsed time in milliseconds: " + output / 1000000);
  }
}

 class FoldesSize {

  private static final Logger LOGGER = LoggerFactory.getLogger(FoldesSize.class);

  private static class SizeOfFileTask extends RecursiveTask<Long> {

    private static final long serialVersionUID = -196522408291343951L;

    private final File file;

    public SizeOfFileTask(final File file) {
      this.file = Objects.requireNonNull(file);
    }

    @Override
    protected Long compute() {
//      FoldesSize.LOGGER.debug("Computing size of: {}", file);

      if (file.isFile()) {
        return file.length();
      }

      final List<SizeOfFileTask> tasks = new ArrayList<>();
      final File[] children = file.listFiles();
      if (children != null) {
        for (final File child : children) {
          final SizeOfFileTask task = new SizeOfFileTask(child);
          task.fork();
          tasks.add(task);
        }
      }

      long size = 0;
      for (final SizeOfFileTask task : tasks) {
        size += task.join();
      }

      return size;
    }
  }

  public static long sizeOf(final File file) {
    final ForkJoinPool pool = new ForkJoinPool();
    try {
      return pool.invoke(new SizeOfFileTask(file));
    } finally {
      pool.shutdown();
    }
  }

  private FoldesSize() {}

}
