import java.util.Arrays;
import java.util.List;

public class Day11 implements Day {

  private static Coordinate[] DIRECTIONS = Arrays.stream(new int[][] {
      {-1, -1}, {-1, 0}, {-1, 1},
      { 0, -1},          { 0, 1},
      { 1, -1}, { 1, 0}, { 1, 1}
  }).map(a -> new Coordinate(a[1], a[0])).toArray(Coordinate[]::new);

  private static final char FLOOR = '.';
  private static final char OCCUPIED = '#';
  private static final char EMPTY = 'L';

  @Override
  public String getInputPath() {
    return "src/main/resources/11.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Character[][] seatMap = parseInputChars(lines);
    AdjacentSeatCounter adjacentSeatCounter = new AdjacentSeatCounter();
    while (updateMap(seatMap, adjacentSeatCounter, 4)) {
      continue;
    }
    return "" + countTotalOccupied(seatMap);
  }

  private boolean updateMap(Character[][] seatMap, OccupiedSeatCounter seatCounter, int occupiedSeatLimit) {
    boolean changed = false;
    Character[][] copy = copy(seatMap);
    for (int y = 0; y < seatMap.length; y++) {
      for (int x = 0; x < seatMap[0].length; x++) {
        Coordinate seat = new Coordinate(x, y);
        char curr = copy[y][x];
        int adjacentSeats = seatCounter.countSeats(copy, seat);
        if (curr == EMPTY && adjacentSeats == 0) {
          seatMap[y][x] = OCCUPIED;
          changed = true;
        } else if (curr == OCCUPIED && adjacentSeats >= occupiedSeatLimit) {
          seatMap[y][x] = EMPTY;
          changed = true;
        }
      }
    }
    return changed;
  }

  private int countTotalOccupied(Character[][] map) {
    return (int) Arrays.stream(map)
        .flatMap(Arrays::stream)
        .filter(s -> s == OCCUPIED)
        .count();
  }


  private static boolean inBounds(Character[][] map, Coordinate seat) {
    return (seat.x >= 0 && seat.y >= 0 &&
        seat.y < map.length && seat.x < map[0].length);
  }

  private Character[][] copy(Character[][] map) {
    return Arrays.stream(map)
        .map(Character[]::clone)
        .toArray(Character[][]::new);
  }

  private Character[][] parseInputChars(List<String> lines) {
    return lines.stream()
        .map(line -> line.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
        .toArray(Character[][]::new);
  }


  interface OccupiedSeatCounter {

    int countSeats(Character[][] map, Coordinate seat);
  }

  static class AdjacentSeatCounter implements OccupiedSeatCounter {

    private boolean occupiedAdjacentInDirection(Character[][] map, Coordinate seat, Coordinate direction) {
      Coordinate adjacentSeat = getSeatInDirection(seat, direction);
      return inBounds(map, adjacentSeat) && map[adjacentSeat.y][adjacentSeat.x] == OCCUPIED;
    }

    @Override
    public int countSeats(Character[][] map, Coordinate seat) {
      return (int) Arrays.stream(DIRECTIONS)
          .filter(d -> occupiedAdjacentInDirection(map, seat, d))
          .count();
    }
  }

  static class VisibleSeatCounter implements OccupiedSeatCounter {

    private boolean occupiedVisibleInDirection(Character[][] map, Coordinate seat, Coordinate direction) {
      Coordinate adjacentSeat = getSeatInDirection(seat, direction);
      while (inBounds(map, adjacentSeat)) {
        if (map[adjacentSeat.y][adjacentSeat.x] == OCCUPIED) {
          return true;
        }
        if (map[adjacentSeat.y][adjacentSeat.x] == EMPTY) {
          return false;
        }
        adjacentSeat = getSeatInDirection(adjacentSeat, direction);
      }
      return false;
    }

    @Override
    public int countSeats(Character[][] map, Coordinate seat) {
      return (int) Arrays.stream(DIRECTIONS)
          .filter(d -> occupiedVisibleInDirection(map, seat, d))
          .count();
    }
  }


  private static Coordinate getSeatInDirection(Coordinate seat, Coordinate direction) {
    Coordinate adjacentSeat = seat.copy();
    adjacentSeat.y += direction.y;
    adjacentSeat.x += direction.x;
    return adjacentSeat;
  }

  @Override
  public String part2(List<String> lines) {
    Character[][] map = parseInputChars(lines);
    VisibleSeatCounter visibleSeatCounter = new VisibleSeatCounter();
    while (updateMap(map, visibleSeatCounter, 5)) {
      continue;
    }

    return "" + countTotalOccupied(map);
  }
}
