package com.barf.tradefinder.domain;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class UrlTextPane extends JTextPane {

  private static final long serialVersionUID = 1L;

  public UrlTextPane() {
    this.setEditable(false);
    this.addHyperlinkListener(new UrlHyperlinkListener());
    this.setContentType("text/html");
  }

  private class UrlHyperlinkListener implements HyperlinkListener {
    @Override
    public void hyperlinkUpdate(final HyperlinkEvent event) {
      if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        try {
          Desktop.getDesktop().browse(event.getURL().toURI());
        } catch (final IOException e) {
          throw new RuntimeException("Can't open URL", e);
        } catch (final URISyntaxException e) {
          throw new RuntimeException("Can't open URL", e);
        }
      }
    }
  }

  @Override
  /**
   * Set the text, first translate it into HTML:
   */
  public void setText(final String input) {

    final String html = "<html><body style=\"font-size: 8.5px;font-family: Tahoma, sans-serif\">" +
        "<a href=\"" + input + "\">" + input + "</a></body></html>";

    super.setText(html);
  }
}