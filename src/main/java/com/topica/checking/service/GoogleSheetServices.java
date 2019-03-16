package com.topica.checking.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import org.springframework.stereotype.Service;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GoogleSheetServices {
//    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
//    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    private static final String SpreadSheet_Id = "1dVi55v_jm3Bot6Mg_rZHjjqFHYsgaHlTPVnX4ccZSYw";
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//
//    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//        .setApplicationName(APPLICATION_NAME)
//        .build();
//
//    public GoogleSheetServices() throws GeneralSecurityException, IOException {
//
//    }
//
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
//        // Load client secrets.
//
//        InputStream in =  TypeReference.class.getResourceAsStream("/json/"+CREDENTIALS_FILE_PATH);
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//            .setAccessType("offline")
//            .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
//
//
////    public String create(String title) throws IOException {
////        Sheets service = this.service;
////        // [START sheets_create]
////        Spreadsheet spreadsheet = new Spreadsheet()
////            .setProperties(new SpreadsheetProperties()
////                .setTitle(title));
////        spreadsheet = service.spreadsheets().create(spreadsheet)
////            .setFields("spreadsheetId")
////            .execute();
////        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
////        // [END sheets_create]
////        return spreadsheet.getSpreadsheetId();
////    }
//
//
//    public void writingData( Map<Integer, Object[]> excelData,String sheetName) throws IOException, GeneralSecurityException {
//
//        final String range = sheetName+"!A1:O";
//
//        Object[] object = new Object[15];
//        object[0] = "STT";
//        object[1] = "Mã";
//        object[2] = "User";
//        object[3] = "Link ảnh đề";
//        object[4] = "Độ sắc nét ảnh đề";
//        object[5] = "Độ Sáng ảnh đề";
//        object[6] = "Loại đề";
//        object[7] = "Điểm kiến thức";
//        object[8] = "Search ra ngay";
//        object[9] = "QA0";
//        object[10] = "QA1";
//        object[12] = "QA2";
//        object[11] = "QA3";
//        object[13] = "QA4";
//        object[14] = "Level search";
//
//        List<List<Object>> valuesWrite = new ArrayList();
//        valuesWrite.add(Arrays.asList(object));
//        for (Object[] x :
//            excelData.values()) {
//            valuesWrite.add(Arrays.asList(x));
//        }
//        List<ValueRange> data = new ArrayList<>();
//        data.add(new ValueRange()
//            .setRange(range)
//            .setValues(valuesWrite));
//// Additional ranges to update ...
//
//        BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
//            .setValueInputOption("USER_ENTERED")
//            .setData(data);
//        BatchUpdateValuesResponse resultWrite =
//            service.spreadsheets().values().batchUpdate(SpreadSheet_Id, body).execute();
//        System.out.printf("%d cells updated.", resultWrite.getTotalUpdatedCells());
//
//
////        List<Request> requests = new ArrayList<>();
////        // Change the spreadsheet's title.
////        requests.add(new Request().setAddSheet(new AddSheetRequest().
////                setProperties(new SheetProperties().
////                    setTitle("test1").setGridProperties(new GridProperties().setColumnCount(10).setRowCount(10)))));
////
////        BatchUpdateSpreadsheetRequest body =
////            new BatchUpdateSpreadsheetRequest().setRequests(requests);
////        BatchUpdateSpreadsheetResponse response =
////            service.spreadsheets().batchUpdate(SpreadSheet_Id, body).execute();
//    }
//
//    public void createSheet(Map<Integer, Object[]> excelData,String sheetName){
//                List<Request> requests = new ArrayList<>();
//        // Change the spreadsheet's title.
//        requests.add(new Request().setAddSheet(new AddSheetRequest().
//                setProperties(new SheetProperties().
//                    setTitle(sheetName).setGridProperties(new GridProperties().setColumnCount(15).setRowCount(5000)))));
//
//        BatchUpdateSpreadsheetRequest body =
//            new BatchUpdateSpreadsheetRequest().setRequests(requests);
//        try {
//            BatchUpdateSpreadsheetResponse response =
//                service.spreadsheets().batchUpdate(SpreadSheet_Id, body).execute();
//            if(response.getSpreadsheetId() != null){
//                writingData(excelData,sheetName);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//    }


}
