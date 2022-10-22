package de.dojaphd.sendserver.core;

import com.google.inject.Singleton;
import de.dojaphd.sendserver.core.commands.HelpCommand;
import de.dojaphd.sendserver.core.commands.MenuOpenerCommand;
import de.dojaphd.sendserver.core.commands.SendCommand;
import de.dojaphd.sendserver.core.gui.activity.ShortcutActivity;
import de.dojaphd.sendserver.core.listener.KeyPressListener;
import de.dojaphd.sendserver.core.listener.TickListener;
import de.dojaphd.sendserver.core.utils.ModColor;
import java.util.Map;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.event.client.scoreboard.TabListUpdateEvent;
import net.labymod.api.inject.LabyGuice;
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

    this.registerListener(KeyPressListener.class);
    this.registerListener(TickListener.class);
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public void reloadTabList() {
    this.labyAPI().eventBus().fire(new TabListUpdateEvent());
  }

  public ShortcutManager getIp(String shortcut) {
    Map<String, ShortcutManager> shortcuts = addon.configuration().getCustomTags();

    for (String string : shortcuts.keySet()) {
      if (string.equalsIgnoreCase(shortcut)) {
        return shortcuts.get(string);
      }
    }
    return null;
  }

  public void openNameTagEditor() {
    ShortcutActivity activity = LabyGuice.getInstance(ShortcutActivity.class);
    activity.setBackground(true);
    openActivity(activity);
  }

  private void openActivity(ScreenInstance screenInstance) {
    Laby.labyAPI().minecraft().executeNextTick(
        () -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(screenInstance));
  }
}
