package com.kirkwoodwest.extensions.commander.commands;

import com.kirkwoodwest.extensions.commander.Commander;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class CommandExecutor {

  private final Commander commander;
  private final HashMap<String, Command> commands = new HashMap<>();

  public CommandExecutor(Commander commander){
    this.commander = commander;
    commands.put("select", new Select(commander));
    commands.put("arm", new Arm(commander));
    commands.put("mute", new Mute(commander));
    commands.put("solo", new Solo(commander));
  }
  public String execute(String command, List<String> args) {
    String success;
    if (commands.containsKey(command)) {
       success = commands.get(command).execute(args);
    } else {
      success = "Failed to execute command: " + command + " with args: " + args.toString() + "\n";

    }
    return success;
  }
}
