import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/02.txt";
  }

  @Override
  public String part1(List<String> lines) {
    long count = lines.stream()
        .map(Entry::fromInput)
        .filter(Entry::isValidPart1)
        .count();
    return String.valueOf(count);
  }

  @Override
  public String part2(List<String> lines) {
    long count = lines.stream()
        .map(Entry::fromInput)
        .filter(Entry::isValidPart2)
        .count();
    return String.valueOf(count);
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
