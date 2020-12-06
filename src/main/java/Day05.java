import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Day05 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/05.txt";
  }

  @Override
  public String part1(List<String> lines) {
    int maxSeatId = lines.stream()
        .mapToInt(Day05::passToInt)
        .max().orElseThrow(NoSuchElementException::new);
    return String.valueOf(maxSeatId);
  }

  @Override
  public String part2(List<String> lines) {
    IntSummaryStatistics stats = lines.stream()
        .collect(Collectors.summarizingInt(Day05::passToInt));
    // sum of arithmetic series
    long expectedTotal = (stats.getCount() + 1) * (stats.getMin() + stats.getMax()) / 2;
    long missingSeatId = expectedTotal - stats.getSum();
    return String.valueOf(missingSeatId);
  }

  private static int passToInt(String pass) {
    String binaryStr = pass.replace("B", "1")
        .replace("F", "0")
        .replace("R", "1")
        .replace("L", "0");
    return Integer.parseInt(binaryStr, 2);
  }
}
