package com.barf.tradefinder;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import com.barf.tradefinder.domain.Item;
import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.PaintedItem.Color;
import com.barf.tradefinder.domain.TradeOffer;
import com.barf.tradefinder.domain.UrlTextPane;

public class GUI extends JFrame {
  private static final long serialVersionUID = 1L;

  static JPanel mainPanel = new JPanel();
  static JPanel keyOfferPanel = new JPanel();
  static JPanel itemOfferPanel = new JPanel();

  static List<TradeOffer> lastRequest = new ArrayList<>();

  static TimeUnit unit = null;

  public GUI() {
    this.setTitle("TradeFinder");
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    GUI.mainPanel.setLayout(new BoxLayout(GUI.mainPanel, BoxLayout.Y_AXIS));
    GUI.keyOfferPanel.setLayout(new BoxLayout(GUI.keyOfferPanel, BoxLayout.Y_AXIS));
    GUI.itemOfferPanel.setLayout(new BoxLayout(GUI.itemOfferPanel, BoxLayout.Y_AXIS));

    final JButton search = new JButton("Search");
    search.setPreferredSize(new Dimension(260, 26));
    this.add(search, BorderLayout.NORTH);

    final JScrollPane scroll = new JScrollPane(GUI.mainPanel);
    this.add(scroll, BorderLayout.CENTER);

    final JTextPane keyText = new JTextPane();
    keyText.setText("Key offers:");
    keyText.setEditable(false);
    GUI.mainPanel.add(keyText);
    GUI.mainPanel.add(GUI.keyOfferPanel);

    final JTextPane itemText = new JTextPane();
    itemText.setText("Item offers:");
    itemText.setEditable(false);
    GUI.mainPanel.add(itemText);
    GUI.mainPanel.add(GUI.itemOfferPanel);

    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.pack();

    search.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {

        GUI.keyOfferPanel.removeAll();
        GUI.itemOfferPanel.removeAll();

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
            final UrlTextPane textPane = new UrlTextPane();
            textPane.setText(tradeOffer.getLink());
            GUI.keyOfferPanel.add(textPane);
          }
        }

        for (final TradeOffer tradeOffer : itemOffers) {
          if (!tradeOffer.isSupressed()) {
            final UrlTextPane textPane = new UrlTextPane();
            textPane.setText(tradeOffer.getLink());
            GUI.itemOfferPanel.add(textPane);
          }
        }

        // calculate maximum height
        scroll.setPreferredSize(null);
        GUI.this.pack();
        final int preferredWidth = Math.min(260, scroll.getWidth());
        final int preferredHeight = Math.min(600, scroll.getHeight());
        scroll.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        GUI.this.pack();
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
