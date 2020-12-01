import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Advent of Code[About][Events][Shop][Settings][Log Out](anonymous user #1065625) var
 * y=2020;[Calendar][AoC++][Sponsors][Leaderboard][Stats] Our sponsors help make Advent of Code possible: GitHub - We're
 * hiring engineers to make GitHub fast. Interested? Email fast@github.com with details of exceptional performance work
 * you've done in the past. --- Day 1: Report Repair --- After saving Christmas five years in a row, you've decided to
 * take a vacation at a nice resort on a tropical island. Surely, Christmas will go on without you.
 * <p>
 * The tropical island has its own currency and is entirely cash-only. The gold coins used there have a little picture
 * of a starfish; the locals just call them stars. None of the currency exchanges seem to have heard of them, but
 * somehow, you'll need to find fifty of these coins by the time you arrive so you can pay the deposit on your room.
 * <p>
 * To save your vacation, you need to get all fifty stars by December 25th.
 * <p>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second
 * puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently,
 * something isn't quite adding up.
 * <p>
 * Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
 * <p>
 * For example, suppose your expense report contained the following:
 * <p>
 * 1721 979 366 299 675 1456 In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together
 * produces 1721 * 299 = 514579, so the correct answer is 514579.
 * <p>
 * Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you multiply
 * them together?
 * <p>
 * To begin, get your puzzle input.
 * <p>
 * Answer:
 * <p>
 * <p>
 * You can also [Share] this puzzle.
 */
public class Day01 {
  private static final String INPUT = "src/main/resources/01.txt";

  public static void main(String args[]) {
    Path path = Paths.get(INPUT);
    try (Stream<String> stream = Files.lines(path)) {
      Set<Integer> ints = stream.map(Integer::parseInt)
          .collect(Collectors.toSet());
      int part1Answer = solve1(ints);
      int part2Answer = solve2(ints);
      System.out.println("Part 1: " + part1Answer);
      System.out.println("Part 2: " + part2Answer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // part 1 (naive – O(N^2) )
  private static int solve1(Set<Integer> ints) {
    for (int n1 : ints) {
      for (int n2 : ints) {
        if (n1 + n2 == 2020) {
          return n1 * n2;
        }
      }
    }
    throw new IllegalArgumentException("No solution");
  }

  // part 1 (optimized – O(N) )
  private static int solve1Optimized(Set<Integer> ints) {
    for (int n1 : ints) {
      int n2 = 2020 - n1;
      if (ints.contains(n2)) {
        return n1 * n2;
      }
    }
    throw new IllegalArgumentException("No solution");
  }

  // part 2 (naive – O(N^3) )
  private static int solve2(Set<Integer> ints) {
    for (int n1 : ints) {
      for (int n2 : ints) {
        for (int n3 : ints) {
          if (n1 + n2 + n3 == 2020) {
            return n1 * n2 * n3;
          }
        }
      }
    }
    throw new IllegalArgumentException("No solution");
  }
}
