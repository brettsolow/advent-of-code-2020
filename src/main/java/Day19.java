import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/19.txt";
  }

  private List<String> getMessages(Iterator<String> iterator) {
    List<String> messages = new ArrayList<>();
    while (iterator.hasNext()) {
      String line = iterator.next();
      messages.add(line);
    }
    return messages;
  }

  private Map<Integer, Rule> getRules(Iterator<String> iterator) {
    Map<Integer, Rule> rules = new HashMap<>();
    while (iterator.hasNext()) {
      String line = iterator.next();
      if (line.isEmpty()) {
        break;
      }
      Rule rule = new Rule(line);
      rules.put(rule.number, new Rule(line));
    }
    return rules;
  }


  static class Rule {
    int number;
    String pattern;
    List<List<Integer>> patterns;

    private boolean matches(String s, Map<Integer, Rule> ruleMap) {
        Pattern compiledPattern = Pattern.compile(getPattern(ruleMap));
      boolean matches = compiledPattern.matcher(s).matches();
      if (matches) {
        System.out.println(s + " matches!");
      } else {
        System.out.println(s + " doesn't match");
      }
      return matches;
    }

    private String getPattern(Map<Integer, Rule> ruleMap) {
      if (pattern != null) {
        return pattern;
      }
      String result = "(";
      for (List<Integer> numList : patterns) {
        for (Integer n : numList) {
          result += ruleMap.get(n).getPattern(ruleMap);
        }
        result += "|";
      }
      result = result.substring(0, result.length() - 1); //remove trailing "|"
      result += ")";
      this.pattern = result;
      return result;
    }

    Rule(String line) {
      String[] parts = line.split(":");
      this.number = Integer.parseInt(parts[0]);
      String rhs = parts[1].trim();
      if (rhs.equals("\"a\"")) {
        this.pattern = "a";
      } else if (rhs.equals("\"b\"")) {
        this.pattern = "b";
      } else {
        String[] patternStrings = rhs.split("\\|");
        this.patterns = Arrays.stream(patternStrings)
            .map(this::patternFromString)
            .collect(Collectors.toList());
      }
    }

    private List<Integer> patternFromString(String s) {
      s = s.trim();
      return Arrays.stream(s.split(" "))
          .map(String::trim)
          .map(Integer::parseInt)
          .collect(Collectors.toList());
    }
  }

  @Override
  public String part1(List<String> lines) {
    Iterator<String> iterator = lines.iterator();
    Map<Integer, Rule> rules = getRules(iterator);
    List<String> messages = getMessages(iterator);
    Rule rule = rules.get(0);
    long count = messages.stream()
        .filter(m -> rule.matches(m, rules))
        .count();
    return "" + count;
  }

  @Override
  public String part2(List<String> lines) {
    Iterator<String> iterator = lines.iterator();
    Map<Integer, Rule> rules = getRules(iterator);
    List<String> messages = getMessages(iterator);

    String p42 = rules.get(42).getPattern(rules);
    String p31 = rules.get(31).getPattern(rules);
    Pattern all = Pattern.compile("^(?<fortyTwos>" + p42 + "+)(?<thirtyOnes>" + p31 + "+)");
    Pattern pc42 = Pattern.compile(p42);
    Pattern pc31 = Pattern.compile(p31);

    long count = messages.stream()
        .filter(m -> matches(m, all, pc31, pc42))
        .count();
    return "" + count;
  }

  private boolean matches(String m, Pattern all, Pattern p31, Pattern p42) {
    Matcher matcher = all.matcher(m);
    if (matcher.matches()) {
      Matcher m42 = p42.matcher(matcher.group("fortyTwos"));
      Matcher m31 = p31.matcher(matcher.group("thirtyOnes"));
      return countMatches(m31) < countMatches(m42);
    }

    return false;
  }

  private int countMatches(Matcher m) {
    int count = 0;
    while (m.find()) {
      count++;
    }
    return count;
  }
}
