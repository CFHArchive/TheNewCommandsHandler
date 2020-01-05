package net.tnemc.commands.core.parameter.parsers;

import net.tnemc.commands.core.parameter.ParameterParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Random;

public class PlayerParser implements ParameterParser {
  @Override
  public String parse(CommandSender sender, String argument) {
    String parsed = argument;

    if(argument.startsWith("@")) {
      LinkedList<String> players = new LinkedList<>();

      if(argument.equalsIgnoreCase("@a")) {

        for(Player player : Bukkit.getOnlinePlayers()) {
          players.add(ChatColor.stripColor(player.getDisplayName()));
        }
        parsed = String.join(",", players);
      } else if(argument.equalsIgnoreCase("@r")) {

        for(Player player : Bukkit.getOnlinePlayers()) {
          players.add(ChatColor.stripColor(player.getDisplayName()));
        }

        parsed = players.get(new Random().nextInt(players.size()));
      } else if(argument.equalsIgnoreCase("@p") && sender instanceof Player) {
        return ChatColor.stripColor(((Player)sender).getDisplayName());
      }
      players.clear();
    }
    return parsed;
  }
}
