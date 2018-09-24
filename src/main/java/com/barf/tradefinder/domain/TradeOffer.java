package com.barf.tradefinder.domain;

import java.util.List;

import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem.Color;

public class TradeOffer {

  private final List<PaintedItem> has;
  private final List<PaintedItem> wants;
  private final String tradeLink;

  public TradeOffer(final List<PaintedItem> has, final List<PaintedItem> wants, final String link) {
    this.has = has;
    this.wants = wants;
    this.tradeLink = link;
  }

  public String getTradeLink() {
    return this.tradeLink;
  }

  public boolean hasContainsKey() {
    return this.has.stream().filter(p -> p.getItem().getType().equals(ItemType.KEY)).findFirst().isPresent();
  }

  public boolean hasContainsItem() {
    return this.has.stream().filter(p -> !p.getItem().getType().equals(ItemType.OFFER) && !p.getItem().getType().equals(ItemType.CRATE))
        .findFirst().isPresent();
  }

  public boolean wantsContainsOneOf(final Item item, final List<Color> colors) {
    return this.wants.stream().filter(p -> p.getItem().equals(item) && colors.contains(p.getColor())).findFirst().isPresent();
  }

  public String getLink() {
    return "https://rocket-league.com" + this.tradeLink;
  }

  @Override
  public int hashCode() {
    return this.tradeLink.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final TradeOffer other = (TradeOffer) obj;
    if (this.tradeLink == null) {
      if (other.tradeLink != null) {
        return false;
      }
    } else if (!this.tradeLink.equals(other.tradeLink)) {
      return false;
    }
    return true;
  }
}
