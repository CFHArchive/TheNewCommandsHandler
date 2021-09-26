package net.tnemc.commands.bukkit.provider;

import net.tnemc.commands.core.provider.PlayerProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerProvider implements PlayerProvider {

  final CommandSender sender;

  public BukkitPlayerProvider(CommandSender sender) {
    this.sender = sender;
  }

  @Override
  public UUID getUUID() {
    if(!isPlayer()) {
      return UUID.randomUUID();
    }
    return ((Player)sender).getUniqueId();
  }

  @Override
  public String getName() {
    return sender.getName();
  }

  @Override
  public String getDisplayName(boolean strip) {
    return sender.getName();
  }

  @Override
  public boolean isPlayer() {
    return sender instanceof Player;
  }

  @Override
  public void sendMessage(String message) {
    if(!message.trim().equalsIgnoreCase("")) {
      sender.sendMessage(message);
    }
  }

  @Override
  public boolean hasPermission(String permission) {
    return sender.hasPermission(permission);
  }
}