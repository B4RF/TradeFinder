package com.barf.tradefinder.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum Item {
  // @formatter:off
  OFFER(544, ItemType.OFFER),

  KEY(496, ItemType.KEY),
  GOLDEN_EGG(1298, ItemType.KEY),

  ACCELERATOR_CRATE(733, ItemType.CRATE),
  CHAMPIONS_CRATE_1(494, ItemType.CRATE),
  CHAMPIONS_CRATE_2(495, ItemType.CRATE),
  CHAMPIONS_CRATE_3(502, ItemType.CRATE),
  CHAMPIONS_CRATE_4(545, ItemType.CRATE),
  HAUNTED_HALLOWS_CRATE(881, ItemType.CRATE),
  IMPACT_CRATE(1210, ItemType.CRATE),
  NITRO_CRATE(641, ItemType.CRATE),
  OVERDRIVE_CRATE(671, ItemType.CRATE),
  PLAYERS_CHOICE_CRATE_1(587, ItemType.CRATE),
  RL_BEACH_BLAST_CRATE(1276, ItemType.CRATE),
  SECRET_SANTA_CRATE(983, ItemType.CRATE),
  SPRING_FEVER_CRATE(1026, ItemType.CRATE),
  TRIUMPH_CRATE(1159, ItemType.CRATE),
  TURBO_CRATE(603, ItemType.CRATE),
  VELOCITY_CRATE(961, ItemType.CRATE),
  VICTORY_CRATE(1006, ItemType.CRATE),
  ZEPHYR_CRATE(1305, ItemType.CRATE),

  BERET(336, ItemType.TOPPER),
  BIRTHDAY_CAKE(334, ItemType.TOPPER),
  BOWLER(322, ItemType.TOPPER),
  BRODIE_HELMET(324, ItemType.TOPPER),
//  BYCOCKET(862, ItemType.TOPPER),
//  CATFISH(843, ItemType.TOPPER),
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
//  HALO(93, ItemType.TOPPER),
  HARD_HAT(94, ItemType.TOPPER),
  HAWAIIAN_LEI(851, ItemType.TOPPER),
  HEART_GLASSES(848, ItemType.TOPPER),
  HOMBURG(325, ItemType.TOPPER),
  HUSTLE_BROWS(658, ItemType.TOPPER, false),
  IVY_CAP(391, ItemType.TOPPER),
  JACK_IN_THE_BOX(784, ItemType.TOPPER),
//  LATTE(782, ItemType.TOPPER),
  LITTLE_BOW(388, ItemType.TOPPER),
  MACARON(1329, ItemType.TOPPER),
  MARIACHI_HAT(95, ItemType.TOPPER),
//  MMS_HEADPHONES(847, ItemType.TOPPER),
//  OCTOPUS(852, ItemType.TOPPER),
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
    OFFER, KEY, CRATE, TOPPER, OTHER;
  }

  private final int id;
  private final ItemType type;
  private boolean painted;
  private static List<Item> list = new ArrayList<>();

  Item(final int id, final ItemType type) {
    this(id, type, type.equals(ItemType.TOPPER));
  }

  Item(final int id, final ItemType type, final boolean painted) {
    this.id = id;
    this.type = type;
    this.painted = painted;
  }

  static {
    for (final Item item : Item.values()) {
      Item.list.add(item);
    }
  }

  public static Item valueOf(final int id, final int paint) {
    final Optional<Item> optional = Item.list.stream().filter(i -> i.id == id).findFirst();
    final Item item = optional.isPresent() ? optional.get() : null;

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

  public static List<Item> getAllItems() {
    return Item.list;
  }
}
