package net.tnemc.commands.core.completer.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.completer.ConfigCompleter;

import java.util.LinkedList;

public class PlayerCompleter extends ConfigCompleter {
  public PlayerCompleter() {

    super((sender, search, argument)->{

      final CommandInformation information = search.get().getInformation().get();

      LinkedList<String> players = new LinkedList<>();

      for(String name : CommandsHandler.provider().onlinePlayers()) {

        if(!argument.equalsIgnoreCase("")) {
          if(name.startsWith(argument)) {
            players.add(name);
          }
        } else {
          players.add(name);
        }

        if(players.size() >= 5) break;
      }
      return players;
    }, "player", 5);
  }
}