package com.kirkwoodwest.openwoods.osc.adapters;

import com.kirkwoodwest.openwoods.osc.OscController;

public class OscAdapter<T> {

  private final AdapterInfo adapterInfo;
  public final OscController oscController;
  public String oscPath;
  private final String oscDescription;
  public T dataSource;
  protected boolean dirty;
  protected boolean forceFlush;
  protected boolean enabled = true;

  public OscAdapter(T dataSource, OscController oscController, String oscPath, String oscDescription) {
    this.oscController = oscController;
    this.oscPath = oscPath;
    this.oscDescription = oscDescription;
    this.dataSource = dataSource;
    this.adapterInfo = new AdapterInfo();
  }


  public AdapterInfo getAdapterInfo() {
    return adapterInfo;
  }

  public String getPath() {
    return oscPath;
  }

  public void setDirty(boolean b) {
    dirty = b;
  }

  public boolean getDirty() {
    return dirty;
  }


  public void setEnabled(boolean b) {
    enabled = b;
  }

  public void flush() {
    if(enabled) {
      if (getDirty() || forceFlush) {
        //Send Data to targets
        setDirty(false);
        forceFlush = false;
      }
    }
  }

  public void forceNextFlush() {
    forceFlush = true;
  }
}