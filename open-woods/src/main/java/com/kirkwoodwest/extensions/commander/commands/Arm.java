package com.kirkwoodwest.extensions.commander.commands;

import com.kirkwoodwest.extensions.commander.Commander;

import java.util.List;

public class Arm implements Command {
  private final Commander commander;

  public Arm(Commander commander){
    this.commander = commander;
  }

  @Override
  public String execute(List<String> args) {
    if (args.size() == 1) {
      // Toggle
      int index = Integer.parseInt(args.get(0)) - 1;
      commander.trackMuteToggle(index);
    } else if (args.size() == 2) {
      int index = Integer.parseInt(args.get(0)) - 1;
      boolean isArmed = Boolean.parseBoolean(args.get(1));
      commander.trackArm(index, isArmed);
    }
    return "Success";
  }
}
