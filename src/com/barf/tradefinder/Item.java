package com.barf.tradefinder;

import java.util.HashMap;
import java.util.Map;

public enum Item {

  TEST_ITEM(0, ItemType.OTHER, 0);

  private final int id;
  private final ItemType type;
  private final Paint color;

  Item(final int id, final ItemType type, final int color) {
    this.id = id;
    this.type = type;
    this.color = Paint.valueOf(color);
  }

  public int getId() {
    return this.id;
  }

  public ItemType getType() {
    return this.type;
  }

  public boolean isPainted() {
    return !this.color.equals(Paint.NONE);
  }

  // internal enums

  private enum ItemType {
    OFFER, CRATE, TOPPER, KEY, OTHER;
  }

  private enum Paint {
    NONE(0), BLACK(1), BURNT_SIENNA(2), COBALT(3), CRIMSON(4), FOREST_GREEN(5), GREY(6), LIME(7), ORANGE(8), PINK(9), PURPLE(10), SAFFRON(
        11), SKY_BLUE(12), TITANIUM_WHITE(13);

    int id;
    private static Map<Integer, Paint> map = new HashMap<>();

    private Paint(final int id) {
      this.id = id;
    }

    static {
      for (final Paint paint : Paint.values()) {
        Paint.map.put(paint.id, paint);
      }
    }

    protected static Paint valueOf(final int id) {
      return Paint.map.get(id);
    }
  }
}
