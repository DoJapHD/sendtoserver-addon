package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.CustomNameTag;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.I18n;

public class SendCommand extends Command {

  SendServerAddon addon = LabyGuice.getInstance(SendServerAddon.class);
  String syntax = "/ssasend [shortcut]";

  @Inject
  private SendCommand() {
    super("ssasend", "ssasend");

    this.translationKey("sendserveraddon.commands");
  }

  //Laby.labyAPI().minecraft().chatExecutor().displayClientMessage("Test");
  //Laby.labyAPI().minecraft().chatExecutor().copyToClipboard("CopyTest");


  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (prefix.equalsIgnoreCase("ssasend")) {
      String serverTarget = null;
      try {
        serverTarget = arguments[0];
      } catch (IndexOutOfBoundsException exception) {
        displayTranslatableMsg("general.syntax", NamedTextColor.RED, syntax);
        return true;
      }
      CustomNameTag serverIpTag = addon.getIp(serverTarget);

      if (serverIpTag == null) {
        displayTranslatableMsg("send.notfound", NamedTextColor.RED);
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
    if (this.translationKey != null) {
      translationKey = this.translationKey + "." + key;
    }

    String message = SendServerAddon.Prefix + I18n.translate(translationKey, arguments);
    this.displayMessage(Component.text(message, textColor));
  }

}
