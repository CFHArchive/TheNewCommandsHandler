package net.tnemc.commands.core;

import net.tnemc.commands.core.loader.CommandLoader;
import net.tnemc.commands.core.loader.impl.BukkitCommandLoader;
import net.tnemc.commands.core.loader.impl.CuttlefishCommandLoader;
import net.tnemc.config.CommentedConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
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
    loader.load();
  }

  public List<String> tab(CommandSender sender, Command command, String label, String[] arguments) {
    final Optional<CommandInformation> information = manager.find(label);

    /*if(information.isPresent()) {
      if(manager.getCompleters().containsKey(information.get().getCompleter(arguments.length))) {
         return manager.getCompleters().get(information.get().getCompleter(arguments.length)).complete(sender, command, label, arguments, information.get().isSubCommand());
      }
    }*/
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

    //TODO: Test this then profit??

    final Optional<CommandInformation> information = manager.find(label);

    if(information.isPresent()) {
      if(manager.getExecutors().containsKey(information.get().getExecutor())) {
        if(!manager.getExecutors().get(information.get().getExecutor()).canExecute(information.get(), sender)) {
          sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
          return false;
        }

        System.out.println(information.get().toString());
        return true;
        //return manager.getExecutors().get(information.get().getExecutor()).execute(sender, command, label, arguments);
      }
    }
    return false;
  }

  public static CommandManager manager() {
    return instance().manager;
  }

  public static CommandsHandler instance() {
    return instance;
  }
}