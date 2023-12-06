// https://adventofcode.com/2023/day/5
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

record AlmanacEntry(long destStart, long srcStart, long range) {
  boolean srcInRange(long src) { return src >= srcStart && src <= srcStart+range; }
  long srcToDest(long src) { return srcInRange(src) ? destStart + src-srcStart : Long.MAX_VALUE; }
}

ArrayList<String> lines = new ArrayList<>();

// all the almanac section mappings
List<Long> seeds = new ArrayList<>();
ArrayList<AlmanacEntry> seed2soil = new ArrayList<>();
ArrayList<AlmanacEntry> soil2fert = new ArrayList<>();
ArrayList<AlmanacEntry> fert2water = new ArrayList<>();
ArrayList<AlmanacEntry> water2light = new ArrayList<>();
ArrayList<AlmanacEntry> light2temp = new ArrayList<>();
ArrayList<AlmanacEntry> temp2humd = new ArrayList<>();
ArrayList<AlmanacEntry> humd2loc = new ArrayList<>();

void main(String[] args) throws Exception {
  slurp(args[0]);
  parseAlmanac();
  System.out.println("part 1: " + part1());
  System.out.println("part 2: " + part2());
}

// load text file, leave parsing until later
void slurp(String filename) throws Exception {
  Files.lines(Path.of(filename)).forEach(l -> { lines.add(l); });
}

long part1() {
  return streamToMinLocation(seeds.stream());
}

// Construct a bunch of different streams from the seeds list and run part1 for each.
long part2() {
  long min = Long.MAX_VALUE;
  for (int i=0; i<seeds.size()-1; i+=2) {
    min = Math.min(min, streamToMinLocation(seedRange(i)));
  }
  return min;
}

long streamToMinLocation(Stream<Long> seedStream) {
  return seedStream.parallel().mapToLong(s -> chainMapsForSeed(s)).min().orElse(0L);
}

// part 2 - treat a pair of seed entries as the generator for a stream of seed ids
Stream<Long> seedRange(int seedIndex) {
  return LongStream.range(seeds.get(seedIndex), seeds.get(seedIndex)+seeds.get(seedIndex+1)).boxed();
}

// do the mapping logic thru all the almanac sections
long chainMapsForSeed(long seed) {
  long i = mapWithAlmanac(seed, seed2soil);
  i = mapWithAlmanac(i, soil2fert);
  i = mapWithAlmanac(i, fert2water);
  i = mapWithAlmanac(i, water2light);
  i = mapWithAlmanac(i, light2temp);
  i = mapWithAlmanac(i, temp2humd);
  i = mapWithAlmanac(i, humd2loc);
  return i;
}

// use an almanac section to map src to dest
long mapWithAlmanac(long src, ArrayList<AlmanacEntry> alm) {
  long dest = Long.MAX_VALUE;
  for (AlmanacEntry a : alm) { dest = Math.min(a.srcToDest(src), dest); }
  return dest == Long.MAX_VALUE ? src : dest;
}

// input parsing time! Split sections on title text
void parseAlmanac() {
  for (var i = 0; i<lines.size(); i++) {
    if (i == 0) seeds = parseSeeds(lines.get(0));
    if (lines.get(i).startsWith("seed")) i+= parseMap(i, seed2soil);
    if (lines.get(i).startsWith("soil")) i+= parseMap(i, soil2fert);
    if (lines.get(i).startsWith("fert")) i+= parseMap(i, fert2water);
    if (lines.get(i).startsWith("wate")) i+= parseMap(i, water2light);
    if (lines.get(i).startsWith("ligh")) i+= parseMap(i, light2temp);
    if (lines.get(i).startsWith("temp")) i+= parseMap(i, temp2humd);
    if (lines.get(i).startsWith("humi")) i+= parseMap(i, humd2loc);
  }
}

// parse a section of input into AlmanacEntries until a blank line is hit
int parseMap(int i, ArrayList<AlmanacEntry> map) {
  for (var j = 1; i+j < lines.size(); j++) {
    if (lines.get(i+j).isBlank()) return j;
    map.add(parseEntry(lines.get(i+j)));
  }
  return lines.size();
}

// parse the seeds line
List<Long> parseSeeds(String s) { 
  return Arrays.stream(s.split(" "))
    .skip(1)
    .mapToLong(Long::parseLong)
    .boxed()
    .toList();
}

// parse a single almanac map line
AlmanacEntry parseEntry(String s) {
  var parts = s.split(" +");
  return new AlmanacEntry(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]));
}