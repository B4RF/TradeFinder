package com.barf.tradefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem;
import com.barf.tradefinder.domain.TradeOffer;

public class Request {

  final static int maxTries = 5;
  static boolean hasConnectionIssues = false;

  public static List<TradeOffer> getOffersForItem(final int id) {
    final List<TradeOffer> tradeOffers = new ArrayList<>();

    final List<Document> pages = Request.offerItem(id, 1);
    for (final Document page : pages) {

      final Elements offers = page.getElementsByClass("rlg-trade-display-container is--user");
      for (final Element element : offers) {
        final String tradeLink = element.getElementsByClass("rlg-trade-link-container").select("a").first().attr("href");

        // uncolored toppers are filtered so this is a valid if
        final List<PaintedItem> wants = Request.getItemsInElement(element.getElementById("rlg-theiritems"));
        if (wants.stream().filter(p -> p.getItem().getType().equals(ItemType.TOPPER)).findFirst().isPresent()) {
          final List<PaintedItem> has = Request.getItemsInElement(element.getElementById("rlg-youritems"));

          tradeOffers.add(new TradeOffer(has, wants, tradeLink));
        }
      }
    }

    return tradeOffers;
  }

  private static List<Document> offerItem(final int id, final int page) {
    final List<Document> documents = new ArrayList<>();

    String url = "https://rocket-league.com/trading?filterItem=" + id
        + "&filterCertification=0&filterPaint=0&filterPlatform=1&filterSearchType=2";
    if (page > 1) {
      url += "&p=" + page;
    }

    Document doc = null;
    int retries;

    for (retries = 0; (retries < Request.maxTries) && ((doc == null)
        || doc.getElementsByClass("rlg-trade-pagination-button").isEmpty()); retries++) {
      try {
        Thread.sleep(retries * 500l);
        doc = Jsoup.connect(url).get();
      } catch (final IOException | InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    if ((doc == null) || doc.getElementsByClass("rlg-trade-pagination-button").isEmpty()) {
      Request.hasConnectionIssues = true;
    } else {
      documents.add(doc);
      final String pageLink = doc.getElementsByClass("rlg-trade-pagination-button").last().attr("href");

      if (pageLink.length() > 0) {
        final int nextPage = Integer.parseInt(pageLink.substring(pageLink.length() - 1));
        documents.addAll(Request.offerItem(id, nextPage));
      }
    }

    return documents;
  }

  private static List<PaintedItem> getItemsInElement(final Element element) {
    final List<PaintedItem> items = new ArrayList<>();

    final Pattern p = Pattern.compile("(\\d+)");

    for (final Element itemElement : element.select("a")) {
      final String itemLink = itemElement.attr("href");
      final Matcher m = p.matcher(itemLink);

      m.find();
      final int itemId = Integer.parseInt(m.group());
      m.find();
      m.find();
      final int paintId = Integer.parseInt(m.group());

      int amount = 1;
      final Element amountElement = itemElement.getElementsByClass("rlg-trade-display-item__amount is--rare").first();
      if (amountElement != null) {
        amount = Integer.parseInt(amountElement.text());
      }

      items.add(new PaintedItem(itemId, paintId, amount));
    }

    return items;
  }
}
