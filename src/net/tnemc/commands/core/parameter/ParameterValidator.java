package net.tnemc.commands.core.parameter;

@FunctionalInterface
public interface ParameterValidator {

  boolean valid(String regex, String value);
}