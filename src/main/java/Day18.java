import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day18 implements Day {

  private static final Map<String, BiFunction<Long, Long, Long>> operators;

  static {
    operators = new HashMap<>();
    operators.put("+", (a, b) -> a + b);
    operators.put("-", (a, b) -> a - b);
    operators.put("*", (a, b) -> a * b);
    operators.put("/", (a, b) -> a / b);
  }

  @Override
  public String getInputPath() {
    return "src/main/resources/18.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Parser parser = new Parser(this::getPrecedence1);
    long sum = lines.stream()
        .map(parser::parseLine)
        .mapToLong(Expression::eval)
        .sum();
    return "" + sum;
  }

  @Override
  public String part2(List<String> lines) {
    Parser parser = new Parser(this::getPrecedence2);
    long sum = lines.stream()
        .map(parser::parseLine)
        .mapToLong(Expression::eval)
        .sum();
    return "" + sum;
  }

  // all operators are the same
  private int getPrecedence1(String s) {
    return operators.containsKey(s) ? 1 : 0;
  }

  // give precedence to "+"
  private int getPrecedence2(String s) {
    if (s.equals("+")) {
      return 2;
    }
    return getPrecedence1(s);
  }

  static class Parser {

    Function<String, Integer> precedenceSupplier;

    Parser(Function<String, Integer> precedenceSupplier) {
      this.precedenceSupplier = precedenceSupplier;
    }

    Expression parseLine(String line) {
      // normalize input so we can just split terms on whitespace
      line = line.replaceAll("\\(", "( ");
      line = line.replaceAll("\\)", " )");
      Deque<String> terms = Arrays.stream(line.split(" "))
          .collect(Collectors.toCollection(LinkedList::new));
      Expression expression = parseExpression(terms, 0);
      if (!terms.isEmpty()) {
        throw new IllegalArgumentException("parsing ended before reading all terms");
      }
      return expression;
    }

    Expression parseExpression(Deque<String> terms, int precedence) {
      Expression expression = parseTerm(terms);
      while (!terms.isEmpty()) {
        String s = terms.removeFirst();
        int newPrecedence = precedenceSupplier.apply(s);
        if (newPrecedence <= precedence) {
          terms.addFirst(s);
          break;
        }
        Expression right = parseExpression(terms, newPrecedence);
        expression = new CompoundExpression(s, expression, right);
      }
      return expression;
    }

    // parses a constant or the expression inside of parentheses
    Expression parseTerm(Deque<String> terms) {
      String s = terms.removeFirst();
      try {
        int val = Integer.parseInt(s);
        return new ConstantExpression(val);
      } catch (NumberFormatException e) {
        // not an int, continue and see if it is parentheses
      }
      if (!s.equals("(")) {
        throw new IllegalArgumentException("this needs to be a (!");
      }
      Expression expression = parseExpression(terms, 0);
      s = terms.removeFirst();
      if (!s.equals(")")) {
        throw new IllegalArgumentException("this needs to be a )!");
      }
      return expression;
    }
  }

  interface Expression {
    long eval();
  }

  static class ConstantExpression implements Expression {
    private long val;

    ConstantExpression(int val) {
      this.val = val;
    }

    public long eval() {
      return val;
    }
  }

  static class CompoundExpression implements Expression {

    private String operator;
    private Expression left;
    private Expression right;

    public CompoundExpression(String operator, Expression left, Expression right) {
      this.operator = operator;
      this.left = left;
      this.right = right;
    }

    public long eval() {
      return operators.get(operator).apply(left.eval(), right.eval());
    }
  }
}
