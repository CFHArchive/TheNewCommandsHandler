package net.tnemc.commands.core.loader.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class BukkitCommandLoader implements CommandLoader {

  private FileConfiguration config;

  public BukkitCommandLoader(FileConfiguration config) {
    this.config = config;
  }

  public void loadCommands() {
    final Set<String> commands = config.getConfigurationSection("Commands").getKeys(false);

    for(String command : commands) {
      final String base = "Commands." + command;

      final CommandInformation info = loadCommand(command, base);

      CommandsHandler.manager().register(info.getAliases(), info);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = config.getConfigurationSection("Completion").getKeys(false);

    for(String completer : completers) {
      final String base = "Completion." + completer;

      final TabCompleter completerInstance = loadCompleter(completer, base);
      CommandsHandler.manager().getCompleters().put(completer, completerInstance);
    }
  }

  public CommandInformation loadCommand(String name, String base) {
    CommandInformation commandInfo = new CommandInformation(name);

    commandInfo.setAliases(config.getStringList(base + ".Alias"));
    commandInfo.setAuthor(config.getString(base + ".Author", "creatorfromhell"));
    commandInfo.setPermission(config.getString(base + ".Permission", ""));
    commandInfo.setConsole(config.getBoolean(base + ".Console", true));
    commandInfo.setDeveloper(config.getBoolean(base + ".Developer", false));
    commandInfo.setDescription(config.getString(base + ".Description", "No description provided."));
    commandInfo.setExecutor(config.getString(base + ".Executor", "hello_exe"));

    if(config.contains(base + ".Short")) {
      commandInfo.setSubShort(config.getStringList(base + ".Short"));
    }

    final Set<String> params = config.getConfigurationSection(base + ".Params").getKeys(false);

    for(String parameter : params) {

      final String paramBase = base + ".Params." + parameter;
      CommandParameter param = new CommandParameter(parameter);

      param.setOptional(config.getBoolean(paramBase + ".Optional", true));
      param.setTabComplete(config.getBoolean(paramBase + ".Complete", false));
      param.setCompleteType(config.getString(paramBase + ".CompleteType", "Player"));

      commandInfo.addParameter(param);

    }

    final Set<String> sub = config.getConfigurationSection(base + ".Sub").getKeys(false);

    for(String subName : sub) {
      commandInfo.addSub(loadCommand(subName, base + ".Sub." + subName));
    }
    return commandInfo;
  }

  public TabCompleter loadCompleter(String name, String base) {
    return null;
  }
}