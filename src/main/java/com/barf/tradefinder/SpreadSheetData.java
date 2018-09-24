package com.barf.tradefinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barf.tradefinder.domain.Item;
import com.barf.tradefinder.domain.PaintedItem.Color;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SpreadSheetData {
  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart. If modifying
   * these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    // Load client secrets.
    final InputStream in = SpreadSheetData.class.getResourceAsStream(SpreadSheetData.CREDENTIALS_FILE_PATH);
    final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(SpreadSheetData.JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, SpreadSheetData.JSON_FACTORY, clientSecrets, SpreadSheetData.SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(SpreadSheetData.TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  /**
   * Prints the names and majors of students in a sample spreadsheet:
   * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
   */
  public static Map<Item, List<Color>> getSheetData() throws IOException, GeneralSecurityException {
    final Map<Item, List<Color>> topperList = new HashMap<>();

    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1WGt3_fmOSDuwgrP6hxRWRorBjmZrGknBe-_TAjt55-g";
    final String range = "D7:T48";
    final Sheets service = new Sheets.Builder(HTTP_TRANSPORT, SpreadSheetData.JSON_FACTORY, SpreadSheetData.getCredentials(HTTP_TRANSPORT))
        .setApplicationName(SpreadSheetData.APPLICATION_NAME)
        .build();
    final ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();

    int topperIndex = 0;
    final List<List<Object>> values = response.getValues();
    if ((values == null) || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (final List<?> row : values) {
        final List<Color> colors = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
          final String value = (String) row.get(i);
          if (!("N/A".equals(value) || value.isEmpty())) {
            colors.add(Color.alphabeticIndex(i));
          }
        }
        topperList.put(Item.alphabeticTopperIndex(topperIndex), colors);
        topperIndex++;
      }
    }
    return topperList;
  }

}
