import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ArrayUtilsTest {

  @Test
  public void testRotate() {
    Character[][] actual = new Character[][] {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };
    Character[][] expected = {
        {'7', '4', '1'},
        {'8', '5', '2'},
        {'9', '6', '3'}
    };
    actual = ArrayUtils.rotateClockwise(actual);
    assertTrue(Arrays.deepEquals(actual, expected));
  }
  @Test

  public void testFlipVertical() {
    Character[][] actual = new Character[][] {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };
    Character[][] expected = {
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'}
    };
    actual = ArrayUtils.flipVertical(actual);
    assertTrue(Arrays.deepEquals(actual, expected));
  }

  @Test
  public void testFlipHorizontal() {
    Character[][] actual = new Character[][] {
        {'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'}
    };
    Character[][] expected = {
        {'3', '2', '1'},
        {'6', '5', '4'},
        {'9', '8', '7'}
    };
    actual = ArrayUtils.flipHorizontal(actual);
    assertTrue(Arrays.deepEquals(actual, expected));
  }

}