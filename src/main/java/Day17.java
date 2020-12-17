import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Day17 implements Day {

  private static char ACTIVE = '#';

  interface NeighborFetcher extends Function<Coordinate, Set<Coordinate>> {}

  @Override
  public String getInputPath() {
    return "src/main/resources/17.txt";
  }

  @Override
  public String part1(List<String> lines) {
    long count = runSimulation(lines, 6, this::getNeighbors3D);
    return "" + count;
  }

  @Override
  public String part2(List<String> lines) {
    long count = runSimulation(lines, 6, this::getNeighbors4D);
    return "" + count;
  }

  private long runSimulation(List<String> lines, int rounds, NeighborFetcher neighborFetcher) {
    Set<Coordinate> activePoints = parseInput(lines);
    for (int i = 0; i < rounds; i++) {
      doRound(activePoints, neighborFetcher);
    }
    return activePoints.size();
  }

  private Set<Coordinate> parseInput(List<String> lines) {
    Set<Coordinate> activePoints = new HashSet<>();
    for (int y = 0; y < lines.size(); y++) {
      for (int x = 0; x < lines.get(0).length(); x++) {
        char c = lines.get(y).charAt(x);
        if (c == ACTIVE) {
          activePoints.add(new Coordinate(x, y));
        }
      }
    }
    return activePoints;
  }

  private void doRound(Set<Coordinate> activePoints, NeighborFetcher neighborFetcher) {
    Set<Coordinate> activeCopy = new HashSet<>(activePoints);
    Set<Coordinate> pointsToEvaluate = pointsToEvaluate(activeCopy, neighborFetcher);
    pointsToEvaluate.forEach((c) -> {
      boolean active = activeCopy.contains(c);
      int count = countActiveNeighbors(activeCopy, c, neighborFetcher);
      if (active && (count < 2 || count > 3)) {
        activePoints.remove(c);
      } else if (!active && count == 3) {
        activePoints.add(c);
      }
    });
  }

  private Set<Coordinate> getNeighbors3D(Coordinate coord) {
    Set<Coordinate> neighbors = new HashSet<>();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
          Coordinate neighbor = new Coordinate(coord.x + i, coord.y + j, coord.z + k);
          if (!coord.equals(neighbor)) { //don't add self
            neighbors.add(neighbor);
          }
        }
      }
    }
    return neighbors;
  }

  private Set<Coordinate> getNeighbors4D(Coordinate coord) {
    Set<Coordinate> neighbors = new HashSet<>();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        for (int k = -1; k <= 1; k++) {
          for (int m = -1; m <= 1; m++) {
            Coordinate neighbor = new Coordinate(coord.x + i, coord.y + j, coord.z + k, coord.w + m);
            if (!coord.equals(neighbor)) { //don't add self
              neighbors.add(neighbor);
            }
          }
        }
      }
    }
    return neighbors;
  }

  private int countActiveNeighbors(Set<Coordinate> activePoints, Coordinate coord, NeighborFetcher neighborFetcher) {
    Set<Coordinate> neighbors = neighborFetcher.apply(coord);
    neighbors.retainAll(activePoints);
    return neighbors.size();
  }

  private Set<Coordinate> pointsToEvaluate(Set<Coordinate> activePoints, NeighborFetcher neighborFetcher) {
    Set<Coordinate> points = new HashSet<>();
    activePoints.forEach(coord -> {
      points.addAll(neighborFetcher.apply(coord));
      points.add(coord); // add self
    });
    return points;
  }
}
