package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.labymod.api.client.chat.command.Command;

public class SendCommand extends Command{

  @Inject
  private SendCommand() {
    super("ssasend", "ssasend");
  }


    //Laby.labyAPI().minecraft().chatExecutor().displayClientMessage("Test");
    //Laby.labyAPI().minecraft().chatExecutor().copyToClipboard("CopyTest");


  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssasend")) {
      String serverTarget = null;
      try {
        serverTarget = arguments[1];
      } catch (IndexOutOfBoundsException exception) {
        sendToUser("§cSyntax: /ssasend [shortcut]");
      }
      //for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String,JsonElement>>)SendServerAddon.getAddon().getAddonConfig)

      return true;
    }
    return false;
  }

  private void sendToUser(String msg) {
    SendServerAddon.getAddon().displayMessage(SendServerAddon.Prefix + msg);
  }
}
