package net.tnemc.commands.core.parameter;

import net.tnemc.commands.core.provider.PlayerProvider;

public interface ParameterParser {

  String parse(PlayerProvider sender, String argument);
}