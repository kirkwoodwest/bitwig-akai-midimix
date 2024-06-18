package com.kirkwoodwest.openwoods.forceupdate;

/**
 * ForceUpdate utility class to set and get a boolean value during flush.
 */
public class ForceUpdate {
  private boolean forceUpdate = false;
  public ForceUpdate(){
  }

  public void set(){
    forceUpdate = true;
  }

  public boolean get(){
    boolean forceUpdate = this.forceUpdate;
    this.forceUpdate = false;
    return forceUpdate;
  }
}
