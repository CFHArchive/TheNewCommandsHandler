package net.tnemc.commands.core.parameter.parsers;

import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.parameter.ParameterParser;
import net.tnemc.commands.core.provider.PlayerProvider;

import java.util.LinkedList;
import java.util.Random;

public class PlayerParser implements ParameterParser {
  @Override
  public String parse(PlayerProvider sender, String argument) {
    String parsed = argument;

    if(argument.startsWith("@")) {
      LinkedList<String> players = new LinkedList<>();

      if(argument.equalsIgnoreCase("@a")) {

        parsed = String.join(",", CommandsHandler.provider().onlinePlayers());
      } else if(argument.equalsIgnoreCase("@r")) {

        players.addAll(CommandsHandler.provider().onlinePlayers());

        parsed = players.get(new Random().nextInt(players.size()));
      } else if(argument.equalsIgnoreCase("@p") && sender.isPlayer()) {
        return sender.getDisplayName(true);
      }
      players.clear();
    }
    return parsed;
  }
}
