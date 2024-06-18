package com.kirkwoodwest.extensions.commander.commands;

import com.kirkwoodwest.extensions.commander.Commander;

import java.util.List;
import java.util.function.BiFunction;

public interface Command {
  public String execute(List<String> args);
}
