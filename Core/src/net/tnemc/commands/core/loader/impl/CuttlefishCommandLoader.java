package net.tnemc.commands.core.loader.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.completer.ConfigCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.commands.core.parameter.ParameterType;
import net.tnemc.commands.core.settings.MessageSettings;
import net.tnemc.config.CommentedConfiguration;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

public class CuttlefishCommandLoader implements CommandLoader {

  private CommentedConfiguration config;

  public CuttlefishCommandLoader(CommentedConfiguration config) {
    this.config = config;
    //System.out.println("Account Keys: " + String.join(", ", config.getSection("Commands.account.Sub").getKeys(false)));
    //System.out.println("Account Keys: " + String.join(", ", config.getSection("Commands.language.Sub").getKeys(false)));
    //System.out.println("Pin Keys: " + config.contains("Commands.account.Sub.pin.Sub.set"));
    //System.out.println("Pin Keys: " + config.contains("Commands.account.Sub.pin.Sub"));
    //System.out.println("Pin Keys: " + String.join(", ", config.getSection("Commands.account.Sub.pin.Sub").getKeys(false)));
    //System.out.println("Pin Keys: " + config.getString("Commands.account.Sub.pin.Sub.set.Description"));
    //System.out.println("Pin Keys: " + String.join(", ", config.getSection("Commands.account.Sub.pin.Sub.set").getKeys(false)));
    //System.out.println("Pin Keys: " + String.join(", ", config.getSection("Commands.account.Sub.pin").getKeys(true)));
    //System.out.println("Commands Keys: " + String.join(", ", config.getSection("Commands").getKeys(false)));
  }

  public void loadMessages() {
    //Our parameters messages
    MessageSettings.invalidType = config.getString("Messages.Parameter.InvalidType",
                                                   "<red>Parameter \"$parameter\" is of type $parameter_type."
    );

    MessageSettings.invalidLength = config.getString("Messages.Parameter.InvalidLength",
                                                     "<red>The max length of parameter \"$parameter\" is $max_length."
    );

    MessageSettings.parameterOption = config.getString("Messages.Parameter.ParameterOption",
                                                       "[$parameter]"
    );

    MessageSettings.parameterRequired = config.getString("Messages.Parameter.ParameterRequired",
                                                         "<$parameter>"
    );

    //Our registration messages
    MessageSettings.commandHelp = config.getString("Messages.Command.CommandHelp",
                                                   "Correct usage: /$registration $parameters - $description"
    );

    MessageSettings.cooldown = config.getString("Messages.Command.Cooldown",
                                                   "<red>This registration is on cooldown."
    );

    MessageSettings.developer = config.getString("Messages.Command.Developer",
                                                 "<red>You must be a developer to use that registration."
    );

    MessageSettings.console = config.getString("Messages.Command.Console",
                                               "<red>This registration is not usable from console."
    );

    MessageSettings.player = config.getString("Messages.Command.Console",
                                               "<red>This registration is not usable from in-game."
    );

    MessageSettings.invalidPermission = config.getString("Messages.Command.InvalidPermission",
                                                         "<red>I'm sorry, but you're not allowed to use that registration."
    );
  }

  public void loadCommands() {
    final Set<String> commands = config.getSection("Commands").getKeys(false);

    for(String command : commands) {
      final String base = "Commands." + command;

      final CommandInformation info = loadCommand(command, base, null);

      CommandsHandler.manager().register(info.getIdentifiers(), info);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = config.getSection("Completion").getKeys(false);

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

  public CommandInformation loadCommand(String name, final String base, CommandInformation parent) {
    //System.out.println("Test: " + String.join(", ", config.getSection("Commands.account.Sub.pin.Sub").getKeys(false)));
    //System.out.println("loadCommand: " + name);
    //System.out.println("loadCommand(base): " + base);
    CommandInformation commandInfo = new CommandInformation(name);

    commandInfo.setParent(parent);

    commandInfo.setAliases(CommandsHandler.manager().translate(base + ".Alias", Optional.empty(), config.getStringList(base + ".Alias")));
    commandInfo.setAuthor(config.getString(base + ".Author", "Magic"));
    commandInfo.setPermission(config.getString(base + ".Permission", ""));
    commandInfo.setConsole(config.getBool(base + ".Console", true));
    commandInfo.setPlayer(config.getBool(base + ".Player", true));
    commandInfo.setDeveloper(config.getBool(base + ".Developer", false));
    commandInfo.setCooldown(config.getInt(base + ".Cooldown", 0));
    commandInfo.setDescription(CommandsHandler.manager().translate(base + ".Description", Optional.empty(), config.getString(base + ".Description", "No description provided.")));
    commandInfo.setExecutor(config.getString(base + ".Executor", "hello_exe"));


    if(config.contains(base + ".Short")) {
      commandInfo.setSubShort(config.getStringList(base + ".Short"));
    }

    //System.out.println(commandInfo.toString());

    commandInfo.addParameters(loadParameters(name, base));
    //System.out.println("Base Node: " + base);

    final String subDestination = base + ".Sub";
    //System.out.println("Contains: " + config.contains(subDestination));
    if(config.contains(subDestination)) {
      Set<String> sub = config.getSection(subDestination).getKeys(false);
      //System.out.println("Base Node: " + subDestination);
      //System.out.println("Subs: " + String.join(",", sub));
      //System.out.println("Subs 2: " + String.join(",", config.getSection(subDestination + ".Sub").getKeys(false)));

      for(String subName : config.getSection(subDestination).getKeys(false)) {
        //System.out.println("Sub - " + subName);
        final String subBase = base + ".Sub." + subName;
        //System.out.println("SubBase: " + subBase);
        commandInfo.addSub(loadCommand(subName, subBase, commandInfo));
      }
    }
    return commandInfo;
  }

  @Override
  public LinkedList<CommandParameter> loadParameters(String command, final String configBase) {
    //System.out.println("loadParameters(registration): " + command);
    //System.out.println("loadParameters(base): " + configBase);
    LinkedList<CommandParameter> parameters = new LinkedList<>();

    if(config.contains(configBase + ".Params")) {

      final LinkedHashSet<String> params = config.getSection(configBase + ".Params").getKeysLinked(false);

      for(String parameter : params) {

        //System.out.println("Parameter load: " + parameter);

        final String paramBase = configBase + ".Params." + parameter;
        CommandParameter param = new CommandParameter(parameter.toLowerCase());

        param.setOrder(config.getInt(paramBase + ".Order", -1));

        //Validation-related variables
        final String type = config.getString(paramBase + ".Validation.Type", "string");
        if(ParameterType.exists(type)) param.setType(type);

        param.setMaxLength(config.getInt(paramBase + ".Validation.MaxLength", 0));
        param.setUseRegex(config.getBool(paramBase + ".Validation.Regex.Use", false));

        if(param.isUseRegex()) {
          param.setRegex(config.getString(paramBase + ".Validation.Regex.Statement", ""));
        }

        //Our core param variables
        param.setOptional(config.getBool(paramBase + ".Optional", true));
        param.setTabComplete(config.getBool(paramBase + ".Complete", false));
        param.setCompleteType(config.getString(paramBase + ".CompleteType", "Player"));

        parameters.add(param);
      }
    }
    return parameters;
  }
}