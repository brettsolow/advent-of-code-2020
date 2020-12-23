import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 implements Day {

  private static final Pattern titleRegex = Pattern.compile("Tile (?<id>\\d+):");
  private static final Character[][] MONSTER;
  static String MONSTER_STRING =
            "                  # \n"
          + "#    ##    ##    ###\n"
          + " #  #  #  #  #  #  ";
  static {
    MONSTER = gridFromLines(Arrays.asList(MONSTER_STRING.split("\n")));
  }

  @Override
  public String getInputPath() {
    return "src/main/resources/20.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Tile> tiles = parseInput(lines);
    Map<String, List<Tile>> edgeToTiles = getEdgeMap(tiles);
    long result = getCorners(tiles, edgeToTiles).stream()
        .mapToLong(t -> t.id)
        .reduce(1L, (a, b) -> a * b);
    return "" + result;
  }

  private List<Tile> getCorners(List<Tile> tiles, Map<String, List<Tile>> edgeToTiles) {
    return tiles.stream()
        .filter(t -> countEdgesWithMatch(t, edgeToTiles) == 2)
        .collect(Collectors.toList());
  }

  private Map<String, List<Tile>> getEdgeMap(List<Tile> tiles) {
    Map<String, List<Tile>> edgeToTiles = new HashMap<>();
    tiles.forEach(t -> {
      for (Side s : Side.values()) {
        String newEdge = t.getEdge(s);
        if (edgeToTiles.containsKey(reverse(newEdge))) {
          newEdge = reverse(newEdge);
        }
        List<Tile> tilesForEdge = edgeToTiles.getOrDefault(newEdge, new ArrayList<>());
        tilesForEdge.add(t);
        edgeToTiles.put(newEdge, tilesForEdge);
      }
    });
    return edgeToTiles;
  }

  private Tile matchingTileForSide(Tile t, Side s, Map<String, List<Tile>> edgeToTiles) {
    String edge = t.getEdge(s);
    if (!edgeToTiles.containsKey(edge)) {
      edge = reverse(edge);
    }
    List<Tile> matchingTiles = edgeToTiles.get(edge);
    // no tile with matching edge (matchingTiles includes self)
    if (matchingTiles == null) {
      return null;
    }
    if (matchingTiles.size() > 2) {
      throw new IllegalStateException("more than 1 tile matches this edge");
    }
    return matchingTiles.stream().filter(match -> match.id != t.id).findAny().orElse(null);
  }

  private int countEdgesWithMatch(Tile t, Map<String, List<Tile>> edgeToTiles) {
    return (int) Arrays.stream(Side.values())
        .map(s -> matchingTileForSide(t, s, edgeToTiles))
        .filter(Objects::nonNull)
        .count();
  }

  // assume no edge in more than 2 tiles ( true in provided input )
  @Override
  public String part2(List<String> lines) {
    List<Tile> tiles = parseInput(lines);
    Image image = buildImage(tiles);
    System.out.println(image);
    // assume images are always square
    return null;
  }

  private Image buildImage(List<Tile> tiles) {
    int imageSize = (int) Math.sqrt(tiles.size());
    int tileSize = tiles.get(0).tileSize;
    Image image = new Image(imageSize, tileSize);
    Map<String, List<Tile>> edgeMap = getEdgeMap(tiles);
    List<Tile> corners = getCorners(tiles, edgeMap);
    // pick one at random and stick it in the top left
    Tile leftTile = corners.get(0);
    while (matchingTileForSide(leftTile, Side.TOP, edgeMap) != null
        || matchingTileForSide(leftTile, Side.LEFT, edgeMap) != null) {
      leftTile.rotateClockwise();
    }
    image.tiles[0][0] = leftTile;
    for (int row = 0; row < imageSize; row++) {
      for (int col = 0; col < imageSize - 1; col++) {
        assert leftTile != null;
        Tile rightTile = matchingTileForSide(leftTile, Side.RIGHT, edgeMap);
        assert rightTile != null;
        rotateFlipRightTile(leftTile, rightTile);
        image.tiles[row][col + 1] = rightTile;
        leftTile = rightTile;
      }
      if (row != imageSize - 1) {
        Tile aboveTile = image.tiles[row][0];
        Tile belowTile = matchingTileForSide(aboveTile, Side.BOTTOM, edgeMap);
        rotateFlipBelowTile(aboveTile, belowTile);
        image.tiles[row+1][0] = belowTile;
        leftTile = belowTile;
      }
    }
    return image;
  }

  private void rotateFlipBelowTile(Tile aboveTile, Tile belowTile) {
    String abovesBottom = aboveTile.getEdge(Side.BOTTOM);
    for (int i = 0; i < Side.values().length; i++) {
      if (abovesBottom.equals(belowTile.getEdge(Side.TOP))) {
        return;
      }
      if (abovesBottom.equals(belowTile.getEdge(Side.BOTTOM))) {
        belowTile.flipVertical();
        return;
      }
      belowTile.rotateClockwise();
    }
    throw new IllegalStateException("below tile doesn't match any orientation");
  }


  void rotateFlipRightTile(Tile leftTile, Tile rightTile) {
    String leftsRight = leftTile.getEdge(Side.RIGHT);
    for (int i = 0; i < Side.values().length; i++) {
      if (leftsRight.equals(rightTile.getEdge(Side.LEFT))) {
        return;
      }
      if (leftsRight.equals(rightTile.getEdge(Side.RIGHT))) {
        rightTile.flipHorizontal();
        return;
      }
      rightTile.rotateClockwise();
    }
    throw new IllegalStateException("right tile doesn't match any orientation");
  }


  static class Image {

    int imageSize;
    int tileSize;

    Tile[][] tiles;

    Image(int imageSize, int tileSize) {
      this.imageSize = imageSize;
      this.tileSize = tileSize;
      tiles = new Tile[imageSize][imageSize];
    }

    char getChar(int row, int col) {
      Tile tile = tiles[row / tileSize][col / tileSize];
      return tile.data[row % tileSize][col % tileSize];
    }

    Character[][] removeBorders() {
      int newTileSize = tileSize - 2;
      int size = imageSize * newTileSize;
      Character[][] result = new Character[size][size];
      int row = 0;
      int col = 0;
      for (Tile[] tileRow: tiles) {
        col = 0;
        for (Tile tile: tileRow) {
          Character[][] t = tile.removeBorders();
          for (int i = 0; i < t.length; i++) {
            System.arraycopy(t[i], 0, result[row * newTileSize + i], col * newTileSize + 0, t.length);
          }
          col++;
        }
        row++;
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < tileSize * imageSize; i++) {
        StringBuilder row = new StringBuilder();
        for (int j = 0; j < tileSize * imageSize; j++) {
          row.append(getChar(i, j));
          if ((j +1) % tileSize == 0) {
            row.append(" ");
          }
        }
        if ((i+1) % tileSize == 0) {
          row.append("\n");
        }
        result.append(row);
        result.append("\n");
      }
      return result.toString();
    }
  }

  List<Tile> parseInput(List<String> lines) {
    return Utils.splitOnEmptyLines(lines).stream()
        .map(Day20::parseTile)
        .collect(Collectors.toList());
  }


  enum Side {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
  }


  private String reverse(String s) {
    return new StringBuilder(s).reverse().toString();
  }

  static class Tile {
    int id;
    int tileSize;
    Character[][] data;

    Tile(int tileSize) {
      this.tileSize = tileSize;
      this.data = new Character[tileSize][tileSize];
    }


    String getEdge(Side s) {
      Character[] result = new Character[tileSize];
      switch (s) {
        case TOP:
          result = data[0];
          break;
        case BOTTOM:
          result = data[tileSize - 1];
          break;
        case RIGHT:
          for (int i = 0; i < tileSize; i++) {
            result[i] = data[i][tileSize - 1];
          }
          break;
        case LEFT:
          for (int i = 0; i < tileSize; i++) {
            result[i] = data[i][0];
          }
          break;
        default:
          throw new IllegalArgumentException();

      }
      return null;
//      return (String) Arrays.stream(result).collect(Collectors.joining());
    }

    void rotateClockwise() {
      this.data = ArrayUtils.rotateClockwise(data);
    }

    void flipVertical() {
      this.data = ArrayUtils.flipVertical(data);
    }

    void flipHorizontal() {
      this.data = ArrayUtils.flipHorizontal(data);
    }

    Character[][] removeBorders() {
      return IntStream.range(1, tileSize - 1)
          .mapToObj(i -> Arrays.copyOfRange(data[i], 1, tileSize - 1))
          .toArray(Character[][]::new);
    }


    @Override
    public String toString() {
      StringBuilder result = new StringBuilder();
      for (Character[] row : data) {
        result.append(Arrays.toString(row));
        result.append("\n");
      }
      return result.toString();
    }
  }

  public static Tile parseTile(List<String> lines) {
    int tileSize = lines.size() - 1;
    Tile tile = new Tile(tileSize);
    Matcher matcher = titleRegex.matcher(lines.get(0));
    if (matcher.matches()) {
      tile.id = Integer.parseInt(matcher.group("id"));
    } else {
      throw new IllegalArgumentException("no id");
    }
    tile.data = gridFromLines(lines.subList(1, lines.size()));
    return tile;
  }

  // assume rectangle
  public static Character[][] gridFromLines(List<String> lines) {
    int height = lines.size();
    int width = lines.get(0).length();
    Character[][] result = new Character[height][width];
    for (int row = 0; row < height; row++) {
      String line = lines.get(row);
      for (int col = 0; col< width ; col++) {
        result[row][col] = line.charAt(col);
      }
    }
    return result;
  }

}
