import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/13.txt";
  }

  @Override
  public String part1(List<String> lines) {
    long earliest = Long.parseLong(lines.get(0));
    int[] buses = Arrays.stream(lines.get(1).split(","))
        .filter(b -> !b.equals("x"))
        .mapToInt(Integer::parseInt).toArray();
    int minBus = 0;
    long minWait = Long.MAX_VALUE;
    for (int bus : buses) {
      long time = (earliest / bus + 1) * bus;
      long wait = time - earliest;
      if (wait < minWait) {
        minWait = wait;
        minBus = bus;
      }
    }
    return "" + (minBus * minWait);
  }

  @Override
  public String part2(List<String> lines) {
    // TODO: something that will complete in my lifetime
    String[] input = lines.get(1).split(",");
    List<Bus> buses = IntStream.range(0, input.length)
        .filter(i -> !input[i].equals("x"))
        .mapToObj(i -> new Bus(Integer.parseInt(input[i]), i))
        .sorted(Comparator.comparing(Bus::getId, Comparator.reverseOrder()))
        .collect(Collectors.toList());

    Bus slowestBus = buses.get(0);
    int incBy = slowestBus.getId();
    long t = (long) incBy - (buses.get(0).index);
    while (!checkT(t, buses)) {
      t += incBy;
    }
    return "" + t;
  }

  private boolean checkT(long t, List<Bus> buses) {
    for (Bus b : buses) {
      if (((b.index + t) % b.id) != 0) {
        return false;
      }
    }
    return true;
  }

  static class Bus {

    int id;
    int index;

    Bus(int id, int index) {
      this.id = id;
      this.index = index;
    }

    public int getId() {
      return id;
    }
  }
}
