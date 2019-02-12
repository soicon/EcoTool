package com.topica.checking.web.rest.util;

import com.topica.checking.service.FileStorageServices;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FetchingAPI {


    public JSONObject callApi(String base64, String API_URL) {
        try {
            final String POST_PARAMS = "{\"img\":"+"\""+base64+"\""+"}";
            System.out.println(POST_PARAMS);
            URL obj = new URL(API_URL);
            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            System.out.println("POST Response Code :  " + responseCode);
            System.out.println("POST Response Message : " + postConnection.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
                String jsonText = readAll(in);
                JSONObject json = new JSONObject(jsonText);
                in.close();
                // print resul
                return json;
            } else {
                System.out.println("POST NOT WORKED");
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String writeExcel( Map<Integer, Object[]> data,String filename,FileStorageServices fileStorageServices){
        try {
            XSSFWorkbook workbookWrite = new XSSFWorkbook();
            XSSFSheet sheet = workbookWrite.createSheet("TestECOKID");
            data.put(1, new Object[]{"Mã", "Link", "Link Output", "Ra chính xác"});
            // Iterate over data and write to sheet
            Set<Integer> keyset = data.keySet();
            int rownum = 0;
            for (int i = 0; i < keyset.size(); i++) {
                // this creates a new row in the sheet
                Row row = sheet.createRow(rownum++);
                Object[] objArr = data.get(keyset.toArray()[i]);
                int cellnum = 0;
                for (Object obj : objArr) {
                    // this line creates a cell in the next column of that row
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                }
            }

            String path = fileStorageServices.storeFileResult(new File("result-"+filename));
            System.out.println("Writting successfully on disk.");
            FileOutputStream outputStream = new FileOutputStream(path);
            workbookWrite.write(outputStream);
            workbookWrite.close();
            return "result-"+filename;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
