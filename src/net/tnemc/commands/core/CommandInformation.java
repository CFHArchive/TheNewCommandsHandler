package net.tnemc.commands.core;

import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.commands.core.settings.MessageSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The New Commands Handler Library
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

  //The amount of parameters that are required.
  private int requiredArguments = 0;

  //The instance of the parent command
  private CommandInformation parent;

  //Cached command help.
  private String help = "";

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

    for(String str : getIdentifiers(false)) {
      if(str.equalsIgnoreCase(identifier)) return Optional.of(this);
    }

    for(CommandInformation information : sub.values()) {
      final Optional<CommandInformation> subFind = information.find(identifier);

      if(subFind.isPresent()) {
        return subFind;
      }
    }

    return Optional.empty();
  }

  public CommandSearchInformation findSubInformation(final String[] arguments) {

    List<String> argumentsList = new ArrayList<>(Arrays.asList(arguments));

    CommandInformation info = this;

    CommandSearchInformation subInformation = new CommandSearchInformation(info);

    String identifier = (argumentsList.size() > 0)? argumentsList.get(0) : "";

    Optional<CommandInformation> sub;

    //System.out.println("ArgumentList Size: " + argumentsList.size());
    //System.out.println("Identifier: " + identifier);
    while(!identifier.equalsIgnoreCase("") && (sub = findSub(identifier)).isPresent()) {
      subInformation.setInformation(sub.get());

      if(argumentsList.size() > 0) argumentsList.remove(0);
      identifier = (argumentsList.size() > 0)? argumentsList.get(0) : "";
    }

    if(argumentsList.size() > 0) {
      subInformation.setArguments(argumentsList.toArray(new String[argumentsList.size() - 1]));
    } else {
      subInformation.setArguments(new String[0]);
    }

    return subInformation;
  }

  public String getCompleter(int argumentLength) {
    //System.out.println("Length: " + argumentLength);
    if(parameters.containsKey(argumentLength)) {
      //System.out.println("Length: " + argumentLength);
      final CommandParameter param = parameters.get(argumentLength);

      //System.out.println("comp: " + param.getCompleteType());
      if(param.isTabComplete()) {
        return param.getCompleteType();
      }
    }
    return "";
  }

  public Optional<CommandInformation> findSub(String name) {
    for(Map.Entry<List<String>, CommandInformation> entry : sub.entrySet()) {
      for(String str : entry.getKey()) {
        if(str.equalsIgnoreCase(name)) return Optional.of(entry.getValue());
      }
    }
    return Optional.empty();
  }

  //Getter/Setter methods below
  public void addSub(CommandInformation information) {
    information.setSubCommand(true);
    sub.put(information.getIdentifiers(), information);
  }

  public boolean hasSub(String name) {
    for(List<String> identifiers : sub.keySet()) {
      for(String str : identifiers) {
        if(str.equalsIgnoreCase(name)) return true;
      }
    }
    return false;
  }

  public void addParameter(CommandParameter parameter) {
    if(parameter.getOrder() == -1) {
      parameter.setOrder(parameters.size());
    }
    if(!parameter.isOptional()) requiredArguments += 1;
    //System.out.println("Required Params: " + requiredArguments);
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

  public int getRequiredArguments() {
    return requiredArguments;
  }

  public void setRequiredArguments(int requiredArguments) {
    this.requiredArguments = requiredArguments;
  }

  public void buildHelp() {
    String help = CommandsHandler.manager().translate("Messages.Command.CommandHelp", MessageSettings.commandHelp);
    help = help.replace("$command", buildCommand());
    help = help.replace("$description", description);
    help = help.replace("$parameters", buildParameters());

    this.help = help;
  }

  public String buildCommand() {
    StringBuilder builder = new StringBuilder();

    CommandInformation information = this;

    while(information.isSubCommand()) {
      information = information.parent;

      String append = information.name.toLowerCase();

      if(builder.length() > 0) append += " ";

      builder.insert(0, append);
    }

    String append = information.name.toLowerCase();
    if(builder.length() > 0) append += " ";

    builder.insert(0, append);

    return builder.toString();
  }

  public String buildParameters() {
    StringBuilder builder = new StringBuilder();

    for(CommandParameter param : parameters.values()) {
      if(builder.length() > 0) builder.append(" ");
      final String paramStr = (param.isOptional())? CommandsHandler.manager().translate("Messages.Parameter.ParameterOption", MessageSettings.parameterOption) :
          CommandsHandler.manager().translate("Messages.Parameter.ParameterRequired", MessageSettings.parameterRequired);
      builder.append(paramStr.replace("$parameter", param.getName().toLowerCase()));
    }
    return builder.toString();
  }

  public String getHelp() {
    return help;
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

  public CommandInformation getParent() {
    return parent;
  }

  public void setParent(CommandInformation parent) {
    this.parent = parent;
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