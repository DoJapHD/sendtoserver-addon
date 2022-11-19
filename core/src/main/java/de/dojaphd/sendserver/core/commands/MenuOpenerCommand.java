package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.gui.activity.ShortcutActivity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.I18n;

public class MenuOpenerCommand extends Command {

  public static SendServerAddon addon;
  String syntax = "/ssashortcuts";

  @Inject
  private MenuOpenerCommand() {
    super("ssashortcuts", "ssashortcuts");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssashortcuts")) {
      if (arguments.length != 0) {
        displayTranslatableMsg("sendserveraddon.commands.general.syntax", NamedTextColor.RED,
            syntax);
      } else {
        try {
          ShortcutActivity activity = LabyGuice.getInstance(ShortcutActivity.class);
          activity.setBackground(true);
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
