package net.tnemc.commands.core.cooldown.impl;

import net.tnemc.commands.core.cooldown.CooldownHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCooldownHandler implements CooldownHandler {

  ConcurrentHashMap<UUID, CooldownData> cooldowns = new ConcurrentHashMap<>();

  @Override
  public boolean addCooldown(JavaPlugin plugin, UUID player, String command, long cooldown) {
    CooldownData data = cooldowns.getOrDefault(player, new CooldownData(player));
    data.addCooldown(plugin, command, cooldown);
    cooldowns.put(player, data);
    return true;
  }

  @Override
  public boolean hasCooldown(UUID player, String command) {
    if(cooldowns.containsKey(player)) {
      return cooldowns.get(player).hasCooldown(command);
    }
    return false;
  }

  @Override
  public void removeCooldown(UUID player, String command) {
    if(cooldowns.containsKey(player)) {
      cooldowns.get(player).removeCooldown(command);
    }
  }
}