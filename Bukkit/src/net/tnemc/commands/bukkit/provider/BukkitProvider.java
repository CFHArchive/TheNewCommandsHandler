package net.tnemc.commands.bukkit.provider;

import net.tnemc.commands.core.provider.CommandRegistrationProvider;
import net.tnemc.commands.core.provider.FormatProvider;
import net.tnemc.commands.core.provider.ImplementationProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;

public class BukkitProvider implements ImplementationProvider {

  private JavaPlugin plugin;

  public BukkitProvider(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  public LinkedList<String> onlinePlayers() {
    LinkedList<String> online = new LinkedList<>();

    for(Player player : Bukkit.getOnlinePlayers()) {
      online.add(ChatColor.stripColor(player.getName()));
    }
    return online;
  }

  public CommandRegistrationProvider registration() {
    return new BukkitCommandRegistrationProvider(plugin);
  }

  public FormatProvider formatter() {
    return new BukkitFormatProvider();
  }
}