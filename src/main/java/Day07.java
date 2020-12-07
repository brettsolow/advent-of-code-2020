import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/07.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Map<String, Bag> bags = getBags(lines);
    int numContainingBags = 0;
    Bag shinyGoldBag = bags.get("shiny gold");
    Set<Bag> alreadyChecked = new HashSet<>();
    Queue<Bag> bagsToCheck = new LinkedList<>();
    bagsToCheck.add(shinyGoldBag);
    while (!bagsToCheck.isEmpty()) {
      Bag bag = bagsToCheck.poll();
      if (!alreadyChecked.contains(bag)) {
        alreadyChecked.add(bag);
        numContainingBags++;
        bag.outerBags.forEach(b -> {
          bagsToCheck.add(b);
        });
      }
    }
    return String.valueOf(numContainingBags - 1);
  }

  @Override
  public String part2(List<String> lines) {
    Map<String, Bag> bags = getBags(lines);
    Bag shinyGoldBag = bags.get("shiny gold");
    long count = getBagCount(shinyGoldBag) - 1;
    return String.valueOf(count);

  }

  private long getBagCount(Bag bag) {
    int total = 1;
    if (bag.innerBags.isEmpty()) {
      return total;
    }
    for (Map.Entry<Bag, Integer> entry : bag.innerBags.entrySet()) {
      int currCount = entry.getValue();
      total += currCount * getBagCount(entry.getKey());
    }
    return total;
  }

  private Map<String, Bag> getBags(List<String> lines) {
    Map<String, Bag> bags = new HashMap<>();
    lines.stream().forEach(line -> {
      addBag(bags, line);
    });
    return bags;
  }

  private static void addBag(Map<String, Bag> bags, String line) {
    Pattern inputRegex = Pattern.compile("(?<outerColor>.+) bags contain (?<innerBags>.+)\\.");
    Pattern innerBagsRegex = Pattern.compile("(?<count>\\d+) (?<color>.+?) bags?");

    Matcher matcher = inputRegex.matcher(line);
    matcher.matches();
    String outerColor = matcher.group("outerColor");
    Bag outerBag = bags.getOrDefault(outerColor, new Bag(outerColor));
    bags.put(outerColor, outerBag);
    String innerBagStr = matcher.group("innerBags");
    if (innerBagStr.equals("no other bags")) {
      return;
    }

    Matcher innerBagMatcher = innerBagsRegex.matcher(innerBagStr);
    while (innerBagMatcher.find()) {
      String innerBagColor = innerBagMatcher.group("color");
      int innerBagCount = Integer.parseInt(innerBagMatcher.group("count"));
      Bag innerBag = bags.getOrDefault(innerBagColor, new Bag(innerBagColor));
      bags.put(innerBagColor, innerBag);
      outerBag.innerBags.put(innerBag, innerBagCount);
      innerBag.outerBags.add(outerBag);
    }
  }

  static class Bag {
    private String color;
    private Map<Bag, Integer> innerBags = new HashMap<>();
    private Set<Bag> outerBags = new HashSet<>();

    Bag(String color) {
      this.color = color;
    }

    @Override
    public int hashCode() {
      return Objects.hash(color);
    }
  }
}
