package com.barf.tradefinder.domain;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class UrlLinksButton extends JButton {

  private static final long serialVersionUID = 6501853574972261497L;
  List<String> urls = new ArrayList<>();

  public UrlLinksButton() {
    this.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent arg0) {
        for (final String url : UrlLinksButton.this.urls) {
          try {
            Desktop.getDesktop().browse(new URI(url));
          } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  public void addUrl(final String url) {
    this.urls.add(url);
  }

  public void removeUrls() {
    this.urls.clear();
  }

  @Override
  public void setText(final String text) {
    super.setText(text + " (" + this.urls.size() + ")");
  }
}