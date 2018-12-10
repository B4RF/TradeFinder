package com.barf.tradefinder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.barf.tradefinder.domain.Item;
import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem.Color;
import com.barf.tradefinder.domain.TradeOffer;
import com.barf.tradefinder.domain.UrlLinksButton;

public class GUI extends JFrame {
  private static final long serialVersionUID = 1L;

  static List<TradeOffer> lastRequest = new ArrayList<>();

  static TimeUnit unit = null;

  public GUI() {
    this.setTitle("TradeFinder");
    this.setSize(260, 84);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    this.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;
    c.weighty = 0.5;

    final JButton search = new JButton("Search");
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    this.add(search, c);

    final UrlLinksButton keyUrls = new UrlLinksButton();
    keyUrls.setText("Key offers");
    c.weightx = 0.5;
    c.gridy = 1;
    c.gridwidth = 1;
    this.add(keyUrls, c);

    final UrlLinksButton itemUrls = new UrlLinksButton();
    itemUrls.setText("Item offers");
    c.gridx = 1;
    this.add(itemUrls, c);

    this.setLocationRelativeTo(null);
    this.setVisible(true);

    search.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        keyUrls.removeUrls();
        itemUrls.removeUrls();

        final Set<TradeOffer> keyOffers = new HashSet<>();
        final Set<TradeOffer> itemOffers = new HashSet<>();

        Map<Item, List<Color>> topperList = new HashMap<>();
        List<String> userBlacklist = new ArrayList<>();
        try {
          topperList = SpreadSheetData.getSheetData();
          userBlacklist = SpreadSheetData.getUserBlacklist();
        } catch (IOException | GeneralSecurityException e) {
          JOptionPane.showMessageDialog(null, "Error during spreadsheet connection.");
          e.printStackTrace();
          return;
        }

        Request.hasConnectionIssues = false;
        for (final Item item : Item.values()) {
          if (item.getType().equals(ItemType.TOPPER)) {
            final List<Color> colors = topperList.get(item);

            if (!colors.isEmpty()) {
              final List<TradeOffer> allOffers = Request.getOffersForItem(item.getId(), GUI.unit);

              for (final TradeOffer tradeOffer : allOffers) {
                if (tradeOffer.wantsContainsOneOf(item, colors) && !userBlacklist.contains(tradeOffer.getUser())) {
                  if (tradeOffer.hasContainsKey()) {
                    keyOffers.add(tradeOffer);

                  } else if (tradeOffer.hasContainsItem()) {
                    itemOffers.add(tradeOffer);
                  }
                }
              }
            }
          }
        }

        // remove already known offers
        final List<TradeOffer> tmp = new ArrayList<>();
        tmp.addAll(keyOffers);
        tmp.addAll(itemOffers);
        keyOffers.removeAll(GUI.lastRequest);
        itemOffers.removeAll(GUI.lastRequest);
        if (Request.hasConnectionIssues) {
          GUI.lastRequest.addAll(tmp);
          JOptionPane.showMessageDialog(GUI.this, "RL Garage has connection issues...");
        } else {
          GUI.lastRequest = tmp;
        }
        GUI.unit = null;

        for (final TradeOffer tradeOffer : keyOffers) {
          if (!tradeOffer.isSupressed()) {
            keyUrls.addUrl(tradeOffer.getLink());
          }
        }

        for (final TradeOffer tradeOffer : itemOffers) {
          if (!tradeOffer.isSupressed()) {
            itemUrls.addUrl(tradeOffer.getLink());
          }
        }

        keyUrls.setText("Key offers");
        itemUrls.setText("Item offers");
      }
    });

    final InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "unit");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "unit");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "unit");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "unit");

    this.getRootPane().getActionMap().put("unit", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("m")) {
          GUI.unit = TimeUnit.MINUTES;
          JOptionPane.showMessageDialog(GUI.this, "Offers have been restricted to the last 10 minutes!");
        } else if (e.getActionCommand().equals("h")) {
          GUI.unit = TimeUnit.HOURS;
          JOptionPane.showMessageDialog(GUI.this, "Offers have been restricted to the last hour!");
        } else if (e.getActionCommand().equals("d")) {
          GUI.unit = TimeUnit.DAYS;
          JOptionPane.showMessageDialog(GUI.this, "Offers have been restricted to the last day!");
        } else if (e.getActionCommand().equals("n")) {
          GUI.unit = null;
          JOptionPane.showMessageDialog(GUI.this, "Offers restriction has been removed!");
        }
      }
    });
  }

  @SuppressWarnings("unused")
  public static void main(final String[] args) {

    new GUI();
  }
}
