import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day23 implements Day {

  private static final int NUM_ROUNDS_1 = 100;
  private static final int NUM_ROUNDS_2 = 10_000_000;
  private static final int NUM_CUPS_2 = 1_000_000;
  private static final int CUPS_TO_REMOVE = 3;


  @Override
  public String getInputPath() {
    return "src/main/resources/23.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Cups cups = new Cups();
    addCupsFromInput(cups, lines);
    for (int i = 0; i < NUM_ROUNDS_1; i++) {
      doRound(cups);
    }
    cups.currCup = cups.cupMap.get(1);
    return cups.toString().substring(1);
  }

  private void doRound(Cups cups) {
    Deque<Cup> removedCups = new LinkedList<>();
    for (int i = 0; i < CUPS_TO_REMOVE; i++) {
      Cup cupToRemove = cups.currCup.next;
      cups.remove(cupToRemove);
      removedCups.addLast(cupToRemove);
    }
    Cup destinationCup = cups.getDestinationCup();
    while (!removedCups.isEmpty()) {
      cups.add(removedCups.removeFirst(), destinationCup);
      destinationCup = destinationCup.next;
    }
    cups.currCup = cups.currCup.next;
  }

  static class Cups {

    Map<Integer, Cup> cupMap = new HashMap<>();
    Cup currCup;
    int minCup = 1;
    int maxCup;

    private Cup getDestinationCup() {
      int label = currCup.label - 1;
      while (!cupMap.containsKey(label)) {
        if (label < minCup) {
          label = maxCup;
        } else {
          label--;
        }
      }
      return cupMap.get(label);
    }

    private Cup getLastCup() {
      return currCup == null ? null : currCup.prev;
    }

    private void addToEnd(Cup cupToAdd) {
      add(cupToAdd, getLastCup());
    }

    private void add(Cup cupToAdd, Cup destination) {
      if (currCup == null) {
        // first cup:
        cupToAdd.next = cupToAdd;
        cupToAdd.prev = cupToAdd;
        currCup = cupToAdd;
      } else {
        destination.next.prev = cupToAdd;
        cupToAdd.next = destination.next;
        cupToAdd.prev = destination;
        destination.next = cupToAdd;
      }
      cupMap.put(cupToAdd.label, cupToAdd);
      minCup = Math.min(minCup, cupToAdd.label);
      maxCup = Math.max(maxCup, cupToAdd.label);
    }

    private void remove(Cup cup) {
      if (cup == currCup) {
        throw new IllegalStateException("can't remove curr cup");
      }
      cup.prev.next = cup.next;
      cup.next.prev = cup.prev;
      cupMap.remove(cup.label);
    }

    public String toString() {
      StringBuilder result = new StringBuilder();
      Cup cupToPrint = currCup;
      for (int i = 0; i < cupMap.size(); i++) {
        result.append(cupToPrint.label);
        cupToPrint = cupToPrint.next;
      }
      return result.toString();
    }
  }

  static class Cup {
    int label;
    Cup next;
    Cup prev;

    Cup(int label) {
      this.label = label;
    }

    public String toString() {
      return "" + label;
    }
  }

  private void addCupsFromInput(Cups cups, List<String> lines) {
    String line = lines.get(0);
    line.chars()
        .mapToObj(Character::getNumericValue)
        .map(Cup::new)
        .forEach(cups::addToEnd);
  }

  @Override
  public String part2(List<String> lines) {
    Cups cups = new Cups();
    addCupsFromInput(cups, lines);
    for (int i = cups.maxCup + 1; i <= NUM_CUPS_2; i++) {
      cups.addToEnd(new Cup(i));
    }
    for (int i = 0; i < NUM_ROUNDS_2; i++) {
      doRound(cups);
    }
    Cup one = cups.cupMap.get(1);
    long result = ((long) one.next.label) * one.next.next.label;
    return "" + result;
  }
}
