import java.util.List;
import java.util.NoSuchElementException;

public class Day05 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/05.txt";
  }

  @Override
  public String part1(List<String> lines) {
    int maxSeatId = lines.stream()
        .map(Seat::fromBoardingPass)
        .mapToInt(Seat::getSeatId)
        .max().orElseThrow(NoSuchElementException::new);
    return String.valueOf(maxSeatId);
  }

  @Override
  public String part2(List<String> lines) {
    int[] seatsInList = lines.stream()
        .mapToInt(Seat::passToInt)
        .sorted()
        .toArray();
    for (int i = 0; i < seatsInList.length; i++) {
      if (seatsInList[i] - i != seatsInList[0]) {
        int missingSeatInt = seatsInList[i] - 1;
        Seat missingSeat = Seat.fromInt(missingSeatInt);
        return String.valueOf(missingSeat.getSeatId());
      }
    }
    throw new NoSuchElementException("seat not found");
  }

  static class Seat {

    int row;
    int col;

    static Seat fromInt(int n) {
      Seat seat = new Seat();
      seat.row = n >> 3;
      seat.col = n & 7;
      return seat;
    }

    static Seat fromBoardingPass(String boardingPass) {
      return fromInt(passToInt(boardingPass));
    }

    private static int passToInt(String boardingPass) {
      String binaryStr = boardingPass.replace("B", "1")
          .replace("F", "0")
          .replace("R", "1")
          .replace("L", "0");
      return Integer.parseInt(binaryStr, 2);
    }

    private int getSeatId() {
      return row * 8 + col;
    }
  }
}
