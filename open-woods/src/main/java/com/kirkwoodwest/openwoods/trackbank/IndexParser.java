package com.kirkwoodwest.openwoods.trackbank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class IndexParser {
  public static List<Integer> parseIndexes(String input, Helper helper, int maxIndexes, int maxRange) {
    String[] selections = input.split("\\s*,\\s*");
    List<Integer> result = new ArrayList<>();


    //check if there are more than one results with an astrix
    long count = Arrays.stream(selections).filter(s -> s.endsWith("*")).count();
    if (count > 1) {
      throw new IllegalArgumentException("Only one selection can have an asterisk");
    }

    //check if there is one result with an astrix and parse that one last
    if (count == 1) {
      String[] selectionsWithoutAsterisk = Arrays.stream(selections).filter(s -> !s.endsWith("*")).toArray(String[]::new);
      for (String selection : selectionsWithoutAsterisk) {
        List<Integer> parsedSelection = parseSelection(selection, helper, maxRange);
        result.addAll(parsedSelection);
      }

      String selectionWithAsterisk = Arrays.stream(selections).filter(s -> s.endsWith("*")).findFirst().get();
      List<Integer> parsedSelection = parseAstrixSelection(selectionWithAsterisk, helper, result.size(), maxIndexes, maxRange);
      result.addAll(parsedSelection);
      return result;
    } else {
      //No Astrix
      for (String selection : selections) {
          List<Integer> parsedSelection = parseSelection(selection, helper, maxRange);
          result.addAll(parsedSelection);
      }
    }

    return result;
  }

  private static List<Integer> parseAstrixSelection(String selection, Helper helper, int usedIndexes, int totalIndexes, int maxRange) {
    // Check for asterisk at the end of the selection
    boolean endsWithAsterisk = selection.endsWith("*");
    if (endsWithAsterisk) {
      selection = selection.substring(0, selection.length() - 1); // Remove the asterisk for further processing
    }
    //make sure the selection doesn't have a hyphen
    if (containsHyphenOutsideQuotes(selection)) {
      throw new IllegalArgumentException("Selection cannot contain a hyphen when using an asterisk");
    }

    //get index of the selection
    int index = parsePart(selection, helper);
    //get the count of remaining indexes
    int remainingIndexes = totalIndexes - usedIndexes;
    //build the list of indexes from index and count up until our array size is the remainingIndexes
    List<Integer> parsedIndexes = new ArrayList<>();
    for (int i = 0; i < remainingIndexes; i++) {
      parsedIndexes.add(index + i);
    }
    return parsedIndexes;
  }

  private static List<Integer> parseSelection(String selection, Helper helper, int maxRange) {
    Pattern rangePattern = Pattern.compile("^(\"[^\"]*\"|\\d+)-(\"[^\"]*\"|\\d+)$");
    Matcher rangeMatcher = rangePattern.matcher(selection);

    if (rangeMatcher.matches()) {
      // Handle range selection
      String startPart = rangeMatcher.group(1);
      String endPart = rangeMatcher.group(2);

      int startIndex = isNumericOrString(startPart) ? parsePart(startPart, helper) : -1;
      int endIndex = isNumericOrString(endPart) ? parsePart(endPart, helper) : -1;

      if (startIndex == -1 || endIndex == -1) {
        throw new IllegalArgumentException("Invalid selection format: " + selection);
      }

      final int startIndexFinal = startIndex;
      final int endIndexFinal = endIndex;

      List<Integer> parsedIndexes = new ArrayList<>();
      if (startIndex <= endIndex) {
        parsedIndexes.addAll(IntStream.rangeClosed(startIndexFinal, endIndexFinal).boxed().toList());
      } else {
        parsedIndexes.addAll(IntStream.iterate(startIndexFinal, n -> n >= endIndexFinal, n -> n - 1).boxed().toList());
      }

      return parsedIndexes;
    }

    // Handle other cases (numeric indexes, track names, item+count)
    Pattern pattern = Pattern.compile("\"[^\"]+\"\\+\\d+|\"[^\"]+\"|\\d+\\+\\d+|\\d+");
    Matcher matcher = pattern.matcher(selection);

    List<Integer> parsedIndexes = new ArrayList<>();

    while (matcher.find()) {
      String match = matcher.group();
      String trackName;
      int startIndex, count;

      if (match.matches("\"\"[^\"]+\"\"\\+\\d+") || match.matches("\"[^\"]+\"\\+\\d+")) {
        // Handle track name (with or without extra quotes) with count
        String[] parts = match.split("\\+");
        trackName = parts[0].replaceAll("^\"|\"$", ""); // Remove all leading and trailing quotes
        count = Integer.parseInt(parts[1]) + 1;
        startIndex = helper.getIndexByName(trackName);
      } else if (match.matches("\\d+\\+\\d+")) {
        // Handle index selection with count
        String[] parts = match.split("\\+");
        startIndex = Integer.parseInt(parts[0]);
        count = Integer.parseInt(parts[1]) + 1;
      } else if (match.matches("\\d+")) {
        // Single Integer Selection
        startIndex = parsePart(match, helper);
        count = 1;
      } else if (match.matches("\"[^\"]+\"")) {
        // Handle single track name enclosed in double quotes
        trackName = match.substring(1, match.length() - 1); // Remove quotes
        startIndex = helper.getIndexByName(trackName);
        count = 1;
      } else {
        throw new IllegalArgumentException("Invalid selection format: " + match);
      }

      // Common logic for adding indexes
      if (startIndex != -1) {
        for (int i = 0; i < count; i++) {
          parsedIndexes.add(startIndex + i);
        }
      } else {
        throw new IllegalArgumentException("Track Name/Index Not Found: " + match);
      }
    }

    return parsedIndexes;
  }

  private static boolean isNumericOrString(String str) {
    return str.matches("\\d+") || (str.startsWith("\"") && str.endsWith("\""));
  }

  private static boolean containsHyphenOutsideQuotes(String str) {
    // Regular expression to find hyphens outside of quotes
    String regex = "\"[^\"]*\"|(-)";

    Matcher matcher = Pattern.compile(regex).matcher(str);
    while (matcher.find()) {
      if (matcher.group(1) != null) {
        // Found a hyphen outside of quotes
        return true;
      }
    }
    return false;
  }

  private static boolean isNumeric(String str) {
    return str.matches("\\d+") && !(str.startsWith("\"") && str.endsWith("\""));
  }

  private static int parsePart(String part, Helper helper) {
    if (part.startsWith("\"") && part.endsWith("\"")) {
      // Remove the double quotes and parse as a string
      int trackIndexByName = helper.getIndexByName(part.substring(1, part.length() - 1));
      if (trackIndexByName == -1) {
        throw new IllegalArgumentException("Track Name Not Found: " + part);
      }
      return trackIndexByName;
    } else {
      // Parse as a numeric value, indexes use -1 for one-based index
      return Integer.parseInt(part) -1;
    }
  }

  public static String getHelpString(String name) {
    return  """
     Parses Indexes from a string
       Examples:
          1              //Selects Track 1
          1-3            //Selects Tracks 1-3
          1-3, 5, 7-9      //Selects Tracks 1-3, 5, 7-9
          "Track 1"      //Selects First Track named "Track 1"
          "Alpha", "Track 2", "Omega" //Selects Tracks named "Alpha", "Track 2", "Omega"
          "Track 1", 2, "Omega" //Mixed selection Selects "Track 1", Track 2, "Omega"
          "Track 1"-"Track 5"  //Selects Tracks between named tracks "Track 1" through "Track 5"
          "Track 5"-"Track 1"  //Selects Tracks between named tracks "Track 5" through "Track 1"
          1-"Track 5"    //Selects starting from index 1 to the index of "Track 5"
          "Track 5"-1    //Selects starting from "Track 5" to the index of 1.
          "Omega"*       //Selects starting from "Omega" until the maxIndecies is hit.
          1-4, "Omega"*  //Selects tracks 1-4 and "Omega" until the maxIndecies is hit. That will be lower because 1-4 is already selected.
  """;
  }
}
