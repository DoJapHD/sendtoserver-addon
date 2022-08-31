package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.util.I18n;

public class HelpCommand extends Command {

  String syntax = "/ssahelp";

  @Inject
  private HelpCommand() {
    super("ssahelp", "ssahelp");

    this.translationKey("sendserveraddon.commands");
  }


  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssahelp")) {
      if (arguments.length != 0) {
        displayTranslatableMsg("general.syntax", NamedTextColor.RED, syntax);
      }
      sendMessage();
      return true;
    }
    return false;
  }


  public void sendMessage() {
    displayTranslatableMsg("help.msg1", NamedTextColor.GREEN);
    displayTranslatableMsg("help.msg2", NamedTextColor.GREEN);
    //displayTranslatableMsg("help.msg3", NamedTextColor.GREEN);
    displayTranslatableMsg("help.msg4", NamedTextColor.GREEN);
    displayTranslatableMsg("help.msg5", NamedTextColor.GREEN);
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
