package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.util.I18n;

public class MenuOpenerCommand extends Command {

  //AddonNavigationElement addonNavigationElement;
  String syntax = "/ssashortcuts";

  @Inject
  private MenuOpenerCommand() {
    super("ssashortcuts", "ssashortcuts");
  }

  /**
   * Currently not working as no method found to implement idea
   **/

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssashortcuts")) {
      if (arguments.length != 0) {
        displayTranslatableMsg("sendserveraddon.commands.general.syntax", NamedTextColor.RED, syntax);
      }
      //System.out.println("Passt");
      try {
        //Laby.labyAPI().navigationService().updateLastOpenedElement(addonNavigationElement);

      } catch (Exception e) {
        System.out.println(e);
      }
      displayMessage("Command detected, success");
      SendServerAddon.getAddon().configuration().openNameTags();

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
