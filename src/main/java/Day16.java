import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/16.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Data data = new Data(lines);
    int errorRate = data.otherTickets.stream()
        .flatMap(Collection::stream)
        .filter(n -> !validFieldForAnyRule(data.rules, n))
        .mapToInt(Integer::valueOf)
        .sum();
    return "" + errorRate;
  }

  private boolean validFieldForAnyRule(List<Rule> rules, int n) {
    Optional<Rule> validRule = rules.stream()
        .filter(r -> r.isValid(n))
        .findAny();
    return validRule.isPresent();
  }

  private boolean validTicketForAnyRule(List<Rule> rules, List<Integer> ticket) {
    return ticket.stream().allMatch(f -> validFieldForAnyRule(rules, f));
  }

  @Override
  public String part2(List<String> lines) {
    Data data = new Data(lines);
    List<List<Integer>> validTickets = data.otherTickets.stream()
        .filter(n -> validTicketForAnyRule(data.rules, n))
        .collect(Collectors.toList());

    Map<Integer, Set<String>> fieldNumToValidNames = new HashMap<>();
    for (int fieldNum = 0; fieldNum < data.myTicket.size(); fieldNum++) {
      Set<String> possibleFieldNames = possibleFieldNamesForRule(validTickets, data.rules, fieldNum);
      fieldNumToValidNames.put(fieldNum, possibleFieldNames);
    }

    Map<String, Integer> fieldNameToNum = getFieldNameMapping(fieldNumToValidNames);
    long result = fieldNameToNum.entrySet().stream()
        .filter(e -> e.getKey().startsWith("departure"))
        .map(e -> (long) data.myTicket.get(e.getValue()))
        .reduce(1L, (a, b) -> a * b);
    return "" + result;
  }

  private boolean isPossibleField(List<List<Integer>> tickets, int fieldNum, Rule rule) {
    return tickets.stream()
        .map(ticket -> ticket.get(fieldNum))
        .allMatch(rule::isValid);
  }

  private Set<String> possibleFieldNamesForRule(List<List<Integer>> tickets, List<Rule> rules, int fieldNum) {
    return rules.stream()
        .filter(rule -> isPossibleField(tickets, fieldNum, rule))
        .map(rule -> rule.field)
        .collect(Collectors.toSet());
  }

  private Map<String, Integer> getFieldNameMapping(Map<Integer, Set<String>> fieldNumToPossibleNames) {
    List<Map.Entry<Integer, Set<String>>> sortedList = fieldNumToPossibleNames.entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getValue().size()))
        .collect(Collectors.toList());
    Map<String, Integer> fieldNameToNum = new HashMap<>();
    for (Map.Entry<Integer, Set<String>> entry : sortedList) {
      Set<String> possibleFieldNames = entry.getValue();
      Integer fieldNum = entry.getKey();
      String currFieldName = possibleFieldNames.stream()
          .findAny()
          .orElseThrow(IllegalArgumentException::new);
      fieldNameToNum.put(currFieldName, fieldNum);
      fieldNumToPossibleNames.values().forEach(ruleSet -> ruleSet.remove(currFieldName));
    }
    return fieldNameToNum;
  }

  static class Rule {
    private static Pattern p = Pattern
        .compile("(?<field>.+): (?<min1>\\d+)-(?<max1>\\d+) or (?<min2>\\d+)-(?<max2>\\d+)");

    private String field;
    int min1;
    int max1;
    int min2;
    int max2;

    static Rule fromLine(String line) {
      Rule rule = new Rule();
      Matcher matcher = p.matcher(line);
      if (matcher.matches()) {
        rule.field = matcher.group("field");
        rule.min1 = Integer.parseInt(matcher.group("min1"));
        rule.max1 = Integer.parseInt(matcher.group("max1"));
        rule.min2 = Integer.parseInt(matcher.group("min2"));
        rule.max2 = Integer.parseInt(matcher.group("max2"));
        return rule;
      }
      throw new IllegalArgumentException("no match");
    }

    boolean isValid(int n) {
      return (min1 <= n && n <= max1) || (min2 <= n && n <= max2);
    }
  }

  static class Data {

    List<Rule> rules = new ArrayList<>();
    List<Integer> myTicket;
    List<List<Integer>> otherTickets = new ArrayList<>();

    private List<Integer> parseTicket(String line) {
      return Arrays.stream(line.split(","))
          .map(Integer::parseInt)
          .collect(Collectors.toList());
    }

    public Data(List<String> lines) {
      Iterator<String> iter = lines.iterator();
      while (iter.hasNext()) {
        String line = iter.next();
        if (line.isEmpty()) {
          break;
        }
        Rule rule = Rule.fromLine(line);
        rules.add(rule);
      }
      iter.next();
      myTicket = parseTicket(iter.next());
      iter.next();
      iter.next();
      while (iter.hasNext()) {
        String line = iter.next();
        List<Integer> ticket = parseTicket(line);
        otherTickets.add(ticket);
      }
    }
  }
}
