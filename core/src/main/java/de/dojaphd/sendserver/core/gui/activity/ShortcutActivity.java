package de.dojaphd.sendserver.core.gui.activity;

import de.dojaphd.sendserver.core.SendServerAddon;
import de.dojaphd.sendserver.core.ShortcutManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
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
import net.labymod.api.client.render.font.TextColorStripper;

@Link("manage.lss")
@Link("overview.lss")
@AutoActivity
public class ShortcutActivity extends Activity {

  private static final Pattern SHORTCUT_REGEX = Pattern.compile("[\\w.]{0,32}");
  private static final TextColorStripper TEXT_COLOR_STRIPPER = Laby.references()
      .textColorStripper();
  private final SendServerAddon addon;
  private final VerticalListWidget<ShortcutWidget> nameTagList;
  private final Map<String, ShortcutWidget> nameTagWidgets;
  private ShortcutWidget selectedNameTag;
  private ButtonWidget removeButton;
  private ButtonWidget editButton;

  private FlexibleContentWidget inputWidget;
  private String lastUserName;
  private String lastCustomName;

  private Action action;
  private boolean background = false;


  public ShortcutActivity(boolean background) {
    this.addon = SendServerAddon.getAddon();
    addon.reloadShortcutsList();
    this.background = background;

    this.nameTagWidgets = new HashMap<>();
    addon.configuration().getShortcuts().forEach((userName, customTag) -> {
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

    FlexibleContentWidget container = new FlexibleContentWidget();
    container.addId("name-tag-container");
    for (ShortcutWidget shortcutWidget : this.nameTagWidgets.values()) {
      this.nameTagList.addChild(shortcutWidget);
    }

    if (background) {
      DivWidget containerBackground = new DivWidget();
      containerBackground.addId("container-background");
      containerBackground.addChild(container);
      this.document().addChild(containerBackground);
    }

    container.addFlexibleContent(new ScrollWidget(this.nameTagList));

    this.selectedNameTag = this.nameTagList.session().getSelectedEntry();
    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("overview-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.add", () -> this.setAction(Action.ADD)));

    this.editButton = ButtonWidget.i18n("labymod.ui.button.edit",
        () -> this.setAction(Action.EDIT));
    this.editButton.setEnabled(this.selectedNameTag != null);
    menu.addEntry(this.editButton);

    this.removeButton = ButtonWidget.i18n("labymod.ui.button.remove",
        () -> this.setAction(Action.REMOVE));
    this.removeButton.setEnabled(this.selectedNameTag != null);
    menu.addEntry(this.removeButton);

    container.addContent(menu);
    if (!background) {
      this.document().addChild(container);
    }
    if (this.action == null) {
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
        overlayWidget = this.initializeManageContainer(this.selectedNameTag);
        break;
      case REMOVE:
        overlayWidget = this.initializeRemoveContainer(this.selectedNameTag);
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

    ShortcutWidget previewWidget = new ShortcutWidget(shortcutWidget.getShortcut(),
        shortcutWidget.getCustomTag());
    previewWidget.addId("remove-preview");
    this.inputWidget.addContent(previewWidget);

    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("remove-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.remove", () -> {
      this.addon.configuration().getShortcuts().remove(shortcutWidget.getShortcut());
      this.nameTagWidgets.remove(shortcutWidget.getShortcut());
      this.nameTagList.session().setSelectedEntry(null);
      this.setAction(null);
      this.addon.reloadShortcutsList();
    }));

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    this.inputWidget.addContent(menu);

    return this.inputWidget;
  }

  private DivWidget initializeManageContainer(ShortcutWidget shortcutWidget) {
    TextFieldWidget customTextField = new TextFieldWidget();
    ButtonWidget doneButton = ButtonWidget.i18n("labymod.ui.button.done");

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
    nameTextField.validator(newValue -> SHORTCUT_REGEX.matcher(newValue).matches());
    nameTextField.updateListener(newValue -> {
      doneButton.setEnabled(
          !newValue.trim().isEmpty() && !this.getStrippedText(customTextField.getText()).isEmpty()
      );
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

    customTextField.setText(shortcutWidget.getCustomTag().getServerIp());
    customTextField.validator(newValue -> SHORTCUT_REGEX.matcher(newValue).matches());
    customTextField.updateListener(newValue -> {
      doneButton.setEnabled(
          !this.getStrippedText(newValue).isEmpty() && !nameTextField.getText().trim().isEmpty()
      );
      if (newValue.equals(this.lastCustomName)) {
        return;
      }

      this.lastCustomName = newValue;
    });

    customNameList.addEntry(customTextField);
    this.inputWidget.addContent(customNameList);

    HorizontalListWidget buttonList = new HorizontalListWidget();
    buttonList.addId("edit-button-menu");

    doneButton.setEnabled(
        !nameTextField.getText().trim().isEmpty() && !this.getStrippedText(
            customTextField.getText()).isEmpty()
    );
    doneButton.setPressable(() -> {
      if (shortcutWidget.getCustomTag().getServerIp().length() == 0) {
        this.nameTagWidgets.put(nameTextField.getText(), shortcutWidget);
        this.nameTagList.session().setSelectedEntry(shortcutWidget);
      }

      this.addon.configuration().getShortcuts().remove(shortcutWidget.getShortcut());
      ShortcutManager customNameTag = shortcutWidget.getCustomTag();
      customNameTag.setServerIp(customTextField.getText());
      this.addon.configuration().getShortcuts().put(nameTextField.getText(), customNameTag);
      this.addon.configuration().removeInvalidNameTags();

      shortcutWidget.setShortcut(nameTextField.getText());
      shortcutWidget.setCustomTag(customNameTag);
      this.setAction(null);

      this.addon.reloadShortcutsList();
    });

    buttonList.addEntry(doneButton);

    buttonList.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    inputContainer.addChild(this.inputWidget);
    this.inputWidget.addContent(buttonList);
    return inputContainer;
  }

  private String getStrippedText(String text) {
    text = text.trim();
    if (text.isEmpty()) {
      return text;
    }

    return TEXT_COLOR_STRIPPER.stripColorCodes(text, '&');
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton mouseButton) {
    try {
      if (this.action != null) {
        return this.inputWidget.mouseClicked(mouse, mouseButton);
      }

      return super.mouseClicked(mouse, mouseButton);
    } finally {
      this.selectedNameTag = this.nameTagList.session().getSelectedEntry();
      this.removeButton.setEnabled(this.selectedNameTag != null);
      this.editButton.setEnabled(this.selectedNameTag != null);
    }
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if (key.getId() == 256 && this.action != null) {
      this.setAction(null);
      return true;
    }

    return super.keyPressed(key, type);
  }

  private void setAction(Action action) {
    this.action = action;
    this.reload();
  }

  public void setBackground(boolean background) {
    this.background = background;
  }

  private enum Action {
    ADD, EDIT, REMOVE
  }
}
