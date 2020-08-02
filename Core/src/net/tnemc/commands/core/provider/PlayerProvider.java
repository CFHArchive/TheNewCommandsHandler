package net.tnemc.commands.core.provider;

import java.util.UUID;

public interface PlayerProvider {

  UUID getUUID();

  String getName();

  String getDisplayName(boolean strip);

  boolean isPlayer();

  void sendMessage(final String message);

  boolean hasPermission(final String permission);
}