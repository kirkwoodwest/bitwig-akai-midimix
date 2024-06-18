package com.kirkwoodwest.openwoods.hardware.layer;

import com.kirkwoodwest.openwoods.led.Led;

import java.util.List;

//Used within layers
public abstract class ControlGroup<T> {
  private final int id;
  public T hardware;

  public ControlGroup(T hardware, int id){
    this.hardware = hardware;
    this.id = id;

    //Build Action List Here and Led List
  }

  public int getId(){
    return id;
  }

  public abstract void setInactive();

  public abstract void setActive(); //Activates sub layer bindings

  public abstract void clearBindings(); //Clear all bindings from hardware

  public abstract List<Integer> getLedGroupIds(); //gets all the led group ids

  public abstract int getLedCount(int ledGroup); //gets the number of leds in a group
  public abstract Led<?> getLed(int ledGroup, int index); //gets the led at the index in the group
}
