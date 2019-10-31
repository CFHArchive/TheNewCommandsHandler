package net.tnemc.commands.core.loader.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.completer.ConfigCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.commands.core.parameter.ParameterType;
import net.tnemc.commands.core.settings.MessageSettings;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedList;
import java.util.Set;

public class BukkitCommandLoader implements CommandLoader {

  private FileConfiguration config;

  public BukkitCommandLoader(FileConfiguration config) {
    this.config = config;
  }

  public void loadMessages() {
    //Our parameters messages
    MessageSettings.invalidType = config.getString("Messages.Parameter.InvalidType",
                                                   "<red>Parameter \"$parameter\" is of type $parameter_type.");

    MessageSettings.invalidLength = config.getString("Messages.Parameter.InvalidLength",
                                                   "<red>The max length of parameter \"$parameter\" is $max_length.");

    MessageSettings.parameterOption = config.getString("Messages.Parameter.ParameterOption",
                                                   "[$parameter]");

    MessageSettings.parameterRequired = config.getString("Messages.Parameter.ParameterRequired",
                                                   "<$parameter>");

    //Our command messages
    MessageSettings.commandHelp = config.getString("Messages.Command.CommandHelp",
                                                   "$command $parameters - $description");

    MessageSettings.developer = config.getString("Messages.Command.Developer",
                                                   "<red>You must be a developer to use that command.");

    MessageSettings.console = config.getString("Messages.Command.Console",
                                                   "<red>This command is not usable from console.");

    MessageSettings.invalidPermission = config.getString("Messages.Command.InvalidPermission",
                                                   "<red>I'm sorry, but you're not allowed to use that command.");
  }

  public void loadCommands() {
    final Set<String> commands = config.getConfigurationSection("Commands").getKeys(false);

    for(String command : commands) {
      final String base = "Commands." + command;

      final CommandInformation info = loadCommand(command, base, null);

      CommandsHandler.manager().register(info.getIdentifiers(), info);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = config.getConfigurationSection("Completion").getKeys(false);

    for(String completer : completers) {
      final String base = "Completion." + completer;

      if(CommandsHandler.manager().getCompleters().containsKey(completer.toLowerCase())) {
        TabCompleter completerObj = CommandsHandler.manager().getCompleters().get(completer.toLowerCase());
        if(completerObj instanceof ConfigCompleter) {
          ((ConfigCompleter)completerObj).setLimit(config.getInt(base + ".Limit", 5));
        } else {
          CommandsHandler.manager().getCompleters().put(completer.toLowerCase(), new ConfigCompleter(
              completerObj,
              completer,
              config.getInt(base + ".Limit", 5)
          ));
        }
        CommandsHandler.manager().getCompleters().put(completer.toLowerCase(), completerObj);
      } else {
        CommandsHandler.manager().getCompleters().put(completer.toLowerCase(), new ConfigCompleter(
            (sender, search, argument)->new LinkedList<>(config.getStringList(base + ".Values")),
            completer,
            config.getInt(base + ".Limit", 5)
        ));
      }
    }
  }

  public CommandInformation loadCommand(String name, String base, CommandInformation parent) {
    CommandInformation commandInfo = new CommandInformation(name);

    commandInfo.setParent(parent);

    commandInfo.setAliases(config.getStringList(base + ".Alias"));
    commandInfo.setAuthor(config.getString(base + ".Author", "Magic"));
    commandInfo.setPermission(config.getString(base + ".Permission", ""));
    commandInfo.setConsole(config.getBoolean(base + ".Console", true));
    commandInfo.setDeveloper(config.getBoolean(base + ".Developer", false));
    commandInfo.setDescription(config.getString(base + ".Description", "No description provided."));
    commandInfo.setExecutor(config.getString(base + ".Executor", "hello_exe"));

    if(config.contains(base + ".Short")) {
      commandInfo.setSubShort(config.getStringList(base + ".Short"));
    }

    commandInfo.addParameters(loadParameters(name, base));

    if(config.contains(base + ".Sub")) {
      final Set<String> sub = config.getConfigurationSection(base + ".Sub").getKeys(false);

      for(String subName : sub) {
        commandInfo.addSub(loadCommand(subName, base + ".Sub." + subName, commandInfo));
      }
    }
    commandInfo.buildHelp();
    return commandInfo;
  }

  @Override
  public LinkedList<CommandParameter> loadParameters(String command, String configBase) {
    LinkedList<CommandParameter> parameters = new LinkedList<>();

    if(config.contains(configBase + ".Params")) {

      final Set<String> params = config.getConfigurationSection(configBase + ".Params").getKeys(false);

      for(String parameter : params) {

        final String paramBase = configBase + ".Params." + parameter;
        CommandParameter param = new CommandParameter(parameter.toLowerCase());

        param.setOrder(config.getInt(paramBase + ".Order", -1));

        //Validation-related variables
        final String type = config.getString(paramBase + ".Validation.Type", "string");
        if(ParameterType.exists(type)) param.setType(type);

        param.setMaxLength(config.getInt(paramBase + ".Validation.MaxLength", 0));
        param.setUseRegex(config.getBoolean(paramBase + ".Validation.Regex.Use", false));

        if(param.isUseRegex()) {
          param.setRegex(config.getString(paramBase + ".Validation.Regex.Statement", ""));
        }

        //Our core param variables
        param.setOptional(config.getBoolean(paramBase + ".Optional", true));
        param.setTabComplete(config.getBoolean(paramBase + ".Complete", false));
        param.setCompleteType(config.getString(paramBase + ".CompleteType", "Player"));

        parameters.add(param);
      }
    }
    return parameters;
  }
}