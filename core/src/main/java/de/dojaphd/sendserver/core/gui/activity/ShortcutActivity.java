package de.dojaphd.sendserver.core.gui.activity;

import com.google.inject.Inject;
import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.ShortcutManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

@Link("manage.lss")
@Link("overview.lss")
@AutoActivity
public class ShortcutActivity extends Activity {

  private final SendServerAddon addon;
  private final VerticalListWidget<ShortcutWidget> nameTagList;
  private final Map<String, ShortcutWidget> nameTagWidgets;

  private ButtonWidget removeButton;
  private ButtonWidget editButton;

  private FlexibleContentWidget inputWidget;
  private String lastUserName;
  private String lastCustomName;

  private Action action;
  private boolean updateRequired;
  private boolean background = false;

  @Inject
  private ShortcutActivity(SendServerAddon addon) {
    this.addon = addon;

    this.nameTagWidgets = new HashMap<>();
    addon.configuration().getCustomTags().forEach((userName, customTag) -> {
      this.nameTagWidgets.put(userName, new ShortcutWidget(userName, customTag));
    });

    this.nameTagList = new VerticalListWidget<>();
    this.nameTagList.addId("name-tag-list");
    this.nameTagList.setSelectCallback(shortcutWidget -> {
      ShortcutWidget selectedNameTag = this.nameTagList.session().getSelectedEntry();
      if (Objects.isNull(selectedNameTag)
          || selectedNameTag.getCustomTag() != shortcutWidget.getCustomTag()) {
        this.editButton.setEnabled(true);
        this.removeButton.setEnabled(true);
      }
    });

    this.nameTagList.setDoubleClickCallback(shortcutWidget -> this.setAction(Action.EDIT));
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    DivWidget listContainer = new DivWidget();
    listContainer.addId("name-tag-container");
    for (ShortcutWidget shortcutWidget : this.nameTagWidgets.values()) {
      this.nameTagList.addChild(shortcutWidget);
    }

    if (background) {
      DivWidget containerBackground = new DivWidget();
      containerBackground.addId("container-background");
      containerBackground.addChild(listContainer);
      this.document().addChild(containerBackground);
    }

    listContainer.addChild(new ScrollWidget(this.nameTagList));
    this.document().addChild(listContainer);

    ShortcutWidget selectedNameTag = this.nameTagList.session().getSelectedEntry();
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
        ShortcutWidget newCustomNameTag = new ShortcutWidget("", ShortcutManager.createDefault());
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

  private FlexibleContentWidget initializeRemoveContainer(ShortcutWidget shortcutWidget) {
    this.inputWidget = new FlexibleContentWidget();
    this.inputWidget.addId("remove-container");

    ComponentWidget confirmationWidget = ComponentWidget.i18n(
        "sendserveraddon.gui.manage.remove.title");
    confirmationWidget.addId("remove-confirmation");
    this.inputWidget.addContent(confirmationWidget);

    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("remove-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.remove", () -> {
      this.addon.configuration().getCustomTags().remove(shortcutWidget.getShortcut());
      this.nameTagWidgets.remove(shortcutWidget.getShortcut());
      this.nameTagList.session().setSelectedEntry(null);
      this.setAction(null);
      this.updateRequired = true;
    }));

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    this.inputWidget.addContent(menu);

    return this.inputWidget;
  }

  private DivWidget initializeManageContainer(ShortcutWidget shortcutWidget) {
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
    nameTextField.setText(shortcutWidget.getShortcut());
    nameTextField.updateListener(newValue -> {
      if (newValue.equals(this.lastUserName)) {
        return;
      }

      this.lastUserName = newValue;
    });

    nameList.addEntry(nameTextField);
    this.inputWidget.addContent(nameList);

    ComponentWidget labelCustomName = ComponentWidget.i18n(
        "sendserveraddon.gui.manage.custom.name");
    labelCustomName.addId("label-name");
    this.inputWidget.addContent(labelCustomName);

    HorizontalListWidget customNameList = new HorizontalListWidget();
    customNameList.addId("input-name-list");

    TextFieldWidget customTextField = new TextFieldWidget();
    customTextField.setText(shortcutWidget.getCustomTag().getServerIp());
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
      if (shortcutWidget.getShortcut().length() == 0) {
        this.nameTagWidgets.put(nameTextField.getText(), shortcutWidget);
        this.nameTagList.session().setSelectedEntry(shortcutWidget);
      }

      this.addon.configuration().getCustomTags().remove(shortcutWidget.getShortcut());
      ShortcutManager shortcutManager = shortcutWidget.getCustomTag();
      shortcutManager.setServerIp(customTextField.getText());
      if (Objects.equals(nameTextField.getText(), "")) {
        //Wenn oben nicht eingefüllt
        this.addon.configuration().getCustomTags()
            .put(shortcutManager.getServerIp(), shortcutManager);
        shortcutWidget.setShortcut(shortcutManager.getServerIp());
      } else if (Objects.equals(customTextField.getText(), "")) {
        //Wenn unten nicht eingefüllt
        shortcutManager.setServerIp(nameTextField.getText());
        this.addon.configuration().getCustomTags().put(nameTextField.getText(), shortcutManager);
      } else {
        //Wenn beide eingefüllt
        this.addon.configuration().getCustomTags().put(nameTextField.getText(), shortcutManager);
        shortcutWidget.setShortcut(nameTextField.getText());
      }

      shortcutWidget.setCustomTag(shortcutManager);
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
  public void onCloseScreen() {
    super.onCloseScreen();
    this.addon.reloadShortcutsList();
  }

  public void setBackground(boolean background) {
    this.background = background;
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return null;
  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return null;
  }

  private enum Action {
    ADD, EDIT, REMOVE
  }
}