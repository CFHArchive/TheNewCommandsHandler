package net.tnemc.commands.core.utils;

import net.tnemc.commands.core.provider.PlayerProvider;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface CommandTranslator {

  /**
   * Used to translate messages in The New Commands Handler. This method is
   * passed the configuration nodes in Example.Node.OtherNode format which should
   * then be used by your translator to return a translated String.
   * @param text The configuration node to translate.
   * @param sender An optional containing the CommandSender that caused the translation call, or an
   * empty Optional if no CommandSender was involved.
   * @return An optional with the translated String list, or Optional.empty() if you
   * don't wish to translate it.
   */
  default Optional<LinkedList<String>> translateToList(String text, Optional<PlayerProvider> sender) {
    final Optional<String> translate = translateText(text, sender);

    if(translate.isPresent()) {
      LinkedList<String> translated = new LinkedList<>();
      translated.add(translate.get());
      return Optional.of(translated);
    }
    return Optional.empty();
  }

  /**
   * Used to translate messages in The New Commands Handler. This method is
   * passed the configuration nodes in Example.Node.OtherNode format which should
   * then be used by your translator to return a translated String.
   * @param text The configuration node to translate.
   * @param sender An optional containing the CommandSender that caused the translation call, or an
   * empty Optional if no CommandSender was involved.
   * @return An optional with the translated String, or Optional.empty() if you
   * don't wish to translate it.
   */
  Optional<String> translateText(String text, Optional<PlayerProvider> sender);
}