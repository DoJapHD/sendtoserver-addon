package de.dojaphd.sendserver.core.gui.activity;

import de.dojaphd.sendserver.core.CustomNameTag;
import net.kyori.adventure.text.Component;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;

@AutoWidget
public class ShortcutWidget extends SimpleWidget {

  private String shortcut;
  private CustomNameTag customNameTag;

  public ShortcutWidget(String shortcut, CustomNameTag customNameTag) {
    this.shortcut = shortcut;
    this.customNameTag = customNameTag;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    ComponentWidget nameWidget = ComponentWidget.component(Component.text(this.shortcut));
    nameWidget.addId("name");
    this.addChild(nameWidget);

    ComponentWidget customNameWidget = ComponentWidget.component(this.customNameTag.getComponent());
    customNameWidget.addId("custom-name");
    this.addChild(customNameWidget);
  }

  public String getShortcut() {
    return this.shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public CustomNameTag getCustomTag() {
    return this.customNameTag;
  }

  public void setCustomTag(CustomNameTag customNameTag) {
    this.customNameTag = customNameTag;
  }
}
