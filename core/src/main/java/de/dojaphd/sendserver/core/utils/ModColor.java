package de.dojaphd.sendserver.core.utils;

import java.awt.Color;

public enum ModColor {
  BLACK('0', 0, 0, 0),
  DARK_BLUE('1', 0, 0, 170),
  DARK_GREEN('2', 0, 170, 0),
  DARK_AQUA('3', 0, 170, 170),
  DARK_RED('4', 170, 0, 0),
  DARK_PURPLE('5', 170, 0, 170),
  GOLD('6', 255, 170, 0),
  GRAY('7', 170, 170, 170),
  DARK_GRAY('8', 85, 85, 85),
  BLUE('9', 85, 85, 255),
  GREEN('a', 85, 255, 85),
  AQUA('b', 85, 255, 255),
  RED('c', 255, 85, 85),
  PINK('d', 255, 85, 255),
  YELLOW('e', 255, 255, 85),
  WHITE('f', 255, 255, 255),
  RESET('r'),
  BOLD('l'),
  ITALIC('o'),
  UNDERLINE('n'),
  MAGIC('k'),
  STRIKETHROUGH('m');

  public static final String[] COLOR_CODES = new String[]{"0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l", "o", "r"};
  public static final char COLOR_CHAR_PREFIX = '§';
  private final char colorChar;
  private Color color;

  ModColor(char colorChar, int r, int g, int b) {
    this.colorChar = colorChar;
    this.color = new Color(r, g, b);
  }

  ModColor(char colorChar) {
    this.colorChar = colorChar;
  }

  public static String cl(String colorChar) {
    return '§' + colorChar;
  }

  public static String cl(char colorChar) {
    return String.valueOf(new char[]{'§', colorChar});
  }

  private static String getColorCharPrefix() {
    return String.valueOf('§');
  }

  public static String removeColor(String string) {
    return string.replaceAll(getColorCharPrefix() + "[a-z0-9]", "");
  }

  public static String createColors(String string) {
    return string.replaceAll("(?i)&([a-z0-9])", "§$1");
  }

  public static String booleanToColor(boolean value) {
    return value ? GREEN.toString() : RED.toString();
  }

  public static int toRGB(int r, int g, int b, int a) {
    return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
  }

  public static String getCharAsString() {
    return String.valueOf('§');
  }

  public static Color changeBrightness(Color color, float fraction) {
    float red = (float) color.getRed() + 255.0F * fraction;
    if (red > 255.0F) {
      red = 255.0F;
    }

    if (red < 0.0F) {
      red = 0.0F;
    }

    float green = (float) color.getGreen() + 255.0F * fraction;
    if (green > 255.0F) {
      green = 255.0F;
    }

    if (green < 0.0F) {
      green = 0.0F;
    }

    float blue = (float) color.getBlue() + 255.0F * fraction;
    if (blue > 255.0F) {
      blue = 255.0F;
    }

    if (blue < 0.0F) {
      blue = 0.0F;
    }

    return new Color((int) red, (int) green, (int) blue);
  }

  public static Color getColorByString(String color) {
    if (color == null) {
      return null;
    } else {
      return color.equals("-1") ? null : new Color(Integer.parseInt(color));
    }
  }

  public String toString() {
    return String.valueOf('§') + this.colorChar;
  }

  public Color getColor() {
    return this.color;
  }

  public char getColorChar() {
    return this.colorChar;
  }
}
