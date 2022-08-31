package de.dojaphd.sendserver.core.gui.activity;

import net.kyori.adventure.text.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.navigation.NavigationElement;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.Activity;

public class AddonNavigationElement extends NavigationElement {

  private final ScreenInstance screen;

  private AddonNavigationElement(ScreenInstance screen) {
    this.screen = screen;
  }

  public Class<? extends Activity> getActivityClass() {
    return NameTagActivity.class;
  }

  @Override
  public String getWidgetWrapperId() {
    return "sendserver-navigation-wrapper";
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("sendserveraddon.gui.navigation.name");
  }

  @Override
  public Icon getIcon() {
    return null;
  }

  @Override
  public ScreenInstance createScreen() throws Exception {
    return screen;
  }

  @Override
  public Class<?> getScreenClass() {
    return NameTagActivity.class;
  }
}
