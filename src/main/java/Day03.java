import java.util.List;

public class Day03 implements Day {

  private static final char TREE_CHAR = '#';

  @Override
  public String getInputPath() {
    return "src/main/resources/03.txt";
  }

  @Override
  public String part1(List<String> lines) {
    return String.valueOf(countTrees(lines, 3, 1));
  }

  @Override
  public String part2(List<String> lines) {
    long result = (long) countTrees(lines, 1, 1) *
            countTrees(lines, 3, 1) *
            countTrees(lines, 5, 1) *
            countTrees(lines, 7, 1) *
            countTrees(lines, 1, 2);
    return String.valueOf(result);
  }

  private int countTrees(List<String> lines, int horizontalSlope, int verticalSlope) {
    if (lines.isEmpty()) {
      return 0;
    }
    int height = lines.size();
    int width = lines.get(0).length();
    int count = 0;
    for (int row = 0; row < height; row += verticalSlope) {
      int col = (row * horizontalSlope) % width;
      if (lines.get(row).charAt(col) == TREE_CHAR) {
        count++;
      }
    }
    return count;
  }
}
