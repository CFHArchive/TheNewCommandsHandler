package net.tnemc.commands.core;

import net.tnemc.commands.core.provider.PlayerProvider;

@FunctionalInterface
public interface CommandPermissionHandler {

  /**
   * Used to check if a {@link PlayerProvider} is able to execute a registration they ran.
   * @param commandInformation The {@link CommandInformation} object associated with the registration being
   * executed.
   * @param sender The {@link PlayerProvider} attempting execute this registration.
   * @return True if the {@link PlayerProvider} is able to execute the registration, otherwise false.
   */
  boolean canExecute(final CommandInformation commandInformation, final PlayerProvider sender);
}