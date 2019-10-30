package net.tnemc.commands.core.loader.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.completer.ConfigCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.commands.core.parameter.ParameterType;
import net.tnemc.config.CommentedConfiguration;

import java.util.LinkedHashSet;
import java.util.LinkedList;
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

      CommandsHandler.manager().register(info.getIdentifiers(), info);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = config.getSection("Completion").getKeys(false);

    for(String completer : completers) {
      final String base = "Completion." + completer;

      CommandsHandler.manager().getCompleters().put(completer, new ConfigCompleter(
          (sender, search, argument)->config.getStringList(base + ".Values"),
          completer,
          config.getInt(base + ".Limit", 5)));
    }
  }

  public CommandInformation loadCommand(String name, String base) {
    CommandInformation commandInfo = new CommandInformation(name);

    commandInfo.setAliases(config.getStringList(base + ".Alias"));
    commandInfo.setAuthor(config.getString(base + ".Author", "Magic"));
    commandInfo.setPermission(config.getString(base + ".Permission", ""));
    commandInfo.setConsole(config.getBool(base + ".Console", true));
    commandInfo.setDeveloper(config.getBool(base + ".Developer", false));
    commandInfo.setDescription(config.getString(base + ".Description", "No description provided."));
    commandInfo.setExecutor(config.getString(base + ".Executor", "hello_exe"));

    if(config.contains(base + ".Short")) {
      commandInfo.setSubShort(config.getStringList(base + ".Short"));
    }

    commandInfo.addParameters(loadParameters(name, base));

    final Set<String> sub = config.getSection(base + ".Sub").getKeys();

    for(String subName : sub) {
      commandInfo.addSub(loadCommand(subName, base + ".Sub." + subName));
    }
    commandInfo.buildHelp();
    return commandInfo;
  }

  @Override
  public LinkedList<CommandParameter> loadParameters(String command, String configBase) {
    LinkedList<CommandParameter> parameters = new LinkedList<>();

    final LinkedHashSet<String> params = config.getSection(configBase + ".Params").getKeysLinked();

    for(String parameter : params) {

      final String paramBase = configBase + ".Params." + parameter;
      CommandParameter param = new CommandParameter(parameter.toLowerCase());

      param.setOrder(config.getInt(paramBase + ".Order", -1));

      //Validation-related variables
      final String type = config.getString(paramBase + ".Validation.Type", "string");
      if(ParameterType.exists(type)) param.setType(type);

      param.setMaxLength(config.getInt(paramBase + ".Validation.MaxLength", 0));
      param.setUseRegex(config.getBool(paramBase + ".Validation.Regex.Use", false));
      param.setRegex(config.getString(paramBase + ".Validation.Regex.Statement", ""));


      //Our core param variables
      param.setOptional(config.getBool(paramBase + ".Optional", true));
      param.setTabComplete(config.getBool(paramBase + ".Complete", false));
      param.setCompleteType(config.getString(paramBase + ".CompleteType", "Player"));

      parameters.add(param);
    }
    return parameters;
  }
}
