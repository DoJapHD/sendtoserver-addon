package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.command.Command;
import java.util.Arrays;
import java.util.Objects;

public class ShortcutCommand extends Command {

  @Inject
  private ShortcutCommand() {
    super("ssashortcut", "ssashortcut");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssashortcut")) {
      /*if(arguments.length != 1) {
        sendToUser("§cSyntax: /ssashortcut <add | remove | list>");
      } else*/ if (arguments.length == 1) {
        switch (arguments[0].toLowerCase()) {
          case "list":
            this.displayMessage("ListCMD");
            //displayList();
            break;
          case "add":
            this.displayMessage("AddCMD");
            //add
            break;
          case "remove":
            this.displayMessage("RemoveCMD");
            //remove
            break;
        }
      }

      return true;
    }

    this.displayMessage(Component.text("Pong!", NamedTextColor.GOLD));
    return false;
  }



  /*
  Jan Code
   */
  public boolean onSend(String message) {
    if (!message.startsWith("-shortcut"))
      return false;

    String[] splits = message.split(" ");
    String[] args = new String[splits.length - 1];
    if (splits.length - 1 <= 0) {
      sendToUser("§c-shortcut <add | remove | list>");
      return true;
    }
    System.arraycopy(splits, 1, args, 0, splits.length - 1);

    if (args[0].equalsIgnoreCase("list")) {
      //displayList();
      return true;
    }

    String settingArg = args[0].toLowerCase();

    if (!settingArg.equals("add") && !settingArg.equals("remove")) {
      sendToUser("§c-shortcut <add | remove | list>");
      return true;
    }

    if (args.length < 2) {
      String messageToSend = "§c-shortcut " + settingArg + " <shortcut>";
      if (settingArg.equals("add"))
        messageToSend += " <server>";
      sendToUser(messageToSend);
      return true;
    }

    /*if (settingArg.equals("remove")) {
      if (SendServerAddon.getAddon().removeShortcut(args[1])) {
        sendToUser("§aShortcut removed.");
        return true;
      }
      sendToUser("§cShortcut was never added.");
      return true;
    }

    if (args.length < 3) {
      sendToUser("§c-shortcut add <shortcut> <server>");
      return true;
    }

    SendServerAddon.getAddon().addShortcut(args[1], args[2]);
    sendToUser("§aShortcut added.");
*/
    return true;
  }

  private void sendToUser(String msg) {
    //SendServerAddon.getAddon().getApi().displayMessageInChat(SendServerAddon.Prefix + msg);
    this.displayMessage(Component.text(SendServerAddon.Prefix + msg));
  }

  /*private void displayList() {
    sendToUser("§aShortcuts:");
    SendServerAddon.getAddon().getAddonConfig().getConfigAsJsonObject().get("shortcuts").getAsJsonObject()
        .entrySet().forEach(entry -> sendToUser("§e" + entry.getKey() + " §r-§6> §2" + entry.getValue().getAsString()));
  }*/
}
