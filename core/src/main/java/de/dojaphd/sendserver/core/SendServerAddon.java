package de.dojaphd.sendserver.core;

import com.google.inject.Singleton;
import de.dojaphd.sendserver.core.commands.HelpCommand;
import de.dojaphd.sendserver.core.commands.SendCommand;
import de.dojaphd.sendserver.core.commands.ShortcutCommand;
import de.dojaphd.sendserver.core.utils.ModColor;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.loader.LabyModLoader;
import net.labymod.api.models.addon.annotation.AddonListener;
import de.dojaphd.sendserver.core.commands.ExamplePingCommand;
import de.dojaphd.sendserver.core.listener.ExampleGameTickListener;

@Singleton
@AddonListener
public class SendServerAddon extends LabyAddon<Configuration> {

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
  }

  public static SendServerAddon getAddon() {return addon;}

  /**public void addShortcut(String key, String server) {
    this.config.getConfigAsJsonObject().get("shortcuts").getAsJsonObject().addProperty(key, server);
    this.config.save();
  }

  public boolean removeShortcut(String key) {
    if (!this.config.getConfigAsJsonObject().get("shortcuts").getAsJsonObject().has(key))
      return false;
    this.config.getConfigAsJsonObject().get("shortcuts").getAsJsonObject().remove(key);
    this.config.save();
    return true;
  }*/
  @Override
  protected Class<Configuration> configurationClass() {
    return Configuration.class;
  }
}
