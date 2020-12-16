import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/12.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Direction currDir = Direction.E;
    Coordinate ship = new Coordinate(0, 0);

    List<Move> moves = parseInput(lines);
    for (Move m : moves) {
      switch (m.instruction) {
        case "L":
          currDir = currDir.rotate(-m.amount);
          break;
        case "R":
          currDir = currDir.rotate(m.amount);
          break;
        case "F":
          ship.x += currDir.xDir * m.amount;
          ship.y += currDir.yDir * m.amount;
          break;
        default:
          Direction d = Direction.fromStr(m.instruction);
          ship.x += d.xDir * m.amount;
          ship.y += d.yDir * m.amount;
      }
    }
    return "" + (Math.abs(ship.x) + Math.abs(ship.y));
  }

  @Override
  public String part2(List<String> lines) {
    Waypoint waypoint = new Waypoint(10, -1);

    Coordinate ship = new Coordinate(0, 0);

    List<Move> moves = parseInput(lines);
    for (Move move : moves) {
      switch (move.instruction) {
        case "L":
          waypoint.rotate(-move.amount);
          break;
        case "R":
          waypoint.rotate(move.amount);
          break;
        case "F":
          ship.y += waypoint.x * move.amount;
          ship.y += waypoint.y * move.amount;
          break;
        default:
          Direction d = Direction.fromStr(move.instruction);
          waypoint.x += d.xDir * move.amount;
          waypoint.y += d.yDir * move.amount;
      }
    }

    return "" + (Math.abs(ship.y) + Math.abs(ship.y));
  }

  private List<Move> parseInput(List<String> lines) {
    return lines.stream()
        .map(l -> new Move(l.substring(0, 1), Integer.parseInt(l.substring(1))))
        .collect(Collectors.toList());
  }

  static class Move {

    String instruction;
    int amount;

    Move(String instruction, int amount) {
      this.instruction = instruction;
      this.amount = amount;
    }
  }

  static class Waypoint extends Coordinate {
    Waypoint(int x, int y) {
      super(x,y);
    }

    void rotateClockWise90() {
      int newX = -y;
      int newY = x;
      y = x;
      x = newX;
    }

    void rotate(int degrees) {
      int rotations = ((360 + degrees) / 90) % 4;
      for (int i = 0; i < rotations; i++) {
        rotateClockWise90();
      }
    }
  }

  enum Direction {
    N(0, 0, -1),
    E(1, 1, 0),
    S(2, 0, 1),
    W(3, -1, 0);

    int value;
    int xDir;
    int yDir;

    Direction(int value, int xDir, int yDir) {
      this.value = value;
      this.xDir = xDir;
      this.yDir = yDir;
    }

    static Direction fromStr(String s) {
      return Direction.valueOf(s);
    }

    Direction fromValue(int value) {
      return Arrays.stream(Direction.values())
          .filter(d -> d.value == value)
          .findAny()
          .orElseThrow(() -> new IllegalArgumentException(""));
    }

    Direction rotate(int degrees) {
      int newDirection = (this.value + ((360 + degrees)/90))%4;
      return fromValue(newDirection);
    }
  }
}
