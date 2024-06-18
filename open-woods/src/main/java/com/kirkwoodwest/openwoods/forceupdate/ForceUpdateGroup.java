package com.kirkwoodwest.openwoods.forceupdate;

import java.util.ArrayList;
import java.util.List;

public class ForceUpdateGroup {
  private int count = 0;
  List<String> updates = new ArrayList<>();
  public ForceUpdateGroup(){

  }
  public void add(String groupId) {
    updates.add(groupId);
  }
  public List<String> get(){
    if(updates.isEmpty()){
      return updates;
    } else {
      List<String> strings = new ArrayList<>(updates);
      updates.clear();
      return strings;
    }
  }

}
