package com.barf.tradefinder.domain;

public class PaintedItem {

  Item item;
  Color color;

  public PaintedItem(final int itemId, final int colorId) {
    this(Item.valueOf(itemId), Color.valueOf(colorId));
  }

  public PaintedItem(final Item item, final Color color) {
    this.item = item;
    this.color = color;
  }

  public Item getItem() {
    return this.item;
  }

  public Color getColor() {
    return this.color;
  }

  public enum Color {
    // @formatter:off
    NONE(0),
    BLACK(13),
    BURNT_SIENNA(1),
    COBALT(4),
    CRIMSON(5),
    FOREST_GREEN(6),
    GREY(7),
    LIME(2),
    ORANGE(8),
    PINK(9),
    PURPLE(10),
    SAFFRON(11),
    SKY_BLUE(12),
    TITANIUM_WHITE(3);
    // @formatter:on

    int id;

    Color(final int id) {
      this.id = id;
    }

    public static Color valueOf(final int id) {
      for (final Color color : Color.values()) {
        if (color.id == id) {
          return color;
        }
      }
      return Color.NONE;
    }

    public static Color alphabeticIndex(final int index) {
      int colorIndex = 0;
      for (final Color color : Color.values()) {
        if (colorIndex == (index + 1)) {
          return color;
        }
        colorIndex++;
      }
      return Color.NONE;
    }
  }
}
