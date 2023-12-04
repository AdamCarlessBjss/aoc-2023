// https://adventofcode.com/2023/day/3
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

ArrayList<String> lines = new ArrayList<>();

ArrayList<NumPos> parts = new ArrayList<>();

record NumPos(int num, int line, int sx, int ex) {
  boolean nearTo(int x, int y) { return x >= sx-1 && x <= ex+1 && y >= line-1 && y <= line+1; }
}

// rules
// for every digit at (x,y), if there's a symbol at one of the following, it's
// part of a part number:
// (x-1,y), (x+1,y),
// (x-1,y-1), (x,y-1), (x+1,y-1), 
// (x-1,y+1), (x,y+1), (x+1,y+1) 

// given a line, find the start and end pos of every number
Pattern numPat = Pattern.compile("\\d+");
void findNums(String line, int lineNum) {
  var m = numPat.matcher(line);
  while (m.find()) {
    NumPos np = new NumPos(Integer.parseInt(m.group()), lineNum, m.start(), m.end()-1);
    if (hasSymbol(np)) parts.add(np);
  }  
}

// given a numPos, is it a part number (does it have at least one symbol near it)
Pattern symbolPat = Pattern.compile("[^\\.\\d]+");
boolean matchSymbolInRegion(int lineNum, int start, int end) {
  return symbolPat.matcher(lines.get(lineNum)).region(start, end).find();
}

boolean hasSymbol(NumPos num) {
  return
  matchSymbolInRegion(num.line()-1, num.sx()-1, num.ex()+2) ||
  matchSymbolInRegion(num.line(),   num.sx()-1, num.ex()+2) ||
  matchSymbolInRegion(num.line()+1, num.sx()-1, num.ex()+2);
}


// given a numPos, does it have a gear ('*' near it)
Pattern gearPat = Pattern.compile("\\*");
boolean matchGearInRegion(int lineNum, int start, int end) {
  return gearPat.matcher(lines.get(lineNum)).region(start, end).find();
}

boolean hasGear(NumPos num) {
  return
  matchGearInRegion(num.line()-1, num.sx()-1, num.ex()+2) ||
  matchGearInRegion(num.line(),   num.sx()-1, num.ex()+2) ||
  matchGearInRegion(num.line()+1, num.sx()-1, num.ex()+2);
}


int part1() {
  for (var i = 1; i < lines.size() - 1; i++) {
    findNums(lines.get(i), i);
  }
  return parts.stream().mapToInt(NumPos::num).sum();
}


// load text file into `lines`
// pad with dots all round to make bounds easier
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { lines.add("."+l+"."); });
  var dots = ".".repeat(lines.get(0).length());
  lines.addFirst(dots);
  lines.addLast(dots);
}


void main(String[] args) throws Exception {
  slurp(args[0]);
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}


int part2() {
  return 0;
}
