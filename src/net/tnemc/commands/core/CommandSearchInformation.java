package net.tnemc.commands.core;

import java.util.Optional;

public class CommandSearchInformation {

  private CommandInformation information;
  private String[] arguments;

  public CommandSearchInformation(CommandInformation information) {
    this.information = information;
  }

  public CommandSearchInformation(CommandInformation information, String[] arguments) {
    this.information = information;
    this.arguments = arguments;
  }

  public Optional<CommandInformation> getInformation() {
    return Optional.of(information);
  }

  public void setInformation(CommandInformation information) {
    this.information = information;
  }

  public String[] getArguments() {
    return arguments;
  }

  public void setArguments(String[] arguments) {
    this.arguments = arguments;
  }
}