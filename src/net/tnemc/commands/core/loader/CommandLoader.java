package net.tnemc.commands.core.loader;

public interface CommandLoader {

  default void load() {
    loadCommands();

    loadCompleters();
  }

  void loadCommands();

  void loadCompleters();
}