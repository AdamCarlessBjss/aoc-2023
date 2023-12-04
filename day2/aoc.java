// https://adventofcode.com/2023/day/2
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

// a set of coloured cubes
record CubeSet(int red, int green, int blue) {
  boolean greaterThan(CubeSet b) { 
    return red > b.red() || green > b.green() || blue > b.blue(); 
  }
  int power() { return red * green * blue; }
}

// a game has an id and a set of cubes
record Game(int id, CubeSet max){
  int possible(CubeSet limit) { return max.greaterThan(limit) ? 0 : id; }
}

// we play a load of games
ArrayList<Game> games = new ArrayList<>();


void main(String[] args) throws Exception {
  slurp(args[0]);
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}

// load text file and parse into games
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { parseGame(l); });
}

int part1() {
  CubeSet limit = new CubeSet(12, 13, 14);
  return games.stream().mapToInt((g) -> g.possible(limit)).sum();
}

int part2() {
  return games.stream().mapToInt((g) -> g.max().power()).sum();
}

// turn input string into a game with known colour maxes
void parseGame(String game) {
  games.add(new Game(gameId(game), maxes(game)));
}

// find the game id (don't trust them to be a sequence!)
Pattern idRe = Pattern.compile("^Game (\\d+)\\:");
int gameId(String game) {
  Matcher m = idRe.matcher(game);
  m.find();
  return Integer.parseInt(m.group(1));
}

// find the maxes for each colour in a game
Pattern redRe = Pattern.compile("(\\d+) red");
Pattern greenRe = Pattern.compile("(\\d+) green");
Pattern blueRe = Pattern.compile("(\\d+) blue");
CubeSet maxes(String game) {
  return new CubeSet(
    getMaxMatch(redRe.matcher(game)),
    getMaxMatch(greenRe.matcher(game)),
    getMaxMatch(blueRe.matcher(game))
  );
}

// Java's regexp handling _really_ needs improving
// especially now streams and lambdas exist.
int getMaxMatch(Matcher m) {
  int max = 0;
  while (m.find()) {
    int i = Integer.parseInt(m.group(1));
    max = i > max ? i : max;
  }
  return max;
}