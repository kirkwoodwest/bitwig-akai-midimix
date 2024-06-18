package com.kirkwoodwest.openwoods.flush;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;

/*
  * This class is a singleton that manages all the flushable objects in the system.
  * Anything can register itself to flushables and it will be flushed when the system is flushed.
 */
public class Flushables {

  private static Flushables instance;
  private final ArrayList<Flushable> flushables;

  public static Flushables getInstance() {
    if (instance == null) {
      instance = new Flushables();
    }
    return instance;
  }

  private Flushables() {
    //Create list of flushable objects
    flushables = new ArrayList<>();
  }

  public void init(){
    //clean the list of flushable objects
    flushables.clear();
  }

  public void add(Flushable flushable) {
    //register the flushable object
    flushables.add(flushable);
  }

  public void flush() {
    //flush all the flushable objects
    flushables.forEach(flushable -> {
      try {
        flushable.flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
