package net.tnemc.commands.core.parameter;

import org.bukkit.command.CommandSender;

public interface ParameterParser {

  String parse(CommandSender sender, String argument);
}