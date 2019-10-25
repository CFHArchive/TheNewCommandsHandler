package net.tnemc.commands.core.parameter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

public enum ParameterType {

  INT("integer", new String[] { "int", "i" }, (regex, value)->{
    try {
      Integer.parseInt(value);
    } catch(Exception ignore) {
      return false;
    }

    if(!regex.equalsIgnoreCase("")) {
      return Pattern.matches(regex, value);
    }
    return true;
  }),
  DOUBLE("double", new String[] { "doub", "dub", "d" }, (regex, value)->{
    try {
      Double.parseDouble(value);
    } catch(Exception ignore) {
      return false;
    }

    if(!regex.equalsIgnoreCase("")) {
      return Pattern.matches(regex, value);
    }
    return true;
  }),
  BOOLEAN("boolean", new String[] { "bool", "b" }, (regex, value)->{
    try {
      Boolean.parseBoolean(value);
    } catch(Exception ignore) {
      return false;
    }

    if(!regex.equalsIgnoreCase("")) {
      return Pattern.matches(regex, value);
    }
    return true;
  }),
  BIGDECIMAL("bigdecimal", new String[] { "decimal", "bigd", "bigdec", "bd" }, (regex, value)->{
    try {
      new BigDecimal(value);
    } catch(Exception ignore) {
      return false;
    }

    if(!regex.equalsIgnoreCase("")) {
      return Pattern.matches(regex, value);
    }
    return true;
  }),
  STRING("string", new String[] { "str", "s", "text" }, (regex, value)->{
    if(!regex.equalsIgnoreCase("")) {
      return Pattern.matches(regex, value);
    }
    return true;
  });
  private String name;
  private String[] alias;
  private ParameterValidator validator;

  ParameterType(String name, String[] alias, ParameterValidator validator) {
    this.name = name;
    this.alias = alias;
    this.validator = validator;
  }

  public static Optional<ParameterType> find(String name) {
    for(ParameterType type : values()) {
      if(type.name.equalsIgnoreCase(name)) return Optional.of(type);

      for(String shorten : type.alias) {
        if(shorten.equalsIgnoreCase(name)) return Optional.of(type);
      }
    }
    return Optional.empty();
  }

  public static boolean exists(String identifier) {
    return find(identifier).isPresent();
  }
}