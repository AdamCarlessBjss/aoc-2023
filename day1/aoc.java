// https://adventofcode.com/2022/day/1
import java.nio.file.*;
import java.util.*;

Map<String, String> nums = Map.of(
  "one","1",
  "two","2",
  "three","3",
  "four","4",
  "five","5",
  "six","6",
  "seven","7",
  "eight","8",
  "nine","9"
);

ArrayList<String> lines = new ArrayList<>();

// load text file into `lines`
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { lines.add(l); });
}

// get the ends of a string and make them a number
int firstLast(String s) {
  var calVal = new String(new char[] {s.charAt(0), s.charAt(s.length() - 1)});
  return Integer.parseInt(calVal);
}

// turn a string with digits in it into a single number
int coordLineToInt(String line) {
  var n = line.replaceAll("[a-z]", "");
  return n.isBlank() ? 0 : firstLast(n);
}

// turn all number words into digits
String wordsToDigits(String s) {
  for (var k : nums.keySet()) {
    s = s.replaceAll(k, k+nums.get(k)+k);
  }
  return s;
}

int part1() {
  return lines.stream().mapToInt((l) -> coordLineToInt(l)).sum();
}

int part2() {
  return lines.stream().mapToInt((l) -> coordLineToInt(wordsToDigits(l))).sum();
}

void main(String[] args) throws Exception {
  slurp(args[0]);
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}
