package com.barf.tradefinder.domain;

public enum Item {
  // @formatter:off
  OFFER(544, ItemType.OFFER),

  CREDITS(2615, ItemType.CREDIT),
  CREDITS_OFFER(1701, ItemType.CREDIT),
  NCVR_OFFER(1703, ItemType.CREDIT),

  BERET(336, ItemType.TOPPER),
  BIRTHDAY_CAKE(334, ItemType.TOPPER),
  BOWLER(322, ItemType.TOPPER),
  BRODIE_HELMET(324, ItemType.TOPPER),
  BYCOCKET(862, ItemType.TOPPER),
  CATFISH(843, ItemType.TOPPER),
  CHAINSAW(331, ItemType.TOPPER),
  CHEFS_HAT(330, ItemType.TOPPER),
  CROMULON(686, ItemType.TOPPER),
  DERBY(326, ItemType.TOPPER),
  DEVIL_HORNS(89, ItemType.TOPPER),
  DRINK_HELMET(386, ItemType.TOPPER),
  FEZ(90, ItemType.TOPPER),
  FIRE_HELMET(91, ItemType.TOPPER),
  FOAM_HAT(493, ItemType.TOPPER),
  FRUIT_HAT(323, ItemType.TOPPER),
  HALO(93, ItemType.TOPPER),
  HARD_HAT(94, ItemType.TOPPER),
  HAWAIIAN_LEI(851, ItemType.TOPPER),
  HEART_GLASSES(848, ItemType.TOPPER),
  HOMBURG(325, ItemType.TOPPER),
  IVY_CAP(391, ItemType.TOPPER),
  JACK_IN_THE_BOX(784, ItemType.TOPPER),
  LATTE(782, ItemType.TOPPER),
  LITTLE_BOW(388, ItemType.TOPPER),
  MARIACHI_HAT(95, ItemType.TOPPER),
  MMS_HEADPHONES(847, ItemType.TOPPER),
  OCTOPUS(852, ItemType.TOPPER),
  PIRATES_HAT(96, ItemType.TOPPER),
  PORK_PIE(390, ItemType.TOPPER),
  ROYAL_CROWN(99, ItemType.TOPPER),
  STEGOSAUR(858, ItemType.TOPPER),
  SURFBOARD(860, ItemType.TOPPER),
  TOP_HAT(102, ItemType.TOPPER),
  TRAFFIC_CONE(311, ItemType.TOPPER),
  TRUCKER_HAT(394, ItemType.TOPPER),
  UNICORN(310, ItemType.TOPPER),
  VISOR(395, ItemType.TOPPER),
  WILDCAT_EARS(385, ItemType.TOPPER),
  WITCHS_HAT(103, ItemType.TOPPER),
  WIZARD_HAT(104, ItemType.TOPPER),

  OTHER(0, ItemType.OTHER);
  // @formatter:on

  public enum ItemType {
    OFFER, CREDIT, TOPPER, OTHER;
  }

  private final int id;
  private final ItemType type;

  Item(final int id, final ItemType type) {
    this.id = id;
    this.type = type;
  }

  public static Item valueOf(final int id) {
    for (final Item item : Item.values()) {
      if (item.id == id) {
        return item;
      }
    }

    return Item.OTHER;
  }

  public static Item alphabeticTopperIndex(final int index) {
    int topperIndex = 0;
    for (final Item item : Item.values()) {
      if (item.getType().equals(ItemType.TOPPER)) {
        if (topperIndex == index) {
          return item;
        }
        topperIndex++;
      }
    }
    return Item.OTHER;
  }

  public int getId() {
    return this.id;
  }

  public ItemType getType() {
    return this.type;
  }
}
