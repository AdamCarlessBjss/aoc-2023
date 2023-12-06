// https://adventofcode.com/2023/day/6
import java.nio.file.*;
import java.util.*;

record Race(long time, long maxDistance) {
  long winWays() { 
    long ways = 0;
    for (long i=0; i<=time; i++) {
      if (i*(time-i) > maxDistance) ways++;
    }
    return ways;
  }
}

ArrayList<Race> races = new ArrayList<>();

void main(String[] args) throws Exception {
  System.out.println("part 1: " + part1(args[0]));
  System.out.println("part 2: " + part2(args[0]));
}

long part1(String filename) throws Exception {
  parseRaces(Files.lines(Path.of(filename)).toArray(String[]::new));
  return races.stream().mapToLong(r -> r.winWays()).reduce((a,b) -> a*b).orElse(0);
}

long part2(String filename) throws Exception {
  return parseRace(Files.lines(Path.of(filename)).toArray(String[]::new)).winWays();
}

void parseRaces(String[] lines) {
  var times = lines[0].split(" +");
  var distances = lines[1].split(" +");
  for (var i=1; i<times.length; i++) {
    races.add(new Race(Long.parseLong(times[i]), Long.parseLong(distances[i])));
  }
}

Race parseRace(String[] lines) {
  var times = lines[0].replaceAll(" +","").split(":");
  var distances = lines[1].replaceAll(" +","").split(":");
  return new Race(Long.parseLong(times[1]), Long.parseLong(distances[1]));
}