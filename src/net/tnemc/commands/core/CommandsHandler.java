package net.tnemc.commands.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

  private static CommandsHandler instance;

  public CommandsHandler(JavaPlugin plugin) {
    instance = this;

    manager = new CommandManager(plugin, (information, sender)->sender.hasPermission(information.getPermission()));
  }

  public CommandsHandler(JavaPlugin plugin, CommandPermissionHandler permissionHandler) {
    instance = this;

    manager = new CommandManager(plugin, permissionHandler);
  }

  public List<String> tab(CommandSender sender, Command command, String label, String[] arguments) {
    final Optional<CommandInformation> information = manager.find(label);

    if(information.isPresent()) {
      if(manager.getCompleters().containsKey(information.get().getCompleter(arguments.length))) {
         return manager.getCompleters().get(information.get().getCompleter(arguments.length)).complete(sender, command, label, arguments, information.get().isSubCommand());
      }
    }
    return new ArrayList<>();

  }

  public boolean handle(CommandSender sender, Command command, String label, String[] arguments) {

    final Optional<CommandInformation> information = manager.find(label);

    if(information.isPresent()) {
      if(manager.getExecutors().containsKey(information.get().getExecutor())) {
        if(!manager.getExecutors().get(information.get().getExecutor()).canExecute(information.get(), sender)) {
          sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
          return false;
        }
        return manager.getExecutors().get(information.get().getExecutor()).execute(sender, command, label, arguments);
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