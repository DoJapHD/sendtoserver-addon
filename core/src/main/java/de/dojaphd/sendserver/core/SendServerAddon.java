package de.dojaphd.sendserver.core;

import com.google.inject.Singleton;
import de.dojaphd.sendserver.core.commands.HelpCommand;
import de.dojaphd.sendserver.core.commands.MenuOpenerCommand;
import de.dojaphd.sendserver.core.commands.SendCommand;
import de.dojaphd.sendserver.core.utils.ModColor;
import java.util.Map;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import net.labymod.api.models.addon.annotation.AddonListener;

@AddonListener
@Singleton
public class SendServerAddon extends LabyAddon<AddonConfiguration> {

  public static SendServerAddon addon;
  public static String Prefix =
      ModColor.cl('7') + "[" + ModColor.cl('6') + "SendServerAddon" + ModColor.cl('7') + "] §r";

  public static SendServerAddon getAddon() {
    return addon;
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.logger().info("[Send-Server-Addon] Addon loaded.");

    addon = this;

    init();
  }

  private void init() {
    this.registerCommand(SendCommand.class);
    this.registerCommand(HelpCommand.class);
    this.registerCommand(MenuOpenerCommand.class);
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public void reloadShortcutsList() {
    this.labyAPI().eventBus().fire(new ConfigurationSaveEvent());
  }

  public ShortcutManager getIp(String shortcut) {
    Map<String, ShortcutManager> shortcuts = addon.configuration().getShortcuts();

    for (String string : shortcuts.keySet()) {
      if (string.equalsIgnoreCase(shortcut)) {
        return shortcuts.get(string);
      }
    }
    return null;
  }
}
