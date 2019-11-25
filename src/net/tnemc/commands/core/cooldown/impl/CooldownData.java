package net.tnemc.commands.core.cooldown.impl;

import net.tnemc.commands.core.cooldown.CooldownTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownData {

  private ConcurrentHashMap<String, Long> cooldowns = new ConcurrentHashMap<>();

  private UUID player;
  private int id = -1;

  public CooldownData(UUID player) {
    this.player = player;
  }

  public void addCooldown(JavaPlugin plugin, String command, long cooldown) {
    cooldowns.put(command, cooldown);
    this.id = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new CooldownTask(player, command), cooldown * 20).getTaskId();
  }

  public boolean hasCooldown(String command) {
    return cooldowns.containsKey(command);
  }

  public void removeCooldown(String command) {
    cooldowns.remove(command);
    if(this.id != -1) {
      Bukkit.getScheduler().cancelTask(this.id);
    }
  }
}