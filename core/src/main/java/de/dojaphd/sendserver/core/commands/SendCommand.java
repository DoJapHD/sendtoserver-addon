package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.labymod.api.client.chat.command.Command;

public class SendCommand extends Command{

  SendServerAddon addon = LabyGuice.getInstance(SendServerAddon.class);
  
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
        serverTarget = arguments[0];
      } catch (IndexOutOfBoundsException exception) {
        sendToUser("§cSyntax: /ssasend [shortcut]");
        return true;
      }
      String serverIp = addon.getIp(serverTarget).getServerIp();

      if (serverIp == null) {
        sendToUser("Blub mit not found und so");
        return true;
      }

      addon.labyAPI().serverController().joinServer(serverIp);
      return true;
    }
    return false;
  }

  private void sendToUser(String msg) {
    SendServerAddon.getAddon().displayMessage(SendServerAddon.Prefix + msg);
  }
}
