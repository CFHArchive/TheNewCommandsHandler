package net.tnemc.commands.core.loader;

import net.tnemc.commands.core.parameter.CommandParameter;

import java.util.LinkedList;

public interface CommandLoader {

  default void load() {
    loadCommands();

    loadCompleters();
  }

  //TODO: Load completers fully
  //TODO: Tab Completion code
  LinkedList<CommandParameter> loadParameters(String command, String configBase);

  void loadCommands();

  void loadCompleters();
}