package com.kirkwoodwest.openwoods.savelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringCollection {
  private final List<String> strings;
  private String s = "";

  public StringCollection(int count){
    strings = new ArrayList<>(Collections.nCopies(count, ""));
  }

  public ArrayList<String> getStrings() {
    return new ArrayList<String>(strings);
  }

  public void setStrings(ArrayList<String> strings) {
    this.strings.clear();
    this.strings.addAll(strings);
  }

  public String getString(int index ){
    return strings.get(index);
  }

  public void setString(int index, String s) {
    strings.set(index, s);
  }

  public void setTempString(String s) {
    this.s = s;
  }

  public String getTempString() {

    return s;
  }
}
