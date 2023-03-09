package de.dojaphd.sendserver.core;

import de.dojaphd.sendserver.core.gui.activity.ShortcutActivity;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;
import java.util.HashMap;
import java.util.Map;

@ConfigName("settings")
public final class AddonConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @Exclude
  private final Map<String, ShortcutManager> shortcuts = new HashMap<>();

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }


  public Map<String, ShortcutManager> getShortcuts() {
    return this.shortcuts;
  }

  @MethodOrder(after = "enabled")
  @ActivitySetting
  public Activity openNameTags() {
    return new ShortcutActivity(false);
  }

  public void removeInvalidNameTags() {
    this.shortcuts.entrySet()
        .removeIf(entry -> entry.getKey().isEmpty() || entry.getValue().getServerIp().isEmpty());
  }
}