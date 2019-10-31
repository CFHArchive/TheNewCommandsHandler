package net.tnemc.commands.core.utils;

import java.util.Optional;

@FunctionalInterface
public interface CommandTranslator {

  /**
   * Used to translate messages in The New Commands Handler. This method is
   * passed the configuration nodes in Example.Node.OtherNode format which should
   * then be used by your translator to return a translated String.
   * @param text The configuration node to translate.
   * @return An optional with the translated String, or Optional.empty() if you
   * don't wish to translate it.
   */
  Optional<String> translateText(String text);
}