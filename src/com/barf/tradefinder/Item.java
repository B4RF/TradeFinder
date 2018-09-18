package com.barf.tradefinder;

import java.util.HashMap;
import java.util.Map;

public enum Item {
  // @formatter:off
  OFFER(544, ItemType.OFFER),

  KEY(496, ItemType.KEY),
  GOLDEN_EGG(1298, ItemType.KEY),

  CHAMPIONS_CRATE_1(494, ItemType.CRATE),
  CHAMPIONS_CRATE_2(495, ItemType.CRATE),
  CHAMPIONS_CRATE_3(502, ItemType.CRATE),
  CHAMPIONS_CRATE_4(545, ItemType.CRATE),
  PLAYERS_CHOICE_CRATE_1(587, ItemType.CRATE),
  TURBO_CRATE(603, ItemType.CRATE),
  NITRO_CRATE(641, ItemType.CRATE),
  OVERDRIVE_CRATE(671, ItemType.CRATE),
  ACCELERATOR_CRATE(733, ItemType.CRATE),
  HAUNTED_HALLOWS_CRATE(881, ItemType.CRATE),
  VELOCITY_CRATE(961, ItemType.CRATE),
  SECRET_SANTA_CRATE(983, ItemType.CRATE),
  VICTORY_CRATE(1006, ItemType.CRATE),
  SPRING_FEVER_CRATE(1026, ItemType.CRATE),
  TRIUMPH_CRATE(1159, ItemType.CRATE),
  IMPACT_CRATE(1210, ItemType.CRATE),
  RL_BEACH_BLAST_CRATE(1276, ItemType.CRATE),
  ZEPHYR_CRATE(1305, ItemType.CRATE),

  //TODO toppers
  OTHER(0, ItemType.OTHER);
  // @formatter:on

  private enum ItemType {
    OFFER, KEY, CRATE, TOPPER, OTHER;
  }

  private final int id;
  private final ItemType type;
  private boolean painted;
  private static Map<Integer, Item> map = new HashMap<>();

  Item(final int id, final ItemType type) {
    this.id = id;
    this.type = type;
    this.painted = this.type.equals(ItemType.TOPPER);
  }

  static {
    for (final Item item : Item.values()) {
      Item.map.put(item.id, item);
    }
  }

  protected static Item valueOf(final int id, final int paint) {
    final Item item = Item.map.get(id);

    if ((item == null) || (item.painted && (paint == 0))) {
      return Item.OTHER;
    } else {
      return item;
    }
  }

  public int getId() {
    return this.id;
  }

  public ItemType getType() {
    return this.type;
  }

  public boolean isPainted() {
    return this.painted;
  }
}
