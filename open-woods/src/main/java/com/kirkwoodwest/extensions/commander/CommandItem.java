package com.kirkwoodwest.extensions.commander;

import java.util.ArrayList;
import java.util.List;

//Container and basic parser for each command
public class CommandItem {
  private String command;
  private String action;
  private List<String> args;
  public CommandItem(String command){
    this.command = command;
    //parse command
    String[] parts = command.split(" ");
    List<String> args = new ArrayList<>(parts.length - 1);
    for (int i = 0; i < parts.length; i++) {
      if (i==0){
        this.action = parts[0].replace("/", "");
      } else {
        args.add(parts[i]);
      }
    }
    this.args = args;
  }

  public String getCommand(){
    return this.action;
  }
  
  public List<String> getArgs(){
    return this.args;
  }
}
