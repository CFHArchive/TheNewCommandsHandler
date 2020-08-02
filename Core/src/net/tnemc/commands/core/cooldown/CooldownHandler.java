package net.tnemc.commands.core.cooldown;

import java.util.UUID;

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
public interface CooldownHandler {

  boolean addCooldown(UUID player, String command, long cooldown);

  boolean hasCooldown(UUID player, String command);

  void removeCooldown(UUID player, String command);

  int scheduleCooldown(final UUID player, final String command, final long cooldown);

  void cancelCooldown(final int id);
}