package net.tnemc.commands.core.utils;

@FunctionalInterface
public interface CommandTranslator {

  String translateText(String text);
}