import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day08 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/08.txt";
  }

  @Override
  public String part1(List<String> lines) {
    List<Instruction> instructions = parseInput(lines);
    try {
      run(instructions);
    } catch (InvalidInputException e) {
      return String.valueOf(e.lastAccumulatorValue);
    }
    return "not found";
  }

  @Override
  public String part2(List<String> lines) {
    List<Instruction> instructions = parseInput(lines);
    for (int i = 0; i < instructions.size(); i++) {
      instructions.get(i).swap();
      try {
        int result = run(instructions);
        return String.valueOf(result);
      } catch (InvalidInputException e) {
        instructions.get(i).swap();
      }
    }
    return "not found";
  }

  private int run(List<Instruction> instructions) throws InvalidInputException {
    int accumulator = 0;
    Set<Integer> executed = new HashSet<>();
    int i = 0;
    while (i < instructions.size()) {
      if (executed.contains(i) || i < 0) {
        throw new InvalidInputException(accumulator);
      }
      executed.add(i);
      Instruction instruction = instructions.get(i);

      switch (instruction.operation) {
        case "jmp":
          i += instruction.argument;
          break;
        case "acc":
          accumulator += instruction.argument;
        case "nop":
        default:
          i++;
      }
    }
    if (i != instructions.size()) {
      throw new InvalidInputException(accumulator);
    }
    return accumulator;
  }

  static class InvalidInputException extends Exception {

    private int lastAccumulatorValue;

    InvalidInputException(int lastAccumulatorValue) {
      this.lastAccumulatorValue = lastAccumulatorValue;
    }
  }

  private List<Instruction> parseInput(List<String> lines) {
    return lines.stream().map(s -> {
      String[] parts = s.split(" ");
      String operation = parts[0];
      int argument = Integer.parseInt(parts[1]);
      return new Instruction(operation, argument);
    })
        .collect(Collectors.toList());
  }

  static class Instruction {

    private String operation;
    private int argument;

    Instruction(String operation, int argument) {
      this.operation = operation;
      this.argument = argument;
    }

    void swap() {
      if (operation.equals("nop")) {
        operation = "jmp";
      } else if (operation.equals("jmp")) {
        operation = "nop";
      }
    }
  }
}
