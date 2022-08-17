package de.dojaphd.sendserver.core.commands;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.command.Command;

public class SendCommand extends Command {

  @Inject
  private SendCommand() {
    super("ssasend", "ssasend");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (prefix.equalsIgnoreCase("ssasend")) {
      this.displayMessage(Component.text("Ping!", NamedTextColor.AQUA));
      return true;
    }

    this.displayMessage(Component.text("Pong!", NamedTextColor.GOLD));
    return false;
  }
}
