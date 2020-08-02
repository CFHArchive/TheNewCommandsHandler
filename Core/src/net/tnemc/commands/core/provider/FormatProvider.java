package net.tnemc.commands.core.provider;

public interface FormatProvider {

  String format(String message, boolean stripColours);
}