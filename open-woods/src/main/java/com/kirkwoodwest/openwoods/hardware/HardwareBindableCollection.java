package com.kirkwoodwest.openwoods.hardware;

import com.bitwig.extension.controller.api.HardwareBindable;

import java.util.*;

public class HardwareBindableCollection {
  private final HashMap<Integer, List<HardwareBindable>> bindables = new HashMap<>();

  public void add(int index, HardwareBindable binding) {
    bindables.computeIfAbsent(index, k -> new ArrayList<>()).add(binding);
  }

  public List<HardwareBindable> get(int index) {
    return bindables.getOrDefault(index, new ArrayList<>());
  }

  public Map<Integer, List<HardwareBindable>> getAll() {
    return bindables;
  }
}
