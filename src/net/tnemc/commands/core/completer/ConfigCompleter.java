package net.tnemc.commands.core.completer;

import net.tnemc.commands.core.CommandSearchInformation;
import net.tnemc.commands.core.TabCompleter;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.Optional;

public class ConfigCompleter implements TabCompleter {


  private TabCompleter completer;

  private String name;
  private int limit;

  public ConfigCompleter(TabCompleter completer, String name, int limit) {
    this.completer = completer;
    this.name = name;
    this.limit = limit;
  }

  @Override
  public LinkedList<String> complete(CommandSender sender, Optional<CommandSearchInformation> search, String argument) {
    return completer.complete(sender, search, argument);
  }

  public TabCompleter getCompleter() {
    return completer;
  }

  public void setCompleter(TabCompleter completer) {
    this.completer = completer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
}