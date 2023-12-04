// https://adventofcode.com/2023/day/4
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

record Card(int id, TreeSet<Integer> wins, TreeSet<Integer> nums) {
  long points() {
    var matches = matches();
    return matches == 0L ? 0 : (long)Math.pow(2, matches -1);
  }
  long matches() {
    return wins.stream().filter(w -> nums.contains(w)).count();
  }
}

// every card, as a map id->Card for easy access in part 2
Map<Integer, Card> cards = new HashMap<>();

// part 2 needs us to count how many copies of each card we have
// so keep a sorted list of card ids and their respective counts
TreeMap<Integer, Long> cardCounts = new TreeMap<>();

// turn a space separated list of numbers into a sorted list of ints
TreeSet<Integer> toNums(String n) {
  return new TreeSet<>(
    Arrays.stream(n.split(" +"))
    .map(Integer::parseInt)
    .collect(Collectors.toSet())
  );
}

// turn an input line into a Card and set its copies to 1
Pattern cardPat = Pattern.compile("^Card +(\\d+)\\: ([\\d ]+)\\|([\\d ]+)$");
void parseCard(String l) {
  var m = cardPat.matcher(l);
  if (m.find()) {
    var id = Integer.valueOf(m.group(1));
    cards.put(id, new Card(id, toNums(m.group(2).trim()), toNums(m.group(3).trim())));
    cardCounts.put(id, 1L);
  }
}

// do the bonkers card copying for part 2
void copyCards() {
  for (var id : cardCounts.keySet()) {
    var matchCount = cards.get(id).matches();
    var cardCount = cardCounts.get(id);
    for (var i = 1; i <= matchCount; i++) {
      var nextCardCount = cardCounts.get(id+i);
      if (nextCardCount == null) continue;
      cardCounts.put(id+i, nextCardCount+cardCount);
    }
  }
}

long part1() {
  return cards.values().stream().mapToLong(c -> c.points()).sum();
}

long part2() {
  copyCards();
  return cardCounts.values().stream().mapToLong(i -> i).sum();
}

// load text file and parse into useful structures
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { parseCard(l); });
}

void main(String[] args) throws Exception {
  slurp(args[0]);
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}