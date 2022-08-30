package de.dojaphd.sendserver.core;

import de.dojaphd.sendserver.core.gui.activity.NameTagActivity;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.AddonActivityWidget.AddonActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget.ColorPickerSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.inject.LabyGuice;
import net.labymod.api.util.MethodOrder;
import java.util.HashMap;
import java.util.Map;

@ConfigName("settings")
public final class AddonConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> hideNameTagBackground = new ConfigProperty<>(false);
  @ColorPickerSetting
  private final ConfigProperty<Integer> color = new ConfigProperty<>(0);

  private Map<String, CustomNameTag> customTags = new HashMap<>();

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> shouldHideNameTagBackground() {
    return this.hideNameTagBackground;
  }

  public ConfigProperty<Integer> color() {
    return this.color;
  }

  public Map<String, CustomNameTag> getCustomTags() {
    return this.customTags;
  }

  @MethodOrder(after = "enabled")
  @AddonActivitySetting
  public Activity openNameTags() {
    return LabyGuice.getInstance(NameTagActivity.class);
  }
}