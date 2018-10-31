package com.barf.tradefinder.domain;

import java.util.List;

import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem.Color;

public class TradeOffer {

  private final List<PaintedItem> has;
  private final List<PaintedItem> wants;
  private final String tradeLink;
  private final String user;

  public TradeOffer(final List<PaintedItem> has, final List<PaintedItem> wants, final String link, final String user) {
    this.has = has;
    this.wants = wants;
    this.tradeLink = link;
    this.user = user;
  }

  public String getTradeLink() {
    return this.tradeLink;
  }

  public String getUser() {
    return this.user;
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
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.has == null) ? 0 : this.has.hashCode());
    result = (prime * result) + ((this.wants == null) ? 0 : this.wants.hashCode());
    result = (prime * result) + ((this.tradeLink == null) ? 0 : this.tradeLink.hashCode());
    return result;
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
    if (this.has == null) {
      if (other.has != null) {
        return false;
      }
    } else if (!this.has.equals(other.has)) {
      return false;
    }
    if (this.wants == null) {
      if (other.wants != null) {
        return false;
      }
    } else if (!this.wants.equals(other.wants)) {
      return false;
    }
    if (this.tradeLink == null) {
      if (other.tradeLink != null) {
        return false;
      }
    } else if (!this.tradeLink.equals(other.tradeLink)) {
      return false;
    }
    if (this.user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!this.user.equals(other.user)) {
      return false;
    }
    return true;
  }
}
