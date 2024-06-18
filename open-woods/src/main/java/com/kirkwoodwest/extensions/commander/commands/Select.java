package com.kirkwoodwest.extensions.commander.commands;

import com.kirkwoodwest.extensions.commander.Commander;

import java.util.List;

public class Select implements Command {
  private final Commander commander;

  public Select(Commander commander){
    this.commander = commander;
  }

  @Override
  public String execute(List<String> args) {
    if (args.size() == 2) {
      String modifier = args.get(0);
      int index = Integer.parseInt(args.get(1)) - 1;
      if (modifier.equals("track")) {
        commander.selectTrackInEditor(index );
      } else if (modifier.equals("scene")) {
        commander.selectSceneInEditor(index);
      }
    }
    return "Success";
  }
}
