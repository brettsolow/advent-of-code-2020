import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/21.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Food> foods = parseInput(lines);
    Map<String, Set<String>> allergenToPossibleIngredients = getAllergenToPossibleIngredients(foods);
    Set<String> possibleIngredientsWithAllergens = allergenToPossibleIngredients.values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
    long count = foods.stream()
        .flatMap(food -> food.ingredients.stream())
        .filter(i -> !possibleIngredientsWithAllergens.contains(i))
        .count();
    return "" + count;
  }

  Map<String, Set<String>> getAllergenToPossibleIngredients(List<Food> foods) {
    Map<String, Set<String>> allergenToPossibleIngredients = new HashMap<>();
    foods.forEach(food -> {
      food.allergens.forEach(allergen -> {
        Set<String> possibleIngredients = allergenToPossibleIngredients
            .getOrDefault(allergen, new HashSet<>(food.ingredients));
        possibleIngredients.retainAll(food.ingredients);
        allergenToPossibleIngredients.put(allergen, possibleIngredients);
      });
    });
    return allergenToPossibleIngredients;
  }

  @Override
  public String part2(List<String> lines) {
    List<Food> foods = parseInput(lines);
    Map<String, Set<String>> allergenToPossibleIngredients = getAllergenToPossibleIngredients(foods);
    Map<String, String> allergenToIngredient = new HashMap<>();
    Queue<String> confirmedAllergensToProcess = allergenToPossibleIngredients.entrySet().stream()
        .filter(e -> e.getValue().size() == 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toCollection(LinkedList::new));
    while (!confirmedAllergensToProcess.isEmpty()) {
      String allergen = confirmedAllergensToProcess.poll();
      String ingredient = allergenToPossibleIngredients.get(allergen).stream()
          .findAny()
          .orElseThrow(IllegalStateException::new);
      allergenToIngredient.put(allergen, ingredient);
      allergenToPossibleIngredients.remove(allergen);
      allergenToPossibleIngredients.forEach((otherAllergen, otherIngredients) -> {
        if (otherIngredients.remove(ingredient) && otherIngredients.size() == 1) {
          // this allergen just got narrowed down to 1 ingredient, queue it to process
          confirmedAllergensToProcess.add(otherAllergen);
        }
      });
    }
    if (!allergenToPossibleIngredients.isEmpty()) {
      throw new IllegalStateException("Remaining allergens didn't get processed");
    }
    return allergenToIngredient.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(Map.Entry::getValue)
        .collect(Collectors.joining(","));
  }

  List<Food> parseInput(List<String> lines) {
    return lines.stream()
        .map(Food::new)
        .collect(Collectors.toList());
  }

  static class Food {

    Set<String> ingredients;
    Set<String> allergens;

    Food(String line) {
      String[] parts = line.split(" \\(contains ");
      ingredients = new HashSet<>(Arrays.asList(parts[0].split(" ")));
      allergens = new HashSet<>(Arrays.asList(parts[1].substring(0, parts[1].length() - 1).split(", ")));
    }
  }
}
