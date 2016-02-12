package archive;/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Muhammad Saad Shamim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Scheduler {

    private static String month;
    private static ArrayList<String> dates, times, filteredTimes, names;
    private static ArrayList<ArrayList<Integer>> schedule, filteredSchedule;
    private static int minShiftsRequired, maxShiftsAllowed;

    private static int[] timePopularity, originalDatePosition;



    public static void run(File xlsFile, Integer minShifts, Integer maxShifts) {

        minShiftsRequired = minShifts;
        maxShiftsAllowed = maxShifts;

        preprocess(extractData(xlsFile));

        PSORunner pso = new PSORunner(names.size(), filteredTimes.size(), minShiftsRequired, maxShiftsAllowed, filteredSchedule);
        pso.runOptimization();

        /*
        NondominatedPopulation result = new Executor()
                .withProblemClass(archive.Scheduler.ScheduleOptimization.class)
                .withAlgorithm("NSGAII")
                //.withAlgorithm("GDE3")
                .distributeOnAllCores()
                .checkpointEveryIteration()
                .withEpsilon(1)
                .withMaxEvaluations(10000)
                .run();

        //display the results
        System.out.format("Objective1  Objective2%n");

        for (Solution solution : result) {

            for(double val :solution.getObjectives()){
                System.out.print(val+" ");
            }

            System.out.println("");

            for(int i = 0; i < dates.size(); i++)
                System.out.print((int)Math.floor(((RealVariable)solution.getVariable(i)).getValue())+" ");

            System.out.println("");
        }
        */

    }

    private static void preprocess(ArrayList<ArrayList<String>> data) {

        month = new String(data.get(2).get(0));

        dates = new ArrayList<String>();
        dates.addAll(data.get(3));

        times = new ArrayList<String>();
        times.addAll(data.get(4));

        names = new ArrayList<String>();
        schedule = new ArrayList<>();


        timePopularity = new int[times.size()];

        for(int i = 5; i < data.size()-1; i++){
            ArrayList<String> row = data.get(i);
            ArrayList<Integer> studentSchedule = new ArrayList<>();

            names.add(row.get(0));
            for(int j = 1; j < row.size(); j++){
                if(row.get(j).contains("OK")){
                    if(row.get(j).contains("(")){
                        studentSchedule.add(20);
                    }else{
                        studentSchedule.add(0);
                    }
                    timePopularity[j-1]++;
                }
                else {
                    studentSchedule.add(45);
                }
            }
            schedule.add(studentSchedule);
        }

        int numZeros = 0;
        for(int val : timePopularity)
            if(val == 0)
                numZeros++;

        filteredTimes = new ArrayList<>();
        originalDatePosition = new int[times.size()-numZeros];

        int j2 = 0;
        for(int i = 0; i < times.size(); i++){
            if(timePopularity[i] > 0){
                filteredTimes.add(times.get(i));
                originalDatePosition[j2] = i;
                j2++;
            }
        }

        filteredSchedule = new ArrayList<>();
        for(int i = 5; i < data.size()-1; i++){
            ArrayList<String> row = data.get(i);
            ArrayList<Integer> studentSchedule = new ArrayList<>();

            for(int k : originalDatePosition){
                if(row.get(k).contains("OK")){
                    if(row.get(k).contains("(")){
                        studentSchedule.add(20);
                    }else{
                        studentSchedule.add(0);
                    }
                }
                else {
                    studentSchedule.add(45);
                }
            }
            filteredSchedule.add(studentSchedule);
        }

    }

    private static ArrayList<ArrayList<String>> extractData(File xlsFile){
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(xlsFile);

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                ArrayList<String> newRow = new ArrayList<String>();

                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    newRow.add(cell.getStringCellValue());
                }
                data.add(newRow);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }


}
