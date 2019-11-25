package net.tnemc.commands.core.cooldown;

import net.tnemc.commands.core.CommandsHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CooldownTask extends BukkitRunnable {

  private UUID player;
  private String command;

  public CooldownTask(UUID player, String command) {
    this.player = player;
    this.command = command;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    CommandsHandler.instance().getCooldownHandler().removeCooldown(player, command);
  }
}