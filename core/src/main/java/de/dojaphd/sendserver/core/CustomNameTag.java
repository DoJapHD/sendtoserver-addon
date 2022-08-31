package de.dojaphd.sendserver.core;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class CustomNameTag {

  private String serverIp;

  private CustomNameTag(String serverIp) {
    this.serverIp = serverIp;
  }

  public static CustomNameTag create(String serverIp) {
    return new CustomNameTag(serverIp);
  }

  public static CustomNameTag createDefault() {
    return create("");
  }


  public String getServerIp() {
    return this.serverIp;
  }

  public void setServerIp(String serverIp) {
    this.serverIp = serverIp;
  }

  public TextComponent getComponent() {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(this.getServerIp());
  }
}
