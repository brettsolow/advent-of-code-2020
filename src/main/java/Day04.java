import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/04.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Passport> passports = parsePassports(lines);
    long validPassports = passports.stream()
        .filter(Passport::isValid1)
        .count();
    return String.valueOf(validPassports);
  }

  @Override
  public String part2(List<String> lines) {
    List<Passport> passports = parsePassports(lines);
    long validPassports = passports.stream()
        .filter(Passport::isValid2)
        .count();
    return String.valueOf(validPassports);
  }

  private static List<Passport> parsePassports(List<String> lines) {
    List<Passport> passports = new ArrayList<>();
    Passport currentPassport = new Passport();
    for (String line : lines) {
      if (line.isEmpty()) {
        passports.add(currentPassport);
        currentPassport = new Passport();
      } else {
        addLineToPassport(currentPassport, line);
      }
    }
    passports.add(currentPassport);
    return passports;
  }

  private static void addLineToPassport(Passport passport, String line) {
    String[] parts = line.split(" ");
    Arrays.stream(parts)
        .map(part -> part.split(":"))
        .forEach(entry -> passport.fields.put(entry[0], entry[1]));
  }

  static class Passport {

    private static final List<String> REQUIRED_FIELDS = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private Map<String, String> fields = new HashMap<>();

    private boolean isValid1() {
      for (String field : REQUIRED_FIELDS) {
        if (!fields.containsKey(field)) {
          return false;
        }
      }
      return true;
    }

    // part 2
    private static Pattern heightRegex = Pattern.compile("(?<value>\\d+)(?<unit>cm|in)");
    private static Pattern passportRegex = Pattern.compile("^\\d{9}$");
    private static Pattern hairRegex = Pattern.compile("#[\\da-f]{6}");
    private static Pattern eyeRegex = Pattern.compile("amb|blu|brn|gry|grn|hzl|oth");

    private boolean isValid2() {
      return isValidateByr() &&
          isValidIyr() &&
          isValidEyr() &&
          isValidHgt() &&
          isValidEcl() &&
          isValidHcl() &&
          isValidPid();
    }

    private boolean isValidYear(String field, int minYear, int maxYear) {
      String yearStr = fields.get(field);
      if (yearStr == null) {
        return false;
      }
      try {
        int year = Integer.parseInt(yearStr);
        return year >= minYear && year <= maxYear;
      } catch (NumberFormatException e) {
        return false;
      }
    }

    private boolean matchesRegex(String field, Pattern regex) {
      String val = fields.get(field);
      return val != null && regex.matcher(val).matches();
    }

    private boolean isValidPid() {
      return matchesRegex("pid", passportRegex);
    }

    private boolean isValidEcl() {
      return matchesRegex("ecl", eyeRegex);
    }

    private boolean isValidHcl() {
      return matchesRegex("hcl", hairRegex);
    }

    private boolean isValidHgt() {
      String height = fields.get("hgt");
      if (height == null) {
        return false;
      }
      Matcher matcher = heightRegex.matcher(height);
      if (!matcher.matches()) {
        return false;
      }
      int value = Integer.parseInt(matcher.group("value"));
      String unit = matcher.group("unit");
      if (unit.equals("cm")) {
        return value >= 150 && value <= 193;
      } else {
        return value >= 59 && value <= 76;
      }
    }

    private boolean isValidateByr() {
      return isValidYear("byr", 1920, 2002);
    }

    private boolean isValidIyr() {
      return isValidYear("iyr", 2010, 2020);
    }

    private boolean isValidEyr() {
      return isValidYear("eyr", 2020, 2030);
    }
  }
}
