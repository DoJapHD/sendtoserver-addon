package de.dojaphd.sendserver.core.commands;

import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.gui.activity.ShortcutActivity;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.I18n;


public class MenuOpenerCommand extends Command {

  private final SendServerAddon addon;
  private final String syntax = "/ssashortcuts";


  public MenuOpenerCommand(SendServerAddon addon) {
    super("ssashortcuts", "ssashortcuts");
    this.addon = addon;

  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssashortcuts")) {
      if (arguments.length != 0) {
        displayTranslatableMsg("sendserveraddon.commands.general.syntax", NamedTextColor.RED,
            syntax);
      } else {
        try {
          ShortcutActivity activity = new ShortcutActivity(true);
          openActivity(activity);
          this.labyAPI.hudWidgetRegistry().saveConfig();
        } catch (Exception e) {
          addon.logger().error(e.toString());
        }
      }
      return true;
    }
    return false;
  }

  private void displayTranslatableMsg(String key, TextColor textColor, Object... arguments) {
    String translationKey = key;

    String message = SendServerAddon.Prefix + I18n.translate(translationKey, arguments);
    this.displayMessage(Component.text(message, textColor));
  }

}
