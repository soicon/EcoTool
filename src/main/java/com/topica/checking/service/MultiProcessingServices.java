package com.topica.checking.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.topica.checking.service.util.CallApiRunner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class MultiProcessingServices {

    public static void main(String[] args) {
        try
        {


            FileInputStream file = new FileInputStream(new File("uploads/Input_20.1_60dep_40xau.xlsx"));
            HashMap<Integer, Object[]> mp= new HashMap<>();
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            Stopwatch stopwatch = Stopwatch.createStarted();
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int index = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                if(row.getLastCellNum() == -1)
                    break;
                Iterator<Cell> cellIterator = row.cellIterator();
                Object[] input = new Object[5];
                int i=0;
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellTypeEnum())
                    {
                        case NUMERIC:
                            input[i] = cell.getNumericCellValue();
                            i++;
                            break;
                        case STRING:
                            input[i] = cell.getStringCellValue();
                            i++;
                            break;
                        case BLANK:
                            input[i] = "";
                            i++;
                            break;
                        default:
                            break;

                    }
                }

                mp.put(index, input);
                index++;
            }
            List<Object[]> dataList=  new ArrayList<>();
            for (Object[] d :  Iterables.skip(mp.values(), 1)) {
                dataList.add(d);
            }
            file.close();

            int numberOfTasks = dataList.size();
            List<Object[]> resultSet=  new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Callable<Object>> todo = new ArrayList<>();

            try {
                for (int i = 0; i < numberOfTasks; i++) {
                    //executor.execute(new CallApiRunner(dataList.get(i), resultSet));
                }
                List<Future<Object>> answers = executor.invokeAll(todo);
            } catch (Exception err) {
                err.printStackTrace();
            }finally {

            }
            executor.shutdown();


            Collections.sort(resultSet, Comparator.comparingDouble(id -> {
                double indexTemp = dataList.indexOf(id);
                return indexTemp == -1? Integer.MAX_VALUE:indexTemp;
            }));

            stopwatch.stop(); // optional
            System.out.println("Time elapsed for myCall() is "+ stopwatch.elapsed(MILLISECONDS));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
