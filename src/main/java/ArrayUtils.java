import java.util.Arrays;

public class ArrayUtils {

  public static Character[][] rotateClockwise(Character[][] grid) {
    int height = grid.length;
    int width = grid[0].length;
    Character[][] result = new Character[width][height];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        result[j][height - 1 - i] = grid[i][j];
      }
    }
    return result;
  }

  // (0, 1) -> (0, length-1-1)
  public static Character[][] flipHorizontal(Character[][] grid) {
    int height = grid.length;
    int width = grid[0].length;
    Character[][] result = new Character[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        result[i][width - 1 - j] = grid[i][j];
      }
    }
    return result;
  }

 static Character[][] flipVertical(Character[][] grid) {
    Character[][] result = rotateClockwise(grid);
    result = rotateClockwise(result);
    result = flipHorizontal(result);
    return result;
  }

  static String toString(Character[][] grid) {
    StringBuilder result = new StringBuilder();
    for (Character[] row : grid) {
      for(char c : row) {
        result.append(c);
      }
      result.append("\n");
    }
    return result.toString();
  }

  static Character[][] copy(Character[][] grid) {
    return Arrays.stream(grid)
        .map(a -> a.clone())
        .toArray(Character[][]::new);
  }
}
