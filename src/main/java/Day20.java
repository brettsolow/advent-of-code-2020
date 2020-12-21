import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day20 implements Day {

  private static final int TILE_SIZE = 10;
  private static final Pattern titleRegex = Pattern.compile("Tile (?<id>\\d+):");

  @Override
  public String getInputPath() {
    return "src/main/resources/20.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Tile> tiles = parseInput(lines);
    Map<String, List<Integer>> edgeToIds = new HashMap<>();
    tiles.forEach(t -> {
      for (Edge e : Edge.values()) {
        String newEdge = t.getEdge(e);
        if (edgeToIds.containsKey(reverse(newEdge))) {
          newEdge = reverse(newEdge);
        }
        List<Integer> ids = edgeToIds.getOrDefault(newEdge, new ArrayList<>());
        ids.add(t.id);
        edgeToIds.put(newEdge, ids);
      }
    });
    long result = tiles.stream()
        .filter(t -> countEdgesWithMatch(t, edgeToIds) == 2)
        .mapToLong(t -> t.id)
        .reduce(1L, (a, b) -> a * b);
    return "" + result;
  }

  private int countEdgesWithMatch(Tile t, Map<String, List<Integer>> edgeToIds) {
    int matches = 0;
    for (Edge e : Edge.values()) {
      String edge = t.getEdge(e);
      if (edgeToIds.get(edge).size() > 1) {
        matches++;
        // matched in same orientation
        //      } else if (edgeToIds.containsKey(reverse(edge))) {
        //        List<Integer> reverseEdges = edgeToIds.get(reverse(edge));
        //        if (reverseEdges.size() > 1 || (reverseEdges.size() == 1 && reverseEdges.get(0) != t.id)) {
        //          matches++;
        //        }
      }
    }
    return matches;
  }

  @Override
  public String part2(List<String> lines) {
    return null;
  }

  List<Tile> parseInput(List<String> lines) {
    List<Tile> tiles = Utils.splitOnEmptyLines(lines).stream()
        .map(Day20::parseTile)
        .collect(Collectors.toList());
    return tiles;
  }

  enum Edge {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT;
  }


  private String reverse(String s) {
    return new StringBuilder(s).reverse().toString();
  }

  static class Tile {

    int id;
    char[][] data = new char[TILE_SIZE][TILE_SIZE];

    String getEdge(Edge e) {
      char[] result = new char[TILE_SIZE];
      switch (e) {
        case TOP:
          result = data[0];
          break;
        case BOTTOM:
          result = data[TILE_SIZE - 1];
          break;
        case RIGHT:
          for (int i = 0; i < TILE_SIZE; i++) {
            result[i] = data[i][TILE_SIZE - 1];
          }
          break;
        case LEFT:
          for (int i = 0; i < TILE_SIZE; i++) {
            result[i] = data[i][0];
          }
          break;
        default:
          throw new IllegalArgumentException();
      }
      return new String(result);
    }
  }

  public static Tile parseTile(List<String> lines) {
    Tile tile = new Tile();
    Matcher matcher = titleRegex.matcher(lines.get(0));
    if (matcher.matches()) {
      tile.id = Integer.parseInt(matcher.group("id"));
    } else {
      throw new IllegalArgumentException("no id");
    }
    for (int i = 0; i < TILE_SIZE; i++) {
      String line = lines.get(i + 1);
      for (int j = 0; j < TILE_SIZE; j++) {
        tile.data[i][j] = line.charAt(j);
      }
    }
    return tile;
  }

}
