package edu.rice.rems;/*
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

import archive.PSORunner;
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
    private static String[] names, dates, shifts;



    public static void run(File xlsFile, Integer minShiftsRequired, Integer maxShiftsAllowed) {

        int[][] availabilities = extractShiftAvailability(extractData(xlsFile));

        int[] solution;
        if(useBruteForce){
            solution = Solver.bruteForce(availabilities);
        }
        else{
            solution = Solver.iterativeImprovement(availabilities);
        }

        displaySolution(month, names, dates, shifts, solution);

    }

    private static void displaySolution(String month, String[] names, String[] dates, String[] shifts, int[] solution) {

    }

    private static int[][] extractShiftAvailability(ArrayList<ArrayList<String>> data) {

        month = new String(data.get(2).get(0));

        dates = new String[data.get(3).size()];
        for(int i = 0; i < dates.length; i++)
            dates[i] = data.get(3).get(i);

        shifts = new String[data.get(4).size()];
        for(int i = 0; i < shifts.length; i++)
            shifts[i] = data.get(4).get(i);

        names = new String[data.size()-6];
        int[][] schedule = new int[data.size()-6][data.get(5).size()-1];

        for(int i = 5; i < data.size()-1; i++){
            ArrayList<String> row = data.get(i);
            int studentIndex = i-5;

            names[studentIndex] = row.get(0);
            for(int j = 1; j < row.size(); j++){
                int index = j-1;
                if(row.get(j).contains("OK")){
                    if(row.get(j).contains("(")){
                        schedule[studentIndex][index] = 1;
                    }else{
                        schedule[studentIndex][index] = 2;
                    }
                }
            }
        }
        return schedule;
    }

    /**
     * Extract shift schedule data from excel file
     *
     * @param xlsFile
     * @return data
     */
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
