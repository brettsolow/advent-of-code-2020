import java.util.ArrayList;
import java.util.List;

public class Utils {
  static List<List<String>> splitOnEmptyLines(List<String> lines) {
    List<List<String>> result = new ArrayList<>();
    List<String> currGroup = new ArrayList<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        result.add(currGroup);
        currGroup = new ArrayList<>();
      } else {
        currGroup.add(line);
      }
    }
    result.add(currGroup);
    return result;
  }
}
