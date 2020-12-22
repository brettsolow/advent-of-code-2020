import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22 implements Day {

  @Override
  public String getInputPath() {
    return "src/main/resources/22.txt";
  }

  @Override
  public String part1(List<String> lines) {
    Round round = parseInput(lines);
    while (playRound(round)) {
      // continue;
    }
    Deque<Integer> winner = round.player1.isEmpty() ? round.player2 : round.player1;
    long score = scoreWinner(winner);

    return "" + score;
  }

  private boolean playRound(Round round) {
    if (round.player1.isEmpty() || round.player2.isEmpty()) {
      return false;
    }
    int one = round.player1.removeFirst();
    int two = round.player2.removeFirst();
    int winner = one >= two ? 1 : 2;
    round.updateDeck(winner, one, two);
    return true;
  }

  @Override
  public String part2(List<String> lines) {
    Round round = parseInput(lines);

    int winner = playRecursiveGame(round, new HashSet<>());
    Deque<Integer> winnerDeck = winner == 2 ?  round.player2 : round.player1;
    long score = scoreWinner(winnerDeck);

    return "" + score;
  }

  private int playRecursiveGame(Round round, Set<Round> playedRounds) {
    while (true) {
      if (playedRounds.contains(round)) {
        return 1;
      }
      if (round.player1.isEmpty()) {
        return 2;
      }
      if (round.player2.isEmpty()) {
        return 1;
      }
      playRecursiveRound(round, playedRounds);
    }
  }

  private void playRecursiveRound(Round round, Set<Round> playedRounds) {
    playedRounds.add(round.copy());
    int one = round.player1.removeFirst();
    int two = round.player2.removeFirst();
    int winner;
    if (one > round.player1.size() || two > round.player2.size()) {
      winner = one >= two ? 1 : 2;
    } else {
      // recursive
      Round roundCopy = new Round();
      roundCopy.player1 = limitDeck(round.player1, one);
      roundCopy.player2 = limitDeck(round.player2, two);
      winner = playRecursiveGame(roundCopy, new HashSet<>(playedRounds));
    }
    round.updateDeck(winner, one, two);
  }

  private Deque<Integer> limitDeck(Deque<Integer> deck, int limit) {
    return deck.stream()
        .limit(limit)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  private long scoreWinner(Deque<Integer> winner) {
    long score = 0;
    long multiplier = 1;

    while (!winner.isEmpty()) {
      score += multiplier * winner.removeLast();
      multiplier++;
    }
    return score;
  }

  private Round parseInput(List<String> lines) {
    List<List<String>> players = Utils.splitOnEmptyLines(lines);
    Round round = new Round();
    round.player1 = parseDeck(players.get(0));
    round.player2 = parseDeck(players.get(1));
    return round;
  }

  private Deque<Integer> parseDeck(List<String> lines) {
    return lines.stream()
        .skip(1)
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  static class Round {

    Deque<Integer> player1;
    Deque<Integer> player2;

    private void updateDeck(int winner, int one, int two) {
      if (winner == 1) {
        player1.addLast(one);
        player1.addLast(two);
      } else {
        player2.addLast(two);
        player2.addLast(one);
      }
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Round round = (Round) o;
      return Objects.equals(player1, round.player1) &&
          Objects.equals(player2, round.player2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(player1, player2);
    }

    Round copy() {
      Round newRound = new Round();
      newRound.player1 = new LinkedList<>(player1);
      newRound.player2 = new LinkedList<>(player2);
      return newRound;
    }

    @Override
    public String toString() {
      return "Round{" +
          "player1=" + player1 +
          ", player2=" + player2 +
          '}';
    }
  }
}
