import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/10.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Integer> adapters = lines.stream()
        .map(Integer::parseInt)
        .sorted()
        .collect(Collectors.toList());
    adapters.add(0, 0);
    adapters.add(adapters.get(adapters.size() - 1) + 3);
    Map<Integer, Long> diffCounts = IntStream.range(1, adapters.size())
        .mapToObj(i -> adapters.get(i) - adapters.get(i - 1))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    return String.valueOf(diffCounts.get(1) * diffCounts.get(3));
  }


  @Override
  public String part2(List<String> lines) {
    List<Integer> adapters = lines.stream().map(Integer::parseInt)
        .sorted()
        .collect(Collectors.toList());
    adapters.add(adapters.size(), adapters.get(adapters.size() - 1) + 3);
    long count = validArrangements(adapters, new Position(0, 0), new HashMap<>());
    return "" + count;
  }


  private long validArrangements(List<Integer> adapters, Position position, Map<Position, Long> memo) {
    if (memo.containsKey(position)) {
      return memo.get(position);
    }
    long result;
    int curr = adapters.get(position.index);
    int diff = curr - position.prev;
    if (diff > 3) {
      result = 0;
    } else if (position.index == adapters.size() - 1) {
      result = 1;
    } else {
      Position skipCurr = new Position(position.index + 1, position.prev);
      Position includeCurr = new Position(position.index + 1, curr);
      result = validArrangements(adapters, skipCurr, memo) + validArrangements(adapters, includeCurr, memo);
    }
    memo.put(position, result);
    return result;
  }

  static class Position {

    final int index;
    final int prev;

    Position(int index, int prev) {
      this.index = index;
      this.prev = prev;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Position that = (Position) o;
      return index == that.index &&
          prev == that.prev;
    }

    @Override
    public int hashCode() {
      return Objects.hash(index, prev);
    }
  }
}
