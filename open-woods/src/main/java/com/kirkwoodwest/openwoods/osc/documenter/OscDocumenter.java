package com.kirkwoodwest.openwoods.osc.documenter;

import com.bitwig.extension.controller.api.ControllerHost;
import java.util.*;

public class OscDocumenter {
  private final OscDocumenterPrinter oscDocumenterPrinter;
  //Hashmap of paths to descriptions
  Map<String, String> indexedPaths = new HashMap<>();
  Map<String, String> pathsToDescriptions = new HashMap<>();
  Map<String, String> pathsToTypeTags = new HashMap<>();

  //Hashmap of paths to setter or getter
  Set<String> hasSetter = new HashSet<>();
  Set<String> hasGetter = new HashSet<>();
  //Groups
  Map<String, List<String>> groups = new HashMap<>();

  public OscDocumenter(ControllerHost host ) {
    oscDocumenterPrinter = new OscDocumenterPrinter(host, this);
  }

  public void addPath(String path, String description, String typeTag, boolean setter, boolean getter) {
    pathsToDescriptions.put(path, description);
    pathsToTypeTags.put(path, typeTag);
    if (setter) hasSetter.add(path);
    if (getter) hasGetter.add(path);
  }



  public void addPath(String path,  String description, String typeTag) {
    addPath(path, description, typeTag, false, false);
  }

  //Set Setter
  public void setSetter(String path) {
    hasSetter.add(path);
  }

  //Set Getter
  public void setGetter(String path) {
    hasGetter.add(path);
  }

  public List<String> getPaths() {
    return List.copyOf(pathsToDescriptions.keySet());
  }

  public String getDescription(String path) {
    return pathsToDescriptions.get(path);
  }

  public boolean hasSetter(String path) {
    return hasSetter.contains(path);
  }

  public boolean hasGetter(String path) {
    return hasGetter.contains(path);
  }

  public void printDocumentation() {
    oscDocumenterPrinter.printToFile();
  }

  public String getTypeTag(String path) {
    return pathsToTypeTags.get(path);
  }

  public void addIndexedPath(String s, String description) {
    indexedPaths.put(s, description);
  }

  public List<PathTest.Result> checkPathAgainstIndexPath(String path) {
    return PathTest.test(path, indexedPaths);
  }
}
