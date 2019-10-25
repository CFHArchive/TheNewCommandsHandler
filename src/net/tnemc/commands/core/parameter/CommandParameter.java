package net.tnemc.commands.core.parameter;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandParameter {

  private int order = -1;
  private String name;
  private boolean optional = true;

  //Validation variables.
  private String type = "string";
  private int maxLength = 0;
  private boolean useRegex = false;
  private String regex = "";

  //Tab completion variables.
  private boolean tabComplete = false;
  private String completeType = "unknown";

  public CommandParameter(String name) {
    this.name = name;
  }

  public CommandParameter(int order, String name) {
    this.name = name;
    this.order = order;
  }

  public CommandParameter(String name, boolean optional, boolean tabComplete, String completeType) {
    this.name = name;
    this.optional = optional;
    this.tabComplete = tabComplete;
    this.completeType = completeType;
  }

  public CommandParameter(int order, String name, boolean optional, boolean tabComplete, String completeType) {
    this.name = name;
    this.order = order;
    this.optional = optional;
    this.tabComplete = tabComplete;
    this.completeType = completeType;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
    this.optional = optional;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  public boolean isUseRegex() {
    return useRegex;
  }

  public void setUseRegex(boolean useRegex) {
    this.useRegex = useRegex;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public boolean isTabComplete() {
    return tabComplete;
  }

  public void setTabComplete(boolean tabComplete) {
    this.tabComplete = tabComplete;
  }

  public String getCompleteType() {
    return completeType;
  }

  public void setCompleteType(String completeType) {
    this.completeType = completeType;
  }
}