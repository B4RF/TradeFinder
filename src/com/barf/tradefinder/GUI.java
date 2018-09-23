package com.barf.tradefinder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.barf.tradefinder.domain.Item;
import com.barf.tradefinder.domain.Item.ItemType;
import com.barf.tradefinder.domain.TradeOffer;
import com.barf.tradefinder.domain.UrlTextPane;

public class GUI extends JFrame {
  private static final long serialVersionUID = 1L;

  static JPanel mainPanel = new JPanel();
  static JPanel keyOfferPanel = new JPanel();
  static JPanel itemOfferPanel = new JPanel();

  public GUI() {
    this.setTitle("TradeFinder");
    this.setResizable(false);
    this.setSize(300, 600);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    GUI.mainPanel.setLayout(new BoxLayout(GUI.mainPanel, BoxLayout.Y_AXIS));
    GUI.keyOfferPanel.setLayout(new BoxLayout(GUI.keyOfferPanel, BoxLayout.Y_AXIS));
    GUI.itemOfferPanel.setLayout(new BoxLayout(GUI.itemOfferPanel, BoxLayout.Y_AXIS));

    final JButton search = new JButton("Search");
    this.add(search, BorderLayout.NORTH);

    final JScrollPane scroll = new JScrollPane(GUI.mainPanel);
    this.add(scroll);

    final JTextPane keyText = new JTextPane();
    keyText.setText("Key offers:");
    GUI.mainPanel.add(keyText);
    GUI.mainPanel.add(GUI.keyOfferPanel);

    final JTextPane itemText = new JTextPane();
    itemText.setText("Item offers:");
    GUI.mainPanel.add(itemText);
    GUI.mainPanel.add(GUI.itemOfferPanel);

    this.setLocationRelativeTo(null);
    this.setVisible(true);

    search.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {

        GUI.keyOfferPanel.removeAll();
        GUI.itemOfferPanel.removeAll();

        final Set<TradeOffer> keyOffers = new HashSet<>();
        final Set<TradeOffer> itemOffers = new HashSet<>();

        for (final Item item : Item.getAllItems()) {
          if (item.getType().equals(ItemType.TOPPER)) {
            final Set<TradeOffer> allOffers = Request.getOffersForItem(item.getId());

            for (final TradeOffer tradeOffer : allOffers) {
              if (tradeOffer.containsKey()) {
                keyOffers.add(tradeOffer);

              } else if (tradeOffer.containsItem()) {
                itemOffers.add(tradeOffer);
              }
            }
          }
        }

        for (final TradeOffer tradeOffer : keyOffers) {
          final UrlTextPane textPane = new UrlTextPane();
          textPane.setText(tradeOffer.getLink());
          GUI.keyOfferPanel.add(textPane);
        }

        for (final TradeOffer tradeOffer : itemOffers) {
          final UrlTextPane textPane = new UrlTextPane();
          textPane.setText(tradeOffer.getLink());
          GUI.itemOfferPanel.add(textPane);
        }

        GUI.mainPanel.setPreferredSize(GUI.mainPanel.getPreferredSize());
        GUI.mainPanel.validate();
      }
    });
  }

  @SuppressWarnings("unused")
  public static void main(final String[] args) {

    new GUI();
  }
}
