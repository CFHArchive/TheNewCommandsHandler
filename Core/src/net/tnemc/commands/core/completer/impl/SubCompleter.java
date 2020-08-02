package net.tnemc.commands.core.completer.impl;

import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.completer.ConfigCompleter;

import java.util.LinkedList;

public class SubCompleter extends ConfigCompleter {

  public SubCompleter() {
    super((sender, search, argument)->{
      final CommandInformation information = search.get().getInformation().get();

      LinkedList<String> sub = new LinkedList<>();

      for(CommandInformation subObj : information.getSub().values()) {

        if(!argument.equalsIgnoreCase("")) {
          for(String str : subObj.getIdentifiers()) {
            if(str.toLowerCase().startsWith(argument.toLowerCase())) sub.add(str.toLowerCase());
          }
        } else {
          sub.add(subObj.getName().toLowerCase());
        }

        if(sub.size() >= 5) break;
      }
      return sub;
    }, "sub_command", 5);
  }
}