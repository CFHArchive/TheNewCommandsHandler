package net.tnemc.commands.core.completer;

import net.tnemc.commands.core.TabCompleter;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;

public class ConfigCompleter implements TabCompleter {

  private LinkedList<String> values;

  private String name;
  private int limit;

  public ConfigCompleter(LinkedList<String> values, String name, int limit) {
    this.values = values;
    this.name = name;
    this.limit = limit;
  }

  @Override
  public LinkedList<String> complete(CommandSender sender, String argument) {
    return values;
  }

  public LinkedList<String> getValues() {
    return values;
  }

  public void setValues(LinkedList<String> values) {
    this.values = values;
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