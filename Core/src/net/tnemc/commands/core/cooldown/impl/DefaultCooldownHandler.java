package net.tnemc.commands.core.cooldown.impl;

import net.tnemc.commands.core.cooldown.CooldownHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface DefaultCooldownHandler extends CooldownHandler {

  ConcurrentHashMap<UUID, CooldownData> cooldowns = new ConcurrentHashMap<>();

  @Override
  default boolean addCooldown(UUID player, String command, long cooldown) {
    CooldownData data = cooldowns.getOrDefault(player, new CooldownData(player));
    data.addCooldown(command, cooldown);
    cooldowns.put(player, data);
    return true;
  }

  @Override
  default boolean hasCooldown(UUID player, String command) {
    if(cooldowns.containsKey(player)) {
      return cooldowns.get(player).hasCooldown(command);
    }
    return false;
  }

  @Override
  default void removeCooldown(UUID player, String command) {
    if(cooldowns.containsKey(player)) {
      cooldowns.get(player).removeCooldown(command);
    }
  }
}