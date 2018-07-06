package tmp.company;

import de.stadt.presse.util.ScanDirs;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class FibonacciTask extends RecursiveTask<Long> {

  private static final long serialVersionUID = 1L;

  private final long inputValue;

  public FibonacciTask(long inputValue) {
    this.inputValue = inputValue;
  }

  @Override
  public Long compute() {

    if (inputValue == 0L) {
      return 0L;
    } else if (inputValue <= 2L) {
      return 1L;
    } else {
      final FibonacciTask firstWorker = new FibonacciTask(inputValue - 1L);
      firstWorker.fork();

      final FibonacciTask secondWorker = new FibonacciTask(inputValue - 2L);
      return secondWorker.compute() + firstWorker.join();
    }
  }
}
