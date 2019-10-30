package net.tnemc.commands.core;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The New Commands Handler Library
 * <p>
 * Created by creatorfromhell on 10/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandManager {

  Map<List<String>, CommandInformation> commands = new HashMap<>();
  Map<String, TabCompleter> completers = new HashMap<>();
  Map<String, CommandExecution> executors = new HashMap<>();

  private CommandPermissionHandler permissionHandler;

  final JavaPlugin plugin;

  private Integer lastRegister = 0;
  private Field commandMap = null;
  private Field knownCommands = null;

  public CommandManager(JavaPlugin plugin, CommandPermissionHandler permissionHandler) {
    this.plugin = plugin;
    this.permissionHandler = permissionHandler;
  }

  public boolean hasCommand(String name) {
    Set<List<String>> keys = commands.keySet();

    for(List<String> identifiers : keys) {
      if(identifiers.contains(name)) return true;
    }
    return false;
  }

  private Optional<CommandInformation> find(String name) {

    Iterator<Map.Entry<List<String>, CommandInformation>> it = commands.entrySet().iterator();

    while(it.hasNext()) {
      final Map.Entry<List<String>, CommandInformation> entry = it.next();

      for(String str : entry.getKey()) {
        if(str.equalsIgnoreCase(name)) return entry.getValue().find(name);
      }
    }
    return Optional.empty();
  }

  public Optional<CommandSearchInformation> search(String name, String[] arguments) {
    Optional<CommandInformation> information = find(name);

    if(information.isPresent()) {
      System.out.println(information.get().toString());
      final CommandSearchInformation search = information.get().findSubInformation(arguments);
      return Optional.of(search);
    }
    return Optional.empty();
  }

  public void init() {
    try {
      commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      commandMap.setAccessible(true);
      knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommands.setAccessible(true);
    } catch (Exception ignore) {
      /* do nothing */
    }
  }

  public void registerCommands() {
    if(lastRegister == commands.size()) return;

    lastRegister = commands.size();
    init();

    if(commandMap != null && knownCommands != null) {

      Iterator<Map.Entry<List<String>, CommandInformation>> i = commands.entrySet().iterator();

      while(i.hasNext()) {
        Map.Entry<List<String>, CommandInformation> entry = i.next();

        for (String s : entry.getKey()) {
          if(registered(s)) {
            unregister(s, false);
          }
          register(s);
        }
      }
    }
  }

  public void registerExecutor(String name, CommandExecution executor) {
    executors.put(name, executor);
  }

  public void unregister(String[] accessors) {
    unregister(accessors, false);
  }

  public void unregister(String[] accessors, boolean commandsMap) {
    for(String s : accessors) {
      unregister(s, commandsMap);
    }
  }

  public void register(List<String> alias, CommandInformation information) {
    commands.put(alias, information);

    for (String s : alias) {
      if(registered(s.toLowerCase())) {
        unregister(s.toLowerCase(), false);
      }
      register(s.toLowerCase());
    }
  }

  private void register(String command) {
    try {

      if(commandMap == null) init();

      Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      c.setAccessible(true);
      System.out.println("CommandManager.register(" + command + ")");
      PluginCommand pluginCommand = c.newInstance(command, plugin);
      if(pluginCommand != null) {
        System.out.println("CommandManager.register(" + command + ")");
        ((SimpleCommandMap) commandMap.get(Bukkit.getServer())).register(command, pluginCommand);
      }
    } catch(Exception ignore) {
      //nothing to see here;
    }
  }

  public void unregister(String command, boolean commandsMap) {
    try {

      if(commandsMap) {
        Iterator<Map.Entry<List<String>, CommandInformation>> it = commands.entrySet().iterator();

        while (it.hasNext()) {
          Map.Entry<List<String>, CommandInformation> entry = it.next();

          boolean remove = false;
          for (String str : entry.getKey()) {
            System.out.println("CommandManager.unregister(" + command + ")");
            if (str.equalsIgnoreCase(command)) {
              System.out.println("CommandManager.unregister(remove = true)");
              remove = true;
            }
          }
          if (remove) it.remove();
        }
      }
      ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(command);
      knownCommands.set(commandMap.get(Bukkit.getServer()), knownCommands);
    } catch(Exception ignore) {
      //nothing to see here;
    }
  }

  private Boolean registered(String command) {
    try {
      return ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).containsKey(command);
    } catch(Exception e) {
      //nothing to see here;
    }
    return false;
  }

  public Map<List<String>, CommandInformation> getCommands() {
    return commands;
  }

  public void setCommands(Map<List<String>, CommandInformation> commands) {
    this.commands = commands;
  }

  public Map<String, TabCompleter> getCompleters() {
    return completers;
  }

  public void setCompleters(Map<String, TabCompleter> completers) {
    this.completers = completers;
  }

  public void addExecutor(String name, CommandExecution execution) {
    executors.put(name, execution);
  }

  public Map<String, CommandExecution> getExecutors() {
    return executors;
  }

  public void setExecutors(Map<String, CommandExecution> executors) {
    this.executors = executors;
  }

  public CommandPermissionHandler getPermissionHandler() {
    return permissionHandler;
  }

  public void setPermissionHandler(CommandPermissionHandler permissionHandler) {
    this.permissionHandler = permissionHandler;
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }
}