package com.kirkwoodwest.openwoods.osc.documenter;
import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OscDocumenterPrinter {
  private final OscDocumenter documenter;
  private final String filePath;

  public OscDocumenterPrinter(ControllerHost host, OscDocumenter documenter) {
    this.documenter = documenter;
    this.filePath = FileUtil.getPath(host, "Osc Documentation.md");
  }

  public void printToFile() {
    File file = new File(filePath);
    try {
      file.createNewFile();
      FileWriter fw = new FileWriter(file, false);
      BufferedWriter bw = new BufferedWriter(fw);

      List<String> sortedPaths = documenter.getPaths().stream()
              .sorted(this::comparePaths)
              .collect(Collectors.toList());

      String currentHeader = "";
      Set<String> printedPatterns = new HashSet<>();
      List<String> tableRows = new ArrayList<>();

      for (String path : sortedPaths) {
        String newHeader = getHeader(path);
        if (!newHeader.equals(currentHeader)) {
          if (!tableRows.isEmpty()) {
            bw.write(String.join("\n", tableRows));
            bw.write("\n");
            tableRows.clear();
          }
          bw.write("\n## " + newHeader + "\n\n");
          bw.write("| Path | Description | Type Tag | Received | Sent |\n");
          bw.write("|------|-------------|----------|----------|------|\n");
          currentHeader = newHeader;
        }

        // Check if this path's pattern has been printed already
        String patternKey = extractPatternKey(path);
        if (!printedPatterns.contains(patternKey)) {
          printedPatterns.add(patternKey);
          String description = documenter.getDescription(path);
          String typeTag = documenter.getTypeTag(path);
          boolean hasGetter = documenter.hasGetter(path);
          boolean hasSetter = documenter.hasSetter(path);
          tableRows.add(String.format("| `%s` | %s | `%s` | %s | %s |", path, description, typeTag, hasGetter ? "✓" : " ", hasSetter ? "✓" : " "));
        }
      }

      if (!tableRows.isEmpty()) {
        bw.write(String.join("\n", tableRows));
      }

      bw.close();
    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
    }
  }

  private String extractPatternKey(String path) {
    return path.replaceAll("\\d+", "{n}");
  }

  private int comparePaths(String path1, String path2) {
    String[] parts1 = path1.split("/");
    String[] parts2 = path2.split("/");
    int length = Math.min(parts1.length, parts2.length);
    for (int i = 0; i < length; i++) {
      if (parts1[i].matches("\\d+") && parts2[i].matches("\\d+")) {
        int num1 = Integer.parseInt(parts1[i]);
        int num2 = Integer.parseInt(parts2[i]);
        if (num1 != num2) return num1 - num2;
      } else if (!parts1[i].equals(parts2[i])) {
        return parts1[i].compareTo(parts2[i]);
      }
    }
    return parts1.length - parts2.length;
  }

  private String getHeader(String path) {
    String[] parts = path.split("/");
    return parts.length > 2 ? parts[1] + "/" + parts[2] : (parts.length > 1 ? parts[1] : "General");
  }
}
