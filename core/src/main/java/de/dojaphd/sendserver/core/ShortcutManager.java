package de.dojaphd.sendserver.core;

import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;

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
    return (TextComponent) LegacyComponentSerializer.legacyAmpersand()
        .deserialize(this.getServerIp());
  }
}
