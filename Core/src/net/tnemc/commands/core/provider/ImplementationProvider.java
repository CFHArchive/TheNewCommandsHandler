package net.tnemc.commands.core.provider;

import java.util.LinkedList;

public interface ImplementationProvider {

  LinkedList<String> onlinePlayers();

  CommandRegistrationProvider registration();

  FormatProvider formatter();
}