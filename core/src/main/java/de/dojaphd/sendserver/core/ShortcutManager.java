package de.dojaphd.sendserver.core;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ShortcutManager {

  private String serverIp;

  private ShortcutManager(String serverIp) {
    this.serverIp = serverIp;
  }

  public static ShortcutManager create(String serverIp) {
    return new ShortcutManager(serverIp);
  }

  public static ShortcutManager createDefault() {
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
