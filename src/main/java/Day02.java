import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day02 {

  private static final String INPUT = "src/main/resources/02.txt";

  public static void main(String[] args) {
    Path path = Paths.get(INPUT);
    try (Stream<String> stream = Files.lines(path)) {
      List<Entry> entries = stream.map(Entry::fromInput)
          .collect(Collectors.toList());
      long numValidPt1 = entries.stream()
          .filter(Entry::isValidPart1)
          .count();
      long numValidPt2 = entries.stream()
          .filter(Entry::isValidPart2)
          .count();
      System.out.println("valid passwords pt1: " + numValidPt1);
      System.out.println("valid passwords pt2: " + numValidPt2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class Entry {
    private static Pattern inputRegex = Pattern.compile("(?<n1>\\d+)-(?<n2>\\d+)\\s+(?<char>.):\\s+(?<password>.+)");

    private String password;
    private char requiredChar;
    private int n1;
    private int n2;

    static Entry fromInput(String input) {
      Entry entry = new Entry();
      Matcher matcher = inputRegex.matcher(input);
      boolean matches = matcher.matches();
      if (!matches) {
        throw new IllegalArgumentException("invalid input: " + input);
      }
      entry.password = matcher.group("password");
      entry.requiredChar = matcher.group("char").charAt(0);
      entry.n1 = Integer.parseInt(matcher.group("n1"));
      entry.n2 = Integer.parseInt(matcher.group("n2"));
      return entry;
    }

    private boolean isValidPart1() {
      int minCount = n1;
      int maxCount = n2;
      long charCount = password.chars()
          .filter(ch -> ch == requiredChar)
          .count();
      return charCount >= minCount && charCount <= maxCount;
    }

    private boolean isValidPart2() {
      // n1 and n2 are 1-indexed
      char charAtN1 = password.charAt(n1 - 1);
      char charAtN2 = password.charAt(n2 - 1);
      return charAtN1 == requiredChar ^ charAtN2 == requiredChar;
    }
  }

}
