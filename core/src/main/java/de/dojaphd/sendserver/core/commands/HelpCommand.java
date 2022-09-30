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
  }


  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssahelp")) {
      if (arguments.length != 0) {
        displayTranslatableMsg("sendserveraddon.commands.general.syntax", NamedTextColor.RED, syntax);
      } else {
        sendMessage();
      }
      return true;
    }
    return false;
  }


  public void sendMessage() {
    displayTranslatableMsg("sendserveraddon.commands.help.msg1", NamedTextColor.GREEN);
    displayTranslatableMsg("sendserveraddon.commands.help.msg2", NamedTextColor.GREEN);
    //displayTranslatableMsg("help.msg3", NamedTextColor.GREEN);
    displayTranslatableMsg("sendserveraddon.commands.help.msg4", NamedTextColor.GREEN);
    displayTranslatableMsg("sendserveraddon.commands.help.msg5", NamedTextColor.GREEN);
  }

  private void displayTranslatableMsg(String key, TextColor textColor, Object... arguments) {
    String translationKey = key;

    if(key.equals("help.msg5")) {
      String translation = I18n.translate("sendserveraddon.commands.help.msg1");
      String HelpWorldlengthFirstSplit = translation.split("§6")[1];
      int HelpWorldlengthSecondSplitLength = HelpWorldlengthFirstSplit.split(" §a")[0].length();
      StringBuilder sb = new StringBuilder(I18n.translate("sendserveraddon.commands.help.msg5"));
      for (int i = 0; i < HelpWorldlengthSecondSplitLength-5; i++) {
        sb.append("-");
      }
      String message = SendServerAddon.Prefix + sb.toString();
      this.displayMessage(Component.text(message, textColor));
    } else {
      String message = SendServerAddon.Prefix + I18n.translate(translationKey, arguments);
      this.displayMessage(Component.text(message, textColor));
    }
  }
}
