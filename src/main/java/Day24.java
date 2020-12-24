import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/24.txt";
  }

  private static final List<String> DIRECTIONS = Arrays.asList("e", "se", "sw", "w", "nw", "ne");
  private static final Pattern LINE_REGEX = Pattern.compile(String.join("|", DIRECTIONS));
  private static final int NUM_DAYS = 100;

  @Override
  public String part1(List<String> lines) {
    Map<Coordinate, Boolean> tiles = new HashMap<>();
    List<List<String>> directions = parseInput(lines);
    for (List<String> line : directions) {
      doLine(tiles, line);
    }
    long blackTileCount = tiles.values().stream()
        .filter(t -> t)
        .count();
    return "" + blackTileCount;
  }

  private void doLine(Map<Coordinate, Boolean> tiles, List<String> directions) {
    Coordinate currCoord = new Coordinate(0, 0);
    for (String d : directions) {
      currCoord = getCoordForDirection(currCoord, d);
    }
    boolean flipped = tiles.getOrDefault(currCoord, false);
    tiles.put(currCoord, !flipped);
  }

  private Map<Coordinate, Boolean> doDay(Map<Coordinate, Boolean> tiles) {
    addAdjacentWhiteTiles(tiles);
    Map<Coordinate, Boolean> result = new HashMap<>();
    tiles.forEach((coord, flipped) -> {
      int flippedNeighbors = countFlippedNeighbors(coord, tiles);
      if (flipped && (flippedNeighbors == 0 || flippedNeighbors > 2)) {
        result.put(coord, false);
      } else if (!flipped && flippedNeighbors == 2) {
        result.put(coord, true);
      } else {
        result.put(coord, flipped);
      }
    });
    return result;
  }

  private void addAdjacentWhiteTiles(Map<Coordinate, Boolean> tiles) {
    Set<Coordinate> tilesAdjacentToBlackTiles = tiles.entrySet().stream()
        .filter(Map.Entry::getValue) // get black tiles
        .flatMap(e -> DIRECTIONS.stream().map(d -> getCoordForDirection(e.getKey(), d))) // get their neighbors
        .collect(Collectors.toSet());
    tilesAdjacentToBlackTiles.forEach(t -> tiles.putIfAbsent(t, false));
  }

  private int countFlippedNeighbors(Coordinate cord, Map<Coordinate, Boolean> tiles) {
    return (int) DIRECTIONS.stream()
        .map(d -> getCoordForDirection(cord, d))
        .map(c -> tiles.getOrDefault(c, false))
        .filter(b -> b)
        .count();
  }

  /**
   * rows are offset:
   *
   * (0, 0) (1, 0) (2, 0) (3, 0)
   *    (0, 1) (1, 1) (2, 1)
   * (0, 2) (1, 2) (2, 2) (3, 2)
   */
  private Coordinate getCoordForDirection(Coordinate cord, String direction) {
    Coordinate result = cord.copy();
    switch (direction) {
      case "e":
        result.x++;
        break;
      case "se":
        result.y++;
        break;
      case "sw":
        result.y++;
        result.x--;
        break;
      case "w":
        result.x--;
        break;
      case "nw":
        result.y--;
        break;
      case "ne":
        result.y--;
        result.x++;
        break;
      default:
        throw new IllegalArgumentException();
    }
    return result;
  }

  private List<List<String>> parseInput(List<String> lines) {
    return lines.stream()
        .map(this::parseLine)
        .collect(Collectors.toList());
  }


  private List<String> parseLine(String line) {
    Matcher matcher = LINE_REGEX.matcher(line);
    List<String> result = new ArrayList<>();
    while (matcher.find()) {
      result.add(matcher.group(0));
    }
    return result;
  }

  @Override
  public String part2(List<String> lines) {
    Map<Coordinate, Boolean> tiles = new HashMap<>();
    List<List<String>> directions = parseInput(lines);
    for (List<String> line : directions) {
      doLine(tiles, line);
    }
    for (int i = 0; i < NUM_DAYS; i++) {
      tiles = doDay(tiles);
    }
    long blackTileCount = tiles.values().stream()
        .filter(t -> t)
        .count();
    return "" + blackTileCount;
  }
}
