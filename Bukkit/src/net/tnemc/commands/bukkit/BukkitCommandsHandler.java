package net.tnemc.commands.bukkit;

import net.tnemc.commands.bukkit.loader.BukkitCommandLoader;
import net.tnemc.commands.bukkit.provider.BukkitCooldownHandler;
import net.tnemc.commands.bukkit.provider.BukkitProvider;
import net.tnemc.commands.core.CommandPermissionHandler;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.config.CommentedConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitCommandsHandler extends CommandsHandler {
  public BukkitCommandsHandler(JavaPlugin plugin, CommentedConfiguration commandsFile) {
    super(new BukkitProvider(plugin), commandsFile);
  }

  public BukkitCommandsHandler(FileConfiguration commandsFile, JavaPlugin plugin) {
    super(new BukkitCommandLoader(commandsFile), new BukkitProvider(plugin));
  }

  public BukkitCommandsHandler(FileConfiguration commandsFile, JavaPlugin plugin, CommandPermissionHandler permissionHandler) {
    super(new BukkitCommandLoader(commandsFile), new BukkitProvider(plugin), permissionHandler);
  }

  @Override
  public BukkitCooldownHandler getCooldownHandler() {
    return (BukkitCooldownHandler)super.getCooldownHandler();
  }
}