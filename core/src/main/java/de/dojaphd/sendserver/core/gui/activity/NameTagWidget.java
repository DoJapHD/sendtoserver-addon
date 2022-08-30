package de.dojaphd.sendserver.core.gui.activity;

import de.dojaphd.sendserver.core.CustomNameTag;
import net.kyori.adventure.text.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@AutoWidget
public class NameTagWidget extends SimpleWidget {

  private String userName;
  private CustomNameTag customNameTag;

  public NameTagWidget(String userName, CustomNameTag customNameTag) {
    this.userName = userName;
    this.customNameTag = customNameTag;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);
    /*IconWidget iconWidget = new IconWidget(this.getIconWidget(this.userName));
    iconWidget.addId("avatar");
    this.addChild(iconWidget);*/

    ComponentWidget nameWidget = ComponentWidget.component(Component.text(this.userName));
    nameWidget.addId("name");
    this.addChild(nameWidget);

    ComponentWidget customNameWidget = ComponentWidget.component(this.customNameTag.getComponent());
    customNameWidget.addId("custom-name");
    this.addChild(customNameWidget);
  }

  public Icon getIconWidget(String userName) {
    return Icon.head(userName.length() == 0 ? "MHF_Question" : userName);
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public CustomNameTag getCustomTag() {
    return this.customNameTag;
  }

  public void setCustomTag(CustomNameTag customNameTag) {
    this.customNameTag = customNameTag;
  }
}
