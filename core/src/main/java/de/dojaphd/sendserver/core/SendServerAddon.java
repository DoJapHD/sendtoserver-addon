package de.dojaphd.sendserver.core;

import com.google.inject.Singleton;
import de.dojaphd.sendserver.core.commands.HelpCommand;
import de.dojaphd.sendserver.core.commands.MenuOpenerCommand;
import de.dojaphd.sendserver.core.commands.SendCommand;
import de.dojaphd.sendserver.core.commands.ShortcutCommand;
import de.dojaphd.sendserver.core.listener.ExampleGameTickListener;
import de.dojaphd.sendserver.core.utils.ModColor;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.event.client.scoreboard.TabListUpdateEvent;
import net.labymod.api.models.addon.annotation.AddonListener;

@AddonListener
@Singleton
public class SendServerAddon extends LabyAddon<AddonConfiguration> {

  public static SendServerAddon addon;

  public static String Prefix = ModColor.cl('7') + "[" + ModColor.cl('6') + "SendServerAddon" + ModColor.cl('7') + "] ";
  private Config config;

  @Override
  protected void enable() {
    this.registerSettingCategory();


    this.logger().info("[Send-Server-Addon] Addon loaded.");

    addon = this;

    init();
  }


  private void init() {
    this.registerListener(ExampleGameTickListener.class);

    this.registerCommand(SendCommand.class);
    this.registerCommand(ShortcutCommand.class);
    this.registerCommand(HelpCommand.class);
    this.registerCommand(MenuOpenerCommand.class);
  }

  public static SendServerAddon getAddon() {
    return addon;
  }

  @Override
  protected Class<AddonConfiguration> configurationClass() {
    return AddonConfiguration.class;
  }

  public void reloadTabList() {
    this.labyAPI().eventBus().fire(new TabListUpdateEvent());
  }
}
