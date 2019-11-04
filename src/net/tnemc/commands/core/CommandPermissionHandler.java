package net.tnemc.commands.core;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandPermissionHandler {

  /**
   * Used to check if a {@link CommandSender} is able to execute a command they ran.
   * @param commandInformation The {@link CommandInformation} object associated with the command being
   * executed.
   * @param sender The {@link CommandSender} attempting execute this command.
   * @return True if the {@link CommandSender} is able to execute the command, otherwise false.
   */
  boolean canExecute(final CommandInformation commandInformation, final CommandSender sender);
}