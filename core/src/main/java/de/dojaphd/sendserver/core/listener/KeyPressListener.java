package de.dojaphd.sendserver.core.listener;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.labymod.api.event.client.input.KeyPressedEvent;

public class KeyPressListener {

  private final SendServerAddon addon;

  @Inject
  private KeyPressListener(SendServerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onKeyPress(KeyEvent event) {
    if(event.state() != State.PRESS || !Laby.labyAPI().minecraft().isMouseLocked()) {
      return;
    }

    ConfigProperty<Key> hotkey = this.addon.configuration().getKeybind();
    this.addon.logger().info("Event Key ID: " + event.key().getLegacyId());
    this.addon.logger().info("Hotkey Key ID: " + hotkey.get().getLegacyId());
    if (event.key().getId() == hotkey.get().getId()) {
      //this.addon.openNameTagEditor();
      this.addon.logger().info("Is equal");
    }
  }
}
