import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/14.txt";
  }

  private static Pattern memRegex = Pattern.compile("mem\\[(?<address>\\d+)] = (?<val>\\d+)");
  private static Pattern maskRegex = Pattern.compile("mask = (?<mask>.+)");

  @Override
  public String part1(List<String> lines) {
    Program p = new Program1(lines);
    p.run();
    return "" + p.sumValues();
  }

  @Override
  public String part2(List<String> lines) {
    Program p = new Program2(lines);
    p.run();
    return "" + p.sumValues();
  }

  static abstract class Program {
    private List<String> lines;
    protected Mask mask;
    protected Map<Long, Long> memory = new HashMap<>();

    Program(List<String> lines) {
      this.lines = lines;
    }

    private void run() {
      lines.forEach(line -> {
        Matcher memMatcher = memRegex.matcher(line);
        if (memMatcher.matches()) {
          long val = Long.parseLong(memMatcher.group("val"));
          long address = Long.parseLong(memMatcher.group("address"));
          doInstruction(val, address);
        } else {
          updateMask(line);
        }
      });
    }

    abstract void doInstruction(long val, long address);

    private void updateMask(String line) {
      Matcher maskMatcher = maskRegex.matcher(line);
      if (maskMatcher.matches()) {
        String newMask = maskMatcher.group("mask");
        mask = Mask.fromString(newMask);
      } else {
        throw new IllegalArgumentException("no match");
      }
    }

    private long sumValues() {
      return memory.values().stream()
          .mapToLong(Long::longValue)
          .sum();
    }
  }

  static class Program1 extends Program {
    Program1(List<String> lines) {
      super(lines);
    }

    @Override
    void doInstruction(long val, long address) {
      val |= mask.onesMask;
      val &= mask.zerosMask;
      memory.put(address, val);
    }
  }

  static class Program2 extends Program {
    Program2(List<String> lines) {
      super(lines);
    }

    @Override
    void doInstruction(long val, long address) {
      address |= mask.onesMask;
      storeFloatingValues(address, val, mask.floatPositions);
    }

    private void storeFloatingValues(long address, long val, List<Integer> remainingFloatingPositions) {
      if (remainingFloatingPositions.isEmpty()) {
        memory.put(address, val);
        return;
      }
      long floatMask = 1L << remainingFloatingPositions.get(0);
      List<Integer> updatedPositions = remainingFloatingPositions.subList(1, remainingFloatingPositions.size());
      storeFloatingValues(address | floatMask, val, updatedPositions); // use 1
      storeFloatingValues(address  & (~floatMask), val, updatedPositions); // use 0
    }
  }

  static class Mask {
    private long onesMask;
    private long zerosMask;
    private List<Integer> floatPositions;

    static Mask fromString(String s){
      Mask mask = new Mask();
      String onesMaskStr = s.replaceAll("[^1]", "0");
      mask.onesMask = Long.parseLong(onesMaskStr, 2);

      String zerosMaskStr = s.replaceAll("[^0]", "1");
      mask.zerosMask = Long.parseLong(zerosMaskStr, 2);

      mask.floatPositions = IntStream.range(0, s.length())
          .filter(i -> s.charAt(i) == 'X')
          .mapToObj(i -> s.length() - 1 - i)
          .collect(Collectors.toList());
      return mask;
    }
  }
}

