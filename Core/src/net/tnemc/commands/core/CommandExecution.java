package net.tnemc.commands.core;

import net.tnemc.commands.core.provider.PlayerProvider;

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
@FunctionalInterface
public interface CommandExecution {

  default boolean canExecute(final CommandInformation commandInformation, final PlayerProvider sender) {
    return CommandsHandler.manager().getPermissionHandler().canExecute(commandInformation, sender);
  }

  boolean execute(PlayerProvider sender, String label, String[] arguments);
}
