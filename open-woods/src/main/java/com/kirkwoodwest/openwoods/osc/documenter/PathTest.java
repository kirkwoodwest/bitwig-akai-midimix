package com.kirkwoodwest.openwoods.osc.documenter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PathTest {
  public static List<Result> test(String path, Map<String, String> indexedPaths) {
    List<Result> results = new ArrayList<>();
    for (Map.Entry<String, String> entry : indexedPaths.entrySet()) {
      String key = entry.getKey();
      String description = entry.getValue();
      String regex = key.replace("*", "(.+)");
      Pattern pattern = Pattern.compile(regex);
      java.util.regex.Matcher matcher = pattern.matcher(path);
      if (matcher.matches()) {
        String index = matcher.group(1);
        ResultType resultType = index.startsWith("1/") ?
                ResultType.IS_INDEXED_AND_IS_ONE : ResultType.IS_INDEXED_NOT_ONE;
        results.add(new Result(resultType, key, description));
      }
    }
    if (results.isEmpty()) {
      results.add(new Result(ResultType.NOT_INDEXED, null, null));
    }
    return results;
  }

  public static class Result {
    private final ResultType resultType;
    private final String matchedPath;
    private final String description;

    public Result(ResultType resultType, String matchedPath, String description) {
      this.resultType = resultType;
      this.matchedPath = matchedPath;
      this.description = description;
    }

    public ResultType getResultType() {
      return resultType;
    }

    public String getMatchedPath() {
      return matchedPath;
    }

    public String getDescription() {
      return description;
    }
  }

  public enum ResultType {
    IS_INDEXED_AND_IS_ONE,   // It's an indexed path and the index is 1
    IS_INDEXED_NOT_ONE,     // It's an indexed path but the index is not 1
    NOT_INDEXED             // It's not an indexed path
  }
}

// Example usage:
// Assuming indexedPaths is populated with paths including wildcards as described.
