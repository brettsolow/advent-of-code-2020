import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {

  public static void main(String[] args) {
    Day currentDay = new Day09();
    runDay(currentDay);
  }

  private static void runDay(Day day) {
    Path path = Paths.get(day.getInputPath());
    try (Stream<String> stream = Files.lines(path)) {
      List<String> lines = stream.collect(Collectors.toList());
      String answer1 = day.part1(lines);
      String answer2 = day.part2(lines);

      System.out.println("Part 1: " + answer1);
      System.out.println("Part 2: " + answer2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
