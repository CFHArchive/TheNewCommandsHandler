package net.tnemc.commands.core;

import net.tnemc.commands.core.parameter.CommandParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandInformation {

  private NavigableMap<Integer, CommandParameter> parameters = new TreeMap<>();


  private Map<List<String>, CommandInformation> sub = new HashMap<>();

  private List<String> aliases;

  private boolean subCommand = false;
  private List<String> subShort = new ArrayList<>();

  private String name;
  private String description;
  private String permission;

  private String executor;
  private String author;
  private boolean console;
  private boolean developer;

  public CommandInformation(String name) {
    this.name = name;
  }

  public CommandInformation(List<String> aliases, String name, String description, String permission, String executor, String author, boolean console, boolean developer) {
    this.aliases = aliases;
    this.name = name;
    this.description = description;
    this.permission = permission;
    this.executor = executor;
    this.author = author;
    this.console = console;
    this.developer = developer;
  }

  //Methods with logic
  public Optional<CommandInformation> find(String identifier) {
    if(getIdentifiers(false).contains(identifier)) {
      return Optional.of(this);
    }

    for(CommandInformation information : sub.values()) {
      final Optional<CommandInformation> subFind = information.find(identifier);

      if(subFind.isPresent()) {
        return subFind;
      }
    }

    return Optional.empty();
  }

  public String getCompleter(int argumentLength) {
    return "";
  }

  //Getter/Setter methods below
  public void addSub(CommandInformation information) {
    information.setSubCommand(true);
    sub.put(information.getIdentifiers(), information);
  }

  public boolean hasSub(String name) {
    for(List<String> identifiers : sub.keySet()) {
      if(identifiers.contains(name)) return true;
    }
    return false;
  }

  public void addParameter(CommandParameter parameter) {
    if(parameter.getOrder() == -1) {
      parameter.setOrder(parameters.lastKey() + 1);
    }
    parameters.put(parameter.getOrder(), parameter);
  }

  public void removeParameter(String name) {
    parameters.remove(name);
  }

  public NavigableMap<Integer, CommandParameter> getParameters() {
    return parameters;
  }

  public void setParameters(NavigableMap<Integer, CommandParameter> parameters) {
    this.parameters = parameters;
  }

  public List<String> getIdentifiers() {
    List<String> identifiers = new ArrayList<>();
    identifiers.add(name);
    identifiers.addAll(aliases);

    for(CommandInformation info : sub.values()) {
      identifiers.addAll(info.getSubShort());
    }

    return identifiers;
  }

  public List<String> getIdentifiers(boolean shortValues) {
    List<String> identifiers = new ArrayList<>();
    identifiers.add(name);
    identifiers.addAll(aliases);

    if(shortValues) {
      for(CommandInformation info : sub.values()) {
        identifiers.addAll(info.getSubShort());
      }
    }
    return identifiers;
  }

  public Map<List<String>, CommandInformation> getSub() {
    return sub;
  }

  public void setSub(Map<List<String>, CommandInformation> sub) {
    this.sub = sub;
  }

  public boolean isSubCommand() {
    return subCommand;
  }

  public void setSubCommand(boolean subCommand) {
    this.subCommand = subCommand;
  }

  public List<String> getSubShort() {
    return subShort;
  }

  public void setSubShort(List<String> subShort) {
    this.subShort = subShort;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getExecutor() {
    return executor;
  }

  public void setExecutor(String executor) {
    this.executor = executor;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public boolean isConsole() {
    return console;
  }

  public void setConsole(boolean console) {
    this.console = console;
  }

  public boolean isDeveloper() {
    return developer;
  }

  public void setDeveloper(boolean developer) {
    this.developer = developer;
  }

  public void addParameters(LinkedList<CommandParameter> parameters) {

    for(CommandParameter param : parameters)  {
      addParameter(param);
    }
  }

  @Override
  public String toString() {
    return "CommandInformation {" +
        "aliases=" + String.join(",", aliases) +
        ", subCommand=" + subCommand +
        ", subShort=" + subShort +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", permission='" + permission + '\'' +
        ", executor='" + executor + '\'' +
        ", author='" + author + '\'' +
        ", console=" + console +
        ", developer=" + developer +
        '}';
  }
}