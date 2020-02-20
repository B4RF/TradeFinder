package com.barf.tradefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem;
import com.barf.tradefinder.domain.TradeOffer;

import cmonster.cookies.DecryptedCookie;

public class Request {

  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36";

  final static int maxTries = 5;
  public static List<DecryptedCookie> cookies = new ArrayList<>();
  public static boolean hasConnectionIssues = false;

  public static List<TradeOffer> getOffersForItem(final int id, final TimeUnit maxUnit) {
    final List<TradeOffer> tradeOffers = new ArrayList<>();

    final List<Document> pages = Request.offerItem(id, 1);
    for (final Document page : pages) {

      final Elements offers = page.getElementsByClass("rlg-trade-display-container is--user");
      for (final Element element : offers) {

        // uncolored toppers are filtered so this is a valid if
        final List<PaintedItem> wants = Request.getItemsInElement(element.getElementById("rlg-theiritems"));

        if (wants.stream().filter(p -> p.getItem().getType().equals(ItemType.TOPPER)).findFirst().isPresent()) {
          final List<PaintedItem> has = Request.getItemsInElement(element.getElementById("rlg-youritems"));
          final String tradeLink = element.getElementsByClass("rlg-trade-link-container").select("a").first().attr("href");
          final String user = element.getElementsByClass("rlg-trade-player-link").first().text();
          final String lastActive = element.getElementsByClass("rlg-trade-display-added").first().text();
          final boolean supress = (maxUnit != null) && Request.isAfter(lastActive, maxUnit);

          tradeOffers.add(new TradeOffer(has, wants, tradeLink, user, supress));
        }
      }
    }

    return tradeOffers;
  }

  private static boolean isAfter(final String lastActive, final TimeUnit maxUnit) {
    // Active 5 seconds ago. Posted by
    final Pattern pattern = Pattern.compile("Active (\\d+) (\\w+) ago");
    final Matcher matcher = pattern.matcher(lastActive);
    matcher.find();
    final int amount = Integer.parseInt(matcher.group(1));
    final String unit = matcher.group(2);

    // only allow units below given
    switch (unit) {
    case "second":
    case "seconds":
    case "minute":
      return false;
    case "minutes":
      return TimeUnit.MINUTES.equals(maxUnit) && (amount > 10);
    case "hour":
      return TimeUnit.MINUTES.equals(maxUnit);
    case "hours":
    case "day":
      return TimeUnit.MINUTES.equals(maxUnit) || TimeUnit.HOURS.equals(maxUnit);
    }

    return true;
  }

  private static List<Document> offerItem(final int id, final int page) {
    final List<Document> documents = new ArrayList<>();

    String url = "https://rocket-league.com/trading?filterItem=" + id
        + "&filterCertification=0&filterPaint=A&filterPlatform=1&filterSearchType=2&filterItemType=1";
    if (page > 1) {
      url += "&p=" + page;
    }

    Document doc = null;
    int retries;

    for (retries = 0; (retries < Request.maxTries) && ((doc == null)
        || doc.getElementsByClass("rlg-trade-pagination-button").isEmpty()); retries++) {
      try {
        Thread.sleep(retries * 500l);
        final Connection connection = Jsoup.connect(url)
            .userAgent(Request.USER_AGENT)
            .header("Accept", "text/html")
            .header("Accept-Encoding", "gzip,deflate")
            .header("Accept-Language", "de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
            .referrer("http://www.google.com/")
            .followRedirects(true);
        for (final DecryptedCookie cookie : Request.cookies) {
          connection.cookie(cookie.getName(), cookie.getDecryptedValue());
        }
        doc = connection.get();

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

    final Pattern itemPattern = Pattern.compile("filterItem=(\\d+)");
    final Pattern paintPattern = Pattern.compile("filterPaint=(\\d+)");
    final Pattern itemTypePattern = Pattern.compile("filterItemType=(\\d+)");

    for (final Element itemElement : element.select("a")) {
      final String itemLink = itemElement.attr("href");

      Matcher m = itemPattern.matcher(itemLink);
      m.find();
      final int itemId = Integer.parseInt(m.group(1));

      m = paintPattern.matcher(itemLink);
      int paintId = 0;
      if (m.find()) {
        paintId = Integer.parseInt(m.group(1));
      }

      m = itemTypePattern.matcher(itemLink);
      int itemType = 0;
      if (m.find()) {
        itemType = Integer.parseInt(m.group(1));
      }

      int amount = 1;
      final Element amountElement = itemElement.getElementsByClass("rlg-trade-display-item__amount is--rare").first();
      if (amountElement != null) {
        amount = Integer.parseInt(amountElement.text());
      }

      items.add(new PaintedItem(itemId, paintId, itemType == 2, amount));
    }

    return items;
  }
}
