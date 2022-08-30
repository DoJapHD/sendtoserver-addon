package de.dojaphd.sendserver.core.gui.activity;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.CustomNameTag;
import de.dojaphd.sendserver.core.SendServerAddon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AutoActivity
@Link("manage.lss")
@Link("overview.lss")
public class NameTagActivity extends Activity {

  private final SendServerAddon addon;
  private final VerticalListWidget<NameTagWidget> nameTagList;
  private final Map<String, NameTagWidget> nameTagWidgets;

  private ButtonWidget removeButton;
  private ButtonWidget editButton;

  private FlexibleContentWidget inputWidget;
  private String lastUserName;
  private String lastCustomName;

  private Action action;
  private boolean updateRequired;

  @Inject
  private NameTagActivity(SendServerAddon addon) {
    this.addon = addon;

    this.nameTagWidgets = new HashMap<>();
    addon.configuration().getCustomTags().forEach((userName, customTag) -> {
      this.nameTagWidgets.put(userName, new NameTagWidget(userName, customTag));
    });

    this.nameTagList = new VerticalListWidget<>();
    this.nameTagList.addId("name-tag-list");
    this.nameTagList.setSelectCallback(nameTagWidget -> {
      NameTagWidget selectedNameTag = this.nameTagList.session().getSelectedEntry();
      if (Objects.isNull(selectedNameTag)
          || selectedNameTag.getCustomTag() != nameTagWidget.getCustomTag()) {
        this.editButton.setEnabled(true);
        this.removeButton.setEnabled(true);
      }
    });

    this.nameTagList.setDoubleClickCallback(nameTagWidget -> this.setAction(Action.EDIT));
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget listContainer = new DivWidget();
    listContainer.addId("name-tag-container");
    for (NameTagWidget nameTagWidget : this.nameTagWidgets.values()) {
      this.nameTagList.addChild(nameTagWidget);
    }

    listContainer.addChild(new ScrollWidget(this.nameTagList));
    this.document().addChild(listContainer);

    NameTagWidget selectedNameTag = this.nameTagList.session().getSelectedEntry();
    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("overview-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.add", () -> this.setAction(Action.ADD)));

    this.editButton = ButtonWidget.i18n("labymod.ui.button.edit",
        () -> this.setAction(Action.EDIT));
    this.editButton.setEnabled(Objects.nonNull(selectedNameTag));
    menu.addEntry(this.editButton);

    this.removeButton = ButtonWidget.i18n("labymod.ui.button.remove",
        () -> this.setAction(Action.REMOVE));
    this.removeButton.setEnabled(Objects.nonNull(selectedNameTag));
    menu.addEntry(this.removeButton);

    this.document().addChild(menu);
    if (Objects.isNull(this.action)) {
      return;
    }

    DivWidget manageContainer = new DivWidget();
    manageContainer.addId("manage-container");

    Widget overlayWidget;
    switch (this.action) {
      default:
      case ADD:
        NameTagWidget newCustomNameTag = new NameTagWidget("", CustomNameTag.createDefault());
        overlayWidget = this.initializeManageContainer(newCustomNameTag);
        break;
      case EDIT:
        overlayWidget = this.initializeManageContainer(selectedNameTag);
        break;
      case REMOVE:
        overlayWidget = this.initializeRemoveContainer(selectedNameTag);
        break;
    }

    manageContainer.addChild(overlayWidget);
    this.document().addChild(manageContainer);
  }

  private FlexibleContentWidget initializeRemoveContainer(NameTagWidget nameTagWidget) {
    this.inputWidget = new FlexibleContentWidget();
    this.inputWidget.addId("remove-container");

    ComponentWidget confirmationWidget = ComponentWidget.i18n(
        "sendserveraddon.gui.manage.remove.title");
    confirmationWidget.addId("remove-confirmation");
    this.inputWidget.addContent(confirmationWidget);

    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("remove-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.remove", () -> {
      this.addon.configuration().getCustomTags().remove(nameTagWidget.getShortcut());
      this.nameTagWidgets.remove(nameTagWidget.getShortcut());
      this.nameTagList.session().setSelectedEntry(null);
      this.setAction(null);
      this.updateRequired = true;
    }));

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    this.inputWidget.addContent(menu);

    return this.inputWidget;
  }

  private DivWidget initializeManageContainer(NameTagWidget nameTagWidget) {
    DivWidget inputContainer = new DivWidget();
    inputContainer.addId("input-container");

    this.inputWidget = new FlexibleContentWidget();
    this.inputWidget.addId("input-list");

    ComponentWidget labelName = ComponentWidget.i18n("sendserveraddon.gui.manage.name");
    labelName.addId("label-name");
    this.inputWidget.addContent(labelName);

    HorizontalListWidget nameList = new HorizontalListWidget();
    nameList.addId("input-name-list");

    TextFieldWidget nameTextField = new TextFieldWidget();
    nameTextField.setText(nameTagWidget.getShortcut());
    nameTextField.updateListener(newValue -> {
      if (newValue.equals(this.lastUserName)) {
        return;
      }

      this.lastUserName = newValue;
    });

    nameList.addEntry(nameTextField);
    this.inputWidget.addContent(nameList);

    ComponentWidget labelCustomName = ComponentWidget.i18n("sendserveraddon.gui.manage.custom.name");
    labelCustomName.addId("label-name");
    this.inputWidget.addContent(labelCustomName);

    HorizontalListWidget customNameList = new HorizontalListWidget();
    customNameList.addId("input-name-list");

    TextFieldWidget customTextField = new TextFieldWidget();
    customTextField.setText(nameTagWidget.getCustomTag().getServerIp());
    customTextField.updateListener(newValue -> {
      if (newValue.equals(this.lastCustomName)) {
        return;
      }

      this.lastCustomName = newValue;
    });


    customNameList.addEntry(customTextField);
    this.inputWidget.addContent(customNameList);

    HorizontalListWidget buttonList = new HorizontalListWidget();
    buttonList.addId("edit-button-menu");

    buttonList.addEntry(ButtonWidget.i18n("labymod.ui.button.done", () -> {
      if (nameTagWidget.getShortcut().length() == 0) {
        this.nameTagWidgets.put(nameTextField.getText(), nameTagWidget);
        this.nameTagList.session().setSelectedEntry(nameTagWidget);
      }

      this.addon.configuration().getCustomTags().remove(nameTagWidget.getShortcut());
      CustomNameTag customNameTag = nameTagWidget.getCustomTag();
      customNameTag.setServerIp(customTextField.getText());
      this.addon.configuration().getCustomTags().put(nameTextField.getText(), customNameTag);
      nameTagWidget.setShortcut(nameTextField.getText());
      nameTagWidget.setCustomTag(customNameTag);
      this.setAction(null);

      this.updateRequired = true;
    }));

    buttonList.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    inputContainer.addChild(this.inputWidget);
    this.inputWidget.addContent(buttonList);
    return inputContainer;
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton mouseButton) {
    if (Objects.nonNull(this.action)) {
      return this.inputWidget.mouseClicked(mouse, mouseButton);
    }

    return super.mouseClicked(mouse, mouseButton);
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if (key.getId() == 256 && Objects.nonNull(this.action)) {
      this.setAction(null);
      return true;
    }

    return super.keyPressed(key, type);
  }

  private void setAction(Action action) {
    this.action = action;
    this.reload();
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return null;
  }

  @Override
  public void onCloseScreen() {
    super.onCloseScreen();
    if (this.updateRequired) {
      this.addon.reloadTabList();
    }
  }

  private enum Action {
    ADD, EDIT, REMOVE
  }
}