package net.tnemc.commands.core;

import net.tnemc.commands.core.completer.impl.PlayerCompleter;
import net.tnemc.commands.core.completer.impl.SubCompleter;
import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.loader.impl.BukkitCommandLoader;
import net.tnemc.commands.core.loader.impl.CuttlefishCommandLoader;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.commands.core.parameter.ParameterType;
import net.tnemc.commands.core.settings.MessageSettings;
import net.tnemc.commands.core.utils.ColourFormatter;
import net.tnemc.commands.core.utils.CommandTranslator;
import net.tnemc.config.CommentedConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The New Commands Handler Library
 * <p>
 * Created by creatorfromhell on 10/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandsHandler {

  private CommandManager manager;
  private CommandLoader loader;

  private static CommandsHandler instance;

  private List<String> developers = new ArrayList<>();

  public CommandsHandler(JavaPlugin plugin, FileConfiguration commandsFile) {
    instance = this;

    loader = new BukkitCommandLoader(commandsFile);
    manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
  }

  public CommandsHandler(JavaPlugin plugin, CommentedConfiguration commandsFile) {
    instance = this;

    loader = new CuttlefishCommandLoader(commandsFile);
    manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
  }

  public CommandsHandler(JavaPlugin plugin, FileConfiguration commandsFile, boolean testing) {
    instance = this;

    loader = new BukkitCommandLoader(commandsFile);

    if(testing) {
      manager = new CommandManager(plugin, (information, sender)->true);
    } else {
      manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
    }
  }

  public CommandsHandler(JavaPlugin plugin, CommentedConfiguration commandsFile, boolean testing) {
    instance = this;

    loader = new CuttlefishCommandLoader(commandsFile);

    if(testing) {
      manager = new CommandManager(plugin, (information, sender)->true);
    } else {
      manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
    }
  }

  public CommandsHandler(JavaPlugin plugin, CommandLoader loader) {
    instance = this;

    this.loader = loader;
    manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
  }

  public CommandsHandler(JavaPlugin plugin, FileConfiguration commandsFile, CommandPermissionHandler permissionHandler) {
    instance = this;

    loader = new BukkitCommandLoader(commandsFile);
    manager = new CommandManager(plugin, permissionHandler);
  }

  public CommandsHandler(JavaPlugin plugin, CommentedConfiguration commandsFile, CommandPermissionHandler permissionHandler) {
    instance = this;

    loader = new CuttlefishCommandLoader(commandsFile);
    manager = new CommandManager(plugin, permissionHandler);
  }

  /**
   * Creates a new Command Handler instance.
   * @param plugin The instance of the plugin class.
   * @param loader The {@link CommandLoader} instance to use.
   * @param permissionHandler The {@link CommandPermissionHandler} instance to use.
   */
  public CommandsHandler(JavaPlugin plugin, CommandLoader loader, CommandPermissionHandler permissionHandler) {
    instance = this;

    this.loader = loader;
    manager = new CommandManager(plugin, permissionHandler);
  }

  /**
   * Used to load everything from the command loader.
   */
  public void load() {

    manager.getCompleters().put("player", new PlayerCompleter());
    manager.getCompleters().put("sub_command", new SubCompleter());

    loader.load();
  }

  /**
   * Used to handle tab completion with registered commands.
   * @param sender The instance of Bukkit's {@link CommandSender} class.
   * @param command The instance of Bukkit's {@link Command} class.
   * @param label The String used as a command. Example: test would be the label in /test hi
   * @param arguments A String array of the arguments provided for the command executed.
   * @return A list containing the tab completion values.
   */
  public List<String> tab(CommandSender sender, Command command, String label, String[] arguments) {
    Optional<CommandSearchInformation> search = manager.search(label, arguments);

    if(search.isPresent() && search.get().getInformation().isPresent()) {

      arguments = search.get().getArguments();

      final String argument = (arguments.length > 0)? arguments[arguments.length - 1] : "";

      if(arguments.length > 0) {
        final Optional<CommandInformation> information = search.get().getInformation();
        if(manager.getCompleters().containsKey(information.get().getCompleter(arguments.length - 1))) {
          return manager.getCompleters().get(information.get().getCompleter(arguments.length - 1))
              .complete(sender, search, argument);
        }
      }
      return manager.getCompleters().get("sub_command").complete(sender, search, argument);
    }
    return new ArrayList<>();
  }

  /**
   * Used to handle a command registered with TNCH.
   * @param sender The instance of Bukkit's {@link CommandSender} class.
   * @param command The instance of Bukkit's {@link Command} class.
   * @param label The String used as a command. Example: test would be the label in /test hi
   * @param arguments A String array of the arguments provided for the command executed.
   * @return True if the command was successful, otherwise false.
   */
  public boolean handle(CommandSender sender, Command command, String label, String[] arguments) {

    final boolean player = (sender instanceof Player);

    Optional<CommandSearchInformation> search = manager.search(label, arguments);

    if(search.isPresent() && search.get().getInformation().isPresent()) {
      final Optional<CommandInformation> information = search.get().getInformation();
      arguments = search.get().getArguments();

      if(manager.getExecutors().containsKey(information.get().getExecutor())) {

        if(!player && !information.get().isConsole()) {
          sender.sendMessage(manager.translate("Messages.Command.Console", ColourFormatter.format(MessageSettings.console, false)));
          return false;
        }

        if(player && !information.get().isPlayer()) {
          sender.sendMessage(manager.translate("Messages.Command.Player", ColourFormatter.format(MessageSettings.player, false)));
          return false;
        }

        if(!information.get().isDeveloper() && !manager.getExecutors().get(information.get().getExecutor()).canExecute(information.get(), sender)) {
          sender.sendMessage(manager.translate("Messages.Command.InvalidPermission", ColourFormatter.format(MessageSettings.invalidPermission, false)));
          return false;
        }

        if(information.get().isDeveloper()) {
          if(!player || !developers.contains(((Player)sender).getUniqueId().toString())) {
            sender.sendMessage(manager.translate("Messages.Command.Developer", ColourFormatter.format(MessageSettings.developer, false)));
            return false;
          }
        }

        if(search.get().getInformation().get().getRequiredArguments() > arguments.length) {
          sender.sendMessage(ColourFormatter.format(search.get().getInformation().get().getHelp(), false));
          return false;
        }

        for(int i = 0; i < arguments.length; i++) {
          final CommandParameter param = search.get().getInformation().get().getParameters().get(i);

          if(param != null) {

            final Optional<ParameterType> type = ParameterType.find(param.getType());
            if(type.isPresent() && !type.get().getValidator().valid(param.getRegex(), arguments[i])) {
              sender.sendMessage(manager.translate("Messages.Parameter.InvalidType", ColourFormatter.format(MessageSettings.invalidType
                                                                                  .replace("$parameter", param.getName())
                                                                                  .replace("$parameter_type", param.getType()), false)));
              return false;
            }

            if(type.isPresent() && type.get().getName().equalsIgnoreCase("string")
                && param.getMaxLength() > 0) {
              if(arguments[i].length() > param.getMaxLength()) {
                sender.sendMessage(manager.translate("Messages.Parameter.InvalidLength", ColourFormatter.format(MessageSettings.invalidLength
                                                                                                                  .replace("$parameter", param.getName())
                                                                                                                  .replace("$parameter_type", param.getType()), false)));
                return false;
              }
            }
          }
        }

        return manager.getExecutors().get(information.get().getExecutor()).execute(sender, command, label, arguments);
      }
    }
    return false;
  }

  /**
   * Used to load everything for this {@link CommandsHandler}.
   * @return This {@link CommandsHandler}
   */
  public CommandsHandler loadEverything() {
    load();
    return this;
  }

  /**
   * Used to change the list of developer UUIDs used by this {@link CommandsHandler}.
   * @param developers The list of String-based UUIDs for all developers.
   * @return This {@link CommandsHandler}
   */
  public CommandsHandler withDevelopers(final List<String> developers) {
    setDevelopers(developers);
    return this;
  }

  /**
   * Used to change the {@link CommandTranslator} used.
   * @param translator The {@link CommandTranslator} to use.
   * @return This {@link CommandsHandler}
   */
  public CommandsHandler withTranslator(CommandTranslator translator) {
    manager.setTranslator(translator);
    return this;
  }

  /**
   * Used to change the {@link CommandTranslator} used.
   * @param translator The {@link CommandTranslator} to use.
   */
  public void setTranslator(CommandTranslator translator) {
    manager.setTranslator(translator);
  }

  /**
   * Used to add a new {@link CommandExecution executor} to TNCH.
   * @param name The name of the command executor
   * @param executor The instance of the executor
   * @return This {@link CommandsHandler}
   */
  public CommandsHandler withExecutor(String name, CommandExecution executor) {
    manager.addExecutor(name, executor);
    return this;
  }

  /**
   * Used to add a new {@link CommandExecution executor} to TNCH.
   * @param name The name of the command executor
   * @param executor The instance of the executor
   */
  public void addExecutor(String name, CommandExecution executor) {
    manager.addExecutor(name, executor);
  }

  public static CommandManager manager() {
    return instance().manager;
  }

  public static CommandsHandler instance() {
    return instance;
  }

  public List<String> getDevelopers() {
    return developers;
  }

  public void setDevelopers(List<String> developers) {
    this.developers = developers;
  }
}