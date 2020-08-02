package net.tnemc.commands.core.provider;

public interface CommandRegistrationProvider {

  /**
   * Called before calls to {@link #register(String)}.
   */
  default void preRegistration() {

  }

  /**
   * Used to register a registration.
   * @param command The registration to register.
   */
  void register(final String command);

  /**
   * Used to unregister a registration.
   * @param command The registration to unregister.
   */
  void unregister(final String command);

  /**
   * Returns whether or not a registration is registered.
   * @param command The registration to check.
   * @return True if the registration is registered, otherwise false.
   */
  boolean isRegistered(final String command);
}