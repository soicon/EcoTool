package com.topica.checking.web.rest.util;

import com.topica.checking.service.*;
import com.topica.checking.service.dto.*;
import com.topica.checking.web.rest.SourceResource;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

public class FetchingAPI {

    private final Logger log = LoggerFactory.getLogger(SourceResource.class);

    @Autowired
    private  FileStorageServices fileStorageServices;

    public FetchingAPI(FileStorageServices fileStorageServices) {
        this.fileStorageServices = fileStorageServices;
    }

    public FetchingAPI() {
    }

    public JSONObject callApi(String base64, String API_URL) {
        try {
            final String POST_PARAMS = "{\"img\":"+"\""+base64+"\""+"}";
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
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String writeExcel( Map<Integer, Object[]> data,String filename,FileStorageServices fileStorageServices){
        try {
            XSSFWorkbook workbookWrite = new XSSFWorkbook();
            XSSFSheet sheet = workbookWrite.createSheet("TestECOKID");
            //data.put(1, new Object[]{"Mã", "Link", "Link Output", "Ra chính xác"});
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

    public String doJob(Optional<DataVersionDTO> dataVersionDTO,
                       File fileData, SourceService sourceService, String API_URL, String url, QuestionService questionService,
                      AnswerService answerService, RunnerLogService runnerLogService, List<RunnerLogDTO> runnerLogDTOList,
                      String filename){
        RunnerLogDTO runnerLogDTO = new RunnerLogDTO();

        runnerLogDTO.setDataversionId(dataVersionDTO.get().getId());
        runnerLogDTO.setDataversionVersion(dataVersionDTO.get().getVersion());



        try {

            Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
            FileInputStream excelFile = new FileInputStream(fileData);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Row row = datatypeSheet.getRow(1);

            int rowCount = 0;

            Iterator<Cell> cellIteratorIndex = row.iterator();
            while (cellIteratorIndex.hasNext()){
                Cell indexCell = cellIteratorIndex.next();
                log.info(indexCell.getCellTypeEnum().name());
                if(indexCell.getCellTypeEnum() == CellType.BLANK && rowCount != 1)
                    break;
                if(indexCell.getCellTypeEnum() == CellType.NUMERIC ){
                    if((int)indexCell.getNumericCellValue() == -1)
                        break;
                }
                if(indexCell.getCellTypeEnum() == CellType.STRING && rowCount == 1 ){
                    rowCount++;
                }

                rowCount++;
            }
            log.info("column "+rowCount);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int index = 1;
            if(iterator.hasNext()){
                iterator.next();
            }
            while (iterator.hasNext()) {
                Object[] object = new Object[rowCount+2];
                try {
                    Row currentRow = iterator.next();
                    Iterator<Cell> cellIterator = currentRow.iterator();
                    SourceDTO sourceDTO = new SourceDTO();
                    sourceDTO.setDevice_id("alo");
                    sourceDTO.setNeed_re_answer(0);
                    sourceDTO.setStatus(1);
                    sourceDTO.setType(0);
                    String urlImage = "";
                    int questionId = -1;
                    int indexObject = 0;
                    while (cellIterator.hasNext()) {
                        Cell currentCell = cellIterator.next();
                        if(indexObject == 3)
                            break;

                        if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            if(currentCell.getNumericCellValue() == -1)
                                break;
                                questionId = (int) currentCell.getNumericCellValue();
                                if (indexObject == 0) {
                                    log.info(questionId+"");
                                    sourceDTO.setQuestion_id((int) currentCell.getNumericCellValue());
                                }
                                object[indexObject] = questionId;

                        }
                        if (currentCell.getCellTypeEnum() == CellType.STRING  ) {
                            urlImage = currentCell.getStringCellValue().trim();
                            sourceDTO.setPath(urlImage);
                            object[indexObject] = urlImage;

                        }
                        if (currentCell.getCellTypeEnum() == CellType.BLANK) {
                            if (indexObject == 3) {
                                break;
                            } else {
                                object[indexObject] = "";
                            }
                        }
                        indexObject++;
                    }
                    if(object[1] != null){
                        if(!object[1].equals("") && object[1].toString().contains("http")){
                            object[2] = object[1];
                            object[1] = "";
                        }
                    }
                    SourceDTO res = sourceService.save(sourceDTO);
                    runnerLogDTO.setSourceId(res.getId());
                    runnerLogDTO.setSourcePath(res.getPath());
                    log.info(urlImage);
                    if(urlImage.equals("") || urlImage == null){
                        break;
                    }
                    byte[] imageBytes = IOUtils.toByteArray(new URL(urlImage));
                    String base64 = Base64.getEncoder().encodeToString(imageBytes);

                    JSONObject response = callApi(base64,API_URL);
                    if (response != null) {
                        JSONArray array = response.getJSONArray("result");
                        if (array.length() > 0) {
                            object[rowCount+1] = 1;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject entry = array.getJSONObject(i);
                                QuestionDTO questionDTO = new QuestionDTO();
                                questionDTO.setQuestion_text((String) entry.get("question"));
                                questionDTO.setSourceId(res.getId());
                                object[rowCount]= url+res.getId();
                                questionDTO.setVisible(1);
                                QuestionDTO quesRes = questionService.save(questionDTO);
                                runnerLogDTO.setQuestionId(quesRes.getId());
                                runnerLogDTO.setQuestionQuestion_text(quesRes.getQuestion_text());
                                AnswerDTO answerDTO = new AnswerDTO();
                                answerDTO.setAnswer_text(entry.get("answer").toString().replace("format1//",""));
                                answerDTO.setQuestionId(quesRes.getId());
                                answerDTO.setReviewer_id(1);
                                answerDTO.setStatus(1);
                                answerDTO.setUser_id(1);
                                AnswerDTO ansRes = answerService.save(answerDTO);
                                runnerLogDTO.setAnswerAnswer_text(ansRes.getAnswer_text());
                                runnerLogDTO.setAnswerId(ansRes.getId());

                            }
                        } else {
                            object[rowCount+1] = 0;
                        }

                    }else{
                        object[rowCount+1] = -1;
                    }
                    //log.info(object[0]== null?"":object[0].toString()+" "+object[2]== null?"":object[2].toString()+" "+object[3]== null?"":object[3].toString()+" "+object[4]== null?"":object[4].toString());
                    data.put(index, object);
                    runnerLogDTOList.add(runnerLogDTO);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                index++;
            }
            runnerLogService.saveAll(runnerLogDTOList);
            String fileResult = writeExcel(data,filename,fileStorageServices);
            return fileResult;


        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }
}
