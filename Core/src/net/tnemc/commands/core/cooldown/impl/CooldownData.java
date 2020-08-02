package net.tnemc.commands.core.cooldown.impl;

import net.tnemc.commands.core.CommandsHandler;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownData {

  private ConcurrentHashMap<String, Long> cooldowns = new ConcurrentHashMap<>();

  private UUID player;
  private int id = -1;

  public CooldownData(UUID player) {
    this.player = player;
  }

  public void addCooldown(String command, long cooldown) {
    cooldowns.put(command, cooldown);
    this.id = CommandsHandler.instance().getCooldownHandler().scheduleCooldown(player, command, cooldown);
  }

  public boolean hasCooldown(String command) {
    return cooldowns.containsKey(command);
  }

  public void removeCooldown(String command) {
    cooldowns.remove(command);
    if(this.id != -1) {
      CommandsHandler.instance().getCooldownHandler().cancelCooldown(id);
    }
  }
}