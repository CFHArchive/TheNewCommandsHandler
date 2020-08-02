package net.tnemc.commands.bukkit.provider;

import net.tnemc.commands.core.provider.CommandRegistrationProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

public class BukkitCommandRegistrationProvider implements CommandRegistrationProvider {

  private JavaPlugin plugin;

  private Field commandMap = null;
  private Field knownCommands = null;

  public BukkitCommandRegistrationProvider(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Called before calls to {@link #register(String)}.
   */
  @Override
  public void preRegistration() {
    try {
      commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      commandMap.setAccessible(true);
      knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommands.setAccessible(true);
    } catch (Exception ignore) {
      /* do nothing */
    }
  }

  /**
   * Used to register a registration.
   *
   * @param command The registration to register.
   */
  @Override
  public void register(String command) {

    try {

      if(commandMap == null) preRegistration();

      Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      c.setAccessible(true);
      //System.out.println("CommandManager.register(" + command + ")");
      PluginCommand pluginCommand = c.newInstance(command, plugin);
      if(pluginCommand != null) {
        //System.out.println("CommandManager.register(" + command + ")");
        ((SimpleCommandMap) commandMap.get(Bukkit.getServer())).register(command, pluginCommand);
      }
    } catch(Exception ignore) {
      //nothing to see here;
    }
  }

  /**
   * Used to unregister a registration.
   *
   * @param command The registration to unregister.
   */
  @Override
  public void unregister(String command) {
    try {
      ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(command);
      knownCommands.set(commandMap.get(Bukkit.getServer()), knownCommands);
    } catch(Exception ignore) {
      //nothing to see here;
    }
  }

  /**
   * Returns whether or not a registration is registered.
   *
   * @param command The registration to check.
   *
   * @return True if the registration is registered, otherwise false.
   */
  @Override
  public boolean isRegistered(String command) {
    try {
      return ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).containsKey(command);
    } catch(Exception e) {
      //nothing to see here;
    }
    return false;
  }
}