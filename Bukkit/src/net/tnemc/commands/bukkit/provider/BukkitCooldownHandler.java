package net.tnemc.commands.bukkit.provider;

import net.tnemc.commands.bukkit.provider.cooldown.CooldownTask;
import net.tnemc.commands.core.cooldown.impl.DefaultCooldownHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class BukkitCooldownHandler implements DefaultCooldownHandler {

  private JavaPlugin plugin;

  public BukkitCooldownHandler(JavaPlugin plugin) {
    this.plugin = plugin;
  }


  @Override
  public int scheduleCooldown(UUID player, String command, long cooldown) {
    return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new CooldownTask(player, command), cooldown * 20).getTaskId();
  }

  @Override
  public void cancelCooldown(int id) {
    if(id != -1) {
      Bukkit.getScheduler().cancelTask(id);
    }
  }
}