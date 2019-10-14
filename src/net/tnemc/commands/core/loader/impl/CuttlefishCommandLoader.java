package net.tnemc.commands.core.loader.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.config.CommentedConfiguration;

import java.util.LinkedHashSet;
import java.util.Set;

public class CuttlefishCommandLoader implements CommandLoader {
  
  private CommentedConfiguration config;
  
  public CuttlefishCommandLoader(CommentedConfiguration config) {
    this.config = config;
  }

  public void loadCommands() {
    final Set<String> commands = config.getSection("Commands").getKeys(false);

    for(String command : commands) {
      final String base = "Commands." + command;

      final CommandInformation info = loadCommand(command, base);

      CommandsHandler.manager().register(info.getAliases(), info);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = config.getSection("Completion").getKeys(false);

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
    commandInfo.setConsole(config.getBool(base + ".Console", true));
    commandInfo.setDeveloper(config.getBool(base + ".Developer", false));
    commandInfo.setDescription(config.getString(base + ".Description", "No description provided."));
    commandInfo.setExecutor(config.getString(base + ".Executor", "hello_exe"));

    if(config.contains(base + ".Short")) {
      commandInfo.setSubShort(config.getStringList(base + ".Short"));
    }

    final LinkedHashSet<String> params = config.getSection(base + ".Params").getKeysLinked();

    for(String parameter : params) {

      final String paramBase = base + ".Params." + parameter;
      CommandParameter param = new CommandParameter(parameter);

      param.setOptional(config.getBool(paramBase + ".Optional", true));
      param.setTabComplete(config.getBool(paramBase + ".Complete", false));
      param.setCompleteType(config.getString(paramBase + ".CompleteType", "Player"));

      commandInfo.addParameter(param);

    }

    final Set<String> sub = config.getSection(base + ".Sub").getKeys();

    for(String subName : sub) {
      commandInfo.addSub(loadCommand(subName, base + ".Sub." + subName));
    }
    return commandInfo;
  }

  public TabCompleter loadCompleter(String name, String base) {
    return null;
  }
}
