package net.tnemc.commands.core;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandPermissionHandler {
  boolean canExecute(final CommandInformation commandInformation, final CommandSender sender);
}