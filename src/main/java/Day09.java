import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class Day09 implements Day {

  private static int PREAMBLE_LENGTH = 25;


  @Override
  public String getInputPath() {
    return "src/main/resources/09.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Long> input = parseInput(lines);
    for (int i = PREAMBLE_LENGTH; i < input.size(); i++) {
      long curr = input.get(i);
      List<Long> previous = input.subList(i - PREAMBLE_LENGTH, i);
      if (!isValid(curr, previous)) {
        return String.valueOf(curr);
      }
    }

    return "not found";
  }

  private List<Long> parseInput(List<String> lines) {
    return lines.stream()
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }

  private long findConsecutiveSum(long target, List<Long> input) {
    int lowerIndex = 0;
    int upperIndex = 0;
    long sum = input.get(0);
    while (upperIndex < input.size() && lowerIndex < input.size()) {
      if (sum < target) {
        upperIndex++;
        sum += input.get(upperIndex);
      } else if (sum > target) {
        sum -= input.get(lowerIndex);
        lowerIndex++;
      }
      if (sum == target) {
        return getEncryptionWeakness(input.subList(lowerIndex, upperIndex+1));
      }
    }
    throw new IllegalArgumentException("not found");
  }

  private long getEncryptionWeakness(List<Long> input) {
    LongSummaryStatistics stats = input.stream()
        .mapToLong(Long::longValue)
        .summaryStatistics();
    return stats.getMax() + stats.getMin();
  }

  private boolean isValid(long n, List<Long> previous) {
    for (int i = 0; i < previous.size(); i++) {
      for (int j = 0; j < previous.size(); j++) {
        if (i == j) {
          continue;
        }
        if (previous.get(i) + previous.get(j) == n) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String part2(List<String> lines) {
    long target = Long.parseLong(part1(lines));
    List<Long> input = parseInput(lines);
    return String.valueOf(findConsecutiveSum(target, input));
  }
}
