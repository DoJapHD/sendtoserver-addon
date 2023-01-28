package de.dojaphd.sendserver.core.commands;

import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.ShortcutManager;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.I18n;

public class SendCommand extends Command {

  SendServerAddon addon;
  String syntax = "/ssasend [shortcut]";

  public SendCommand(SendServerAddon addon) {
    super("ssasend", "ssasend");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (prefix.equalsIgnoreCase("ssasend")) {
      String serverTarget = null;
      try {
        serverTarget = arguments[0];
      } catch (IndexOutOfBoundsException exception) {
        displayTranslatableMsg("sendserveraddon.commands.general.syntax", NamedTextColor.RED,
            syntax);
        return true;
      }
      ShortcutManager serverIpTag = addon.getIp(serverTarget);

      if (serverIpTag == null) {
        displayTranslatableMsg("sendserveraddon.commands.send.notfound", NamedTextColor.RED);
        return true;
      }

      String serverIp = serverIpTag.getServerIp();
      serverIp = serverIp.replaceAll("&.", "");
      addon.labyAPI().serverController().joinServer(serverIp);
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
