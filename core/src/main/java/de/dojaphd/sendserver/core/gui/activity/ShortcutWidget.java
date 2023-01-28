package de.dojaphd.sendserver.core.gui.activity;

import de.dojaphd.sendserver.core.ShortcutManager;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;

@AutoWidget
public class ShortcutWidget extends SimpleWidget {

  private String shortcut;
  private ShortcutManager shortcutManager;

  public ShortcutWidget(String shortcut, ShortcutManager shortcutManager) {
    this.shortcut = shortcut;
    this.shortcutManager = shortcutManager;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget nameWidget = ComponentWidget.component(Component.text(this.shortcut));
    nameWidget.addId("name");
    this.addChild(nameWidget);

    ComponentWidget customNameWidget = ComponentWidget.component(this.shortcutManager.getComponent());
    customNameWidget.addId("custom-name");
    this.addChild(customNameWidget);
  }

  public String getShortcut() {
    return this.shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public ShortcutManager getCustomTag() {
    return this.shortcutManager;
  }

  public void setCustomTag(ShortcutManager shortcutManager) {
    this.shortcutManager = shortcutManager;
  }
}
