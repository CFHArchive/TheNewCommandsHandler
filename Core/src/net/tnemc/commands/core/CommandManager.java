package net.tnemc.commands.core;

import net.tnemc.commands.core.provider.ImplementationProvider;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.commands.core.utils.CommandTranslator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The New Commands Handler Library
 * <p>
 * Created by creatorfromhell on 10/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandManager {

  Map<List<String>, CommandInformation> commands = new HashMap<>();
  Map<String, TabCompleter> completers = new HashMap<>();
  Map<String, CommandExecution> executors = new HashMap<>();

  private CommandPermissionHandler permissionHandler;
  private CommandTranslator translator = null;
  private ImplementationProvider provider;

  private Integer lastRegister = 0;
  private Field commandMap = null;
  private Field knownCommands = null;

  public CommandManager(final ImplementationProvider provider, CommandPermissionHandler permissionHandler) {
    this.provider = provider;
    this.permissionHandler = permissionHandler;
  }

  /**
   * Used to translate a list of Strings into a list of Strings with the {@link CommandTranslator}.
   * @param message The list of messages to translate.
   * @param sender An optional containing the CommandSender that caused the translation call, or an
   * empty Optional if no CommandSender was involved.
   * @param defaultMessage The default message if the message isn't translated.
   * @return The translated output when possible, otherwise the default message.
   */
  public LinkedList<String> translate(LinkedList<String> message, Optional<PlayerProvider> sender, LinkedList<String> defaultMessage) {
    ////System.out.println("Translate: " + message);
    if(translator != null) {

      LinkedList<String> translated = new LinkedList<>();

      for(String str : message) {
        final Optional<String> trans = translator.translateText(str, sender);

        trans.ifPresent(translated::add);
      }
      return translated;
    }
    ////System.out.println("Default: " + defaultMessage);
    return defaultMessage;
  }

  /**
   * Used to translate a configuration node into a list of Strings with the {@link CommandTranslator}.
   * @param message The message to translate.
   * @param sender An optional containing the CommandSender that caused the translation call, or an
   * empty Optional if no CommandSender was involved.
   * @param defaultMessage The default message if the message isn't translated.
   * @return The translated output when possible, otherwise the default message.
   */
  public LinkedList<String> translate(String message, Optional<PlayerProvider> sender, LinkedList<String> defaultMessage) {
    ////System.out.println("Translate: " + message);
    if(translator != null) {
      final Optional<LinkedList<String>> translated = translator.translateToList(message, sender);

      if(translated.isPresent()) {
        ////System.out.println("Translated: " + translated.get());


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        ////System.out.println("========== STACK ==========");
        ////System.out.println("String: " + stackTraceElements[0].toString());
        ////System.out.println("String: " + stackTraceElements[1].toString());
        ////System.out.println("String: " + stackTraceElements[2].toString());
        ////System.out.println("String: " + stackTraceElements[3].toString());
        ////System.out.println("========== END ==========");

        return translated.get();
      }
    }
    ////System.out.println("Default: " + defaultMessage);
    return defaultMessage;
  }

  /**
   * Used to translate a String with the {@link CommandTranslator}.
   * @param message The message to translate.
   * @param sender An optional containing the CommandSender that caused the translation call, or an
   * empty Optional if no CommandSender was involved.
   * @param defaultMessage The default message if the message isn't translated.
   * @return The translated output when possible, otherwise the default message.
   */
  public String translate(String message, Optional<PlayerProvider> sender, String defaultMessage) {
    ////System.out.println("Translate: " + message);
    if(translator != null) {
      final Optional<String> translated = translator.translateText(message, sender);

      if(translated.isPresent()) {


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        ////System.out.println("========== STACK ==========");
        for(StackTraceElement e : stackTraceElements) {
          ////System.out.println("String: " + e.toString());
        }
        ////System.out.println("========== END ==========");
        ////System.out.println("Translated: " + translated.get());
        return translated.get();
      }
    }
    ////System.out.println("Default: " + defaultMessage);
    return defaultMessage;
  }

  public Optional<CommandInformation> find(String name) {

    Iterator<Map.Entry<List<String>, CommandInformation>> it = commands.entrySet().iterator();

    while(it.hasNext()) {
      final Map.Entry<List<String>, CommandInformation> entry = it.next();

      for(String str : entry.getKey()) {
        if(str.equalsIgnoreCase(name)) return entry.getValue().find(name);
      }
    }
    return Optional.empty();
  }

  /**
   * Used to conduct a registration search. This will automagically find any sub net.tnemc.commands using the
   * arguments array too.
   * @param name The registration identifier.
   * @param arguments The String array of arguments passed in the registration call.
   * @return The {@link CommandSearchInformation} object associated with this search.
   */
  public Optional<CommandSearchInformation> search(String name, String[] arguments) {
    Optional<CommandInformation> information = find(name);

    if(information.isPresent()) {
      ////////System.out.println(information.get().toString());
      final CommandSearchInformation search = information.get().findSubInformation(arguments);
      return Optional.of(search);
    }
    return Optional.empty();
  }

  public void registerCommands() {
    if(lastRegister == commands.size()) return;

    lastRegister = commands.size();
    provider.registration().preRegistration();

    if(commandMap != null && knownCommands != null) {

      Iterator<Map.Entry<List<String>, CommandInformation>> i = commands.entrySet().iterator();

      while(i.hasNext()) {
        Map.Entry<List<String>, CommandInformation> entry = i.next();

        for (String s : entry.getKey()) {
          if(provider.registration().isRegistered(s)) {
            unregister(s, false);
          }
          provider.registration().register(s);
        }
      }
    }
  }

  public void registerExecutor(String name, CommandExecution executor) {
    executors.put(name, executor);
  }

  public void unregister(String[] accessors) {
    unregister(accessors, false);
  }

  public void unregister(String[] accessors, boolean commandsMap) {
    for(String s : accessors) {
      unregister(s, commandsMap);
    }
  }

  public void register(List<String> alias, CommandInformation information) {
    commands.put(alias, information);

    for (String s : alias) {
      if(provider.registration().isRegistered(s.toLowerCase())) {
        unregister(s.toLowerCase(), false);
      }
      provider.registration().register(s.toLowerCase());
    }
  }

  public void unregister(String command, boolean commandsMap) {
    try {

      if(commandsMap) {
        Iterator<Map.Entry<List<String>, CommandInformation>> it = commands.entrySet().iterator();

        while (it.hasNext()) {
          Map.Entry<List<String>, CommandInformation> entry = it.next();

          boolean remove = false;
          for (String str : entry.getKey()) {
            ////////System.out.println("CommandManager.unregister(" + command + ")");
            if (str.equalsIgnoreCase(command)) {
              ////////System.out.println("CommandManager.unregister(remove = true)");
              remove = true;
            }
          }
          if (remove) it.remove();
        }
      }
      provider.registration().unregister(command);
    } catch(Exception ignore) {
      //nothing to see here;
    }
  }

  public Map<List<String>, CommandInformation> getCommands() {
    return commands;
  }

  public void setCommands(Map<List<String>, CommandInformation> commands) {
    this.commands = commands;
  }

  public Map<String, TabCompleter> getCompleters() {
    return completers;
  }

  public void setCompleters(Map<String, TabCompleter> completers) {
    this.completers = completers;
  }

  public void addExecutor(String name, CommandExecution execution) {
    executors.put(name, execution);
  }

  public Map<String, CommandExecution> getExecutors() {
    return executors;
  }

  public void setExecutors(Map<String, CommandExecution> executors) {
    this.executors = executors;
  }

  public CommandPermissionHandler getPermissionHandler() {
    return permissionHandler;
  }

  public void setPermissionHandler(CommandPermissionHandler permissionHandler) {
    this.permissionHandler = permissionHandler;
  }

  public CommandTranslator getTranslator() {
    return translator;
  }

  public void setTranslator(CommandTranslator translator) {
    this.translator = translator;
  }

  public ImplementationProvider provider() {
    return provider;
  }
}