package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.AddonConfiguration;
import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.gui.activity.AddonNavigationElement;
import de.dojaphd.sendserver.core.gui.activity.NameTagActivity;
import net.kyori.adventure.text.Component;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.AddonActivityWidget;
import net.labymod.api.loader.LabyModLoader;
import java.util.Arrays;

public class MenuOpenerCommand extends Command {

AddonNavigationElement addonNavigationElement;

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
      if(arguments.length != 0) {
        sendToUser("§cSyntax: /ssashortcuts");
      }
      System.out.println("Passt");
      try {
        Laby.labyAPI().navigationService().updateLastOpenedElement(addonNavigationElement);

      } catch (Exception e) {
        System.out.println(e);
      }
      SendServerAddon.getAddon().configuration().openNameTags();


      return true;
    }
    return false;
  }

  private void sendToUser(String msg) {
    //SendServerAddon.getAddon().getApi().displayMessageInChat(SendServerAddon.Prefix + msg);
    this.displayMessage(Component.text(SendServerAddon.Prefix + msg));
  }

}
