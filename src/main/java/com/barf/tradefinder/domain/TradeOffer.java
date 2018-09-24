package com.barf.tradefinder.domain;

import java.util.List;

import com.barf.tradefinder.domain.Item.ItemType;

public class TradeOffer {

  private final List<Item> has;
  private final List<Item> wants;
  private final String tradeLink;

  public TradeOffer(final List<Item> has, final List<Item> wants, final String link) {
    this.has = has;
    this.wants = wants;
    this.tradeLink = link;
  }

  public String getTradeLink() {
    return this.tradeLink;
  }

  public boolean containsKey() {
    return this.has.stream().filter(i -> i.getType().equals(ItemType.KEY)).findFirst().isPresent();
  }

  public boolean containsItem() {
    return this.has.stream().filter(i -> !i.getType().equals(ItemType.OFFER) && !i.getType().equals(ItemType.CRATE)).findFirst()
        .isPresent();
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
