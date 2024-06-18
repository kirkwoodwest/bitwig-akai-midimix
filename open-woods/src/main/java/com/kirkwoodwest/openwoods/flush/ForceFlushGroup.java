package com.kirkwoodwest.openwoods.flush;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to manage a list of groups that need to be force flushed. Clears itself after a call to get()

 */
public class ForceFlushGroup {
  List<String> groups = new ArrayList<>();
  public ForceFlushGroup(){}
  public void add(String groupId) {
    groups.add(groupId);
  }
  public List<String> get(){
    if(groups.isEmpty()){
      return groups;
    } else {
      List<String> tempGroups = new ArrayList<>(groups);
      groups.clear();
      return tempGroups;
    }
  }
}
