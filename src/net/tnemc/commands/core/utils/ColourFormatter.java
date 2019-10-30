package net.tnemc.commands.core.utils;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColourFormatter {

  public static final Map<String, String> colours;
  static {
    colours = new HashMap<>();
    //Colour Characters
    colours.put("<aqua>", ChatColor.AQUA.toString());
    colours.put("<black>", ChatColor.BLACK.toString());
    colours.put("<blue>", ChatColor.BLUE.toString());
    colours.put("<dark_aqua>", ChatColor.DARK_AQUA.toString());
    colours.put("<dark_blue>", ChatColor.DARK_BLUE.toString());
    colours.put("<dark_gray>", ChatColor.GRAY.toString());
    colours.put("<dark_grey>", ChatColor.GRAY.toString());
    colours.put("<dark_green>", ChatColor.DARK_GREEN.toString());
    colours.put("<dark_purple>", ChatColor.DARK_PURPLE.toString());
    colours.put("<dark_red>", ChatColor.DARK_RED.toString());
    colours.put("<gold>", ChatColor.GOLD.toString());
    colours.put("<gray>", ChatColor.GRAY.toString());
    colours.put("<grey>", ChatColor.GRAY.toString());
    colours.put("<green>", ChatColor.GREEN.toString());
    colours.put("<purple>", ChatColor.LIGHT_PURPLE.toString());
    colours.put("<red>", ChatColor.RED.toString());
    colours.put("<white>", ChatColor.WHITE.toString());
    colours.put("<yellow>", ChatColor.YELLOW.toString());

    //Special Characters
    colours.put("<magic>", ChatColor.MAGIC.toString());
    colours.put("<bold>", ChatColor.BOLD.toString());
    colours.put("<strike>", ChatColor.STRIKETHROUGH.toString());
    colours.put("<underline>", ChatColor.UNDERLINE.toString());
    colours.put("<italic>", ChatColor.ITALIC.toString());
    colours.put("<reset>", ChatColor.RESET.toString());
  }

  public static String format(String message, boolean stripColours) {
    Iterator<Map.Entry<String, String>> it = colours.entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<String, String> entry = it.next();
      String replacement = (stripColours)? "" : entry.getValue();
      message = message.replace(entry.getKey(), replacement);
    }

    if(stripColours) {
      return ChatColor.stripColor(message);
    }
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}