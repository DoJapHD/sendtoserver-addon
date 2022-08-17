package de.dojaphd.sendserver.core.listener;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class ExampleGameTickListener {

  private final SendServerAddon addon;

  @Inject
  private ExampleGameTickListener(SendServerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onGameTick(GameTickEvent event) {
    if (event.phase() != Phase.PRE) {
      return;
    }

    //this.addon.logger().info(this.addon.configuration().enabled().get() ? "enabled" : "disabled");
  }
}
