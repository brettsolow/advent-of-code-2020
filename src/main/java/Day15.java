import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/15.txt";
  }

  private int getNumber(List<String> lines, int rounds) {
    int[] startingN = Arrays.stream(lines.get(0).split(","))
        .mapToInt(Integer::parseInt)
        .toArray();
    Map<Integer, NumberInfo> numbers = new HashMap<>();
    int lastSpoken = -1;
    // 1 indexed
    for (int i = 1; i <= startingN.length; i++) {
      int n = startingN[i - 1];
      numbers.put(n, new NumberInfo(i, 0));
      lastSpoken = n;
    }
    for (int i = startingN.length + 1; i <= rounds; i++) {
      NumberInfo lastInfo = numbers.get(lastSpoken);
      int nextNumber = lastInfo.age;
      NumberInfo nextInfo = numbers.getOrDefault(nextNumber, new NumberInfo(i, 0));
      nextInfo.age = i - nextInfo.lastSpokenIndex;
      nextInfo.lastSpokenIndex = i;
      numbers.put(nextNumber, nextInfo);
      lastSpoken = nextNumber;
    }
    return lastSpoken;
  }

  @Override
  public String part1(List<String> lines) {
    return "" + getNumber(lines, 2020);
  }

  @Override
  public String part2(List<String> lines) {
    return "" + getNumber(lines, 30000000);
  }

  static class NumberInfo {
    int lastSpokenIndex;
    int age;

    NumberInfo(int lastSpokenIndex, int age) {
      this.lastSpokenIndex = lastSpokenIndex;
      this.age = age;
    }
  }
}
