import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/06.txt";
  }

  @Override
  public String part1(List<String> lines) {
    long sum = getGroups(lines).stream()
        .mapToInt(this::countAnyYes)
        .sum();
    return String.valueOf(sum);
  }

  @Override
  public String part2(List<String> lines) {
    int sum = getGroups(lines).stream()
        .mapToInt(this::countEveryYes)
        .sum();
    return String.valueOf(sum);
  }

  private List<List<String>> getGroups(List<String> lines) {
    List<List<String>> groups = new ArrayList<>();
    List<String> currGroup = new ArrayList<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        groups.add(currGroup);
        currGroup = new ArrayList<>();
      } else {
        currGroup.add(line);
      }
    }
    groups.add(currGroup);
    return groups;
  }

  private int countAnyYes(List<String> group) {
    return (int) group.stream()
        .flatMapToInt(CharSequence::chars)
        .distinct()
        .count();
  }

  private Set<Character> intersection(Set<Character> a, Set<Character> b) {
    Set<Character> result = new HashSet<>(a);
    result.retainAll(b);
    return result;
  }

  private int countEveryYes(List<String> group) {
    return group.stream()
        .map(s -> s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()))
        .reduce(this::intersection)
        .map(Set::size)
        .orElseThrow(IllegalArgumentException::new);
  }
}
