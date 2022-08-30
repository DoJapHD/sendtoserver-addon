package de.dojaphd.sendserver.core;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CustomNameTag {

  private boolean enabled;
  private String customName;
  private boolean replaceScoreboard;

  private CustomNameTag(boolean enabled, String customName, boolean replaceScoreboard) {
    this.enabled = enabled;
    this.customName = customName;
    this.replaceScoreboard = replaceScoreboard;
  }

  public static CustomNameTag create(boolean enabled, String customName,
      boolean replaceScoreboard) {
    return new CustomNameTag(enabled, customName, replaceScoreboard);
  }

  public static CustomNameTag createDefault() {
    return create(true, "", false);
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getCustomName() {
    return this.customName;
  }

  public void setCustomName(String customName) {
    this.customName = customName;
  }

  public boolean isReplaceScoreboard() {
    return this.replaceScoreboard;
  }

  public void setReplaceScoreboard(boolean replaceScoreboard) {
    this.replaceScoreboard = replaceScoreboard;
  }

  public TextComponent getComponent() {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(this.getCustomName());
  }
}
