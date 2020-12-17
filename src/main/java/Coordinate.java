import java.util.Objects;

public class Coordinate {
  public int x;
  public int y;
  public int z;
  public int w;

  Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  Coordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  Coordinate(int x, int y, int z, int w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w= w;
  }
  Coordinate copy() {
    return new Coordinate(x, y, z, w);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return x == that.x &&
        y == that.y &&
        z == that.z &&
        w == that.w;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z, w);
  }
}
