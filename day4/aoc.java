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

ArrayList<Card> cards = new ArrayList<>();

TreeSet<Integer> toNums(String n) {
  return new TreeSet<>(
    Arrays.stream(n.split(" +"))
    .map(Integer::parseInt)
    .collect(Collectors.toSet())
  );
}

Pattern cardPat = Pattern.compile("^Card +(\\d+)\\: ([\\d ]+)\\|([\\d ]+)$");
void parseCard(String l) {
  var m = cardPat.matcher(l);
  if (m.find()) {
    cards.add(new Card(Integer.valueOf(m.group(1)), toNums(m.group(2).trim()), toNums(m.group(3).trim())));
  }
}


long part1() {
  return cards.stream().mapToLong(c -> c.points()).sum();
}

long part2() {
  return 0;
}

// load text file into `cards`
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { parseCard(l); });
}

void main(String[] args) throws Exception {
  slurp(args[0]);
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}