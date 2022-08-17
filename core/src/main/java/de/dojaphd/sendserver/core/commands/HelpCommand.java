package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.kyori.adventure.text.Component;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;

public class HelpCommand extends Command implements ChatMessageSendEvent {

  @Inject
  private HelpCommand() {
    super("ssahelp", "ssahelp");
  }

  public void cancelMessage(ChatMessageSendEvent e) {
    e.changeMessage("");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssahelp")) {
      sendMessage();
      cancelMessage();
      return false;
    }
    return true;
  }

  private void sendToUser(String msg) {
    //SendServerAddon.getAddon().getApi().displayMessageInChat(SendServerAddon.Prefix + msg);
    this.displayMessage(Component.text(SendServerAddon.Prefix + msg));
  }

  public void sendMessage() {
    sendToUser("§a--------- §6Help §a---------");
    sendToUser("§6/ssasend <shortcut> §7» §aConnect to a server using a shortcut.");
    sendToUser("§6/ssashortcut add <shortcut> <serverIp> §7» §aadd a new shortcut for a server.");
    sendToUser("§6/ssashortcut remove <shortcut> §7» §aRemove a shortcut from your list.");
    sendToUser("§6/ssashortcut list §7» §aLists all of your shortcuts.");
  }
}
