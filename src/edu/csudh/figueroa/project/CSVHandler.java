package edu.csudh.figueroa.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVHandler {

    private boolean headerHandled;
    private int headerTreatment; // 1 = use header, 2 = has header but don't use, 3 = no header
    private ArrayList<String> headerArr = new ArrayList<String>();
    private ArrayList<String> rowDataArr = new ArrayList<String>();
    private char delimiter;
    private String fileName;
    private Scanner scanner;

    public CSVHandler() {
    }

    // CSV without header data
    public CSVHandler(String fileName, char delimiter, int headerTreatment) throws FileNotFoundException {
        headerGrabber(headerTreatment);
        this.fileName = fileName;
        this.delimiter = delimiter;
        importData();
        collectData();
    }

    private void importData() throws FileNotFoundException {
        File file = new File(this.fileName);
        if(!file.exists()) {
            System.out.printf("\nERROR: The file [%s] does not exist.\nExiting Application."
                    , this.fileName);
            System.exit(0);
        }
        else {
            this.scanner = new Scanner(file);
        }
    }

    private void collectData() {
        while(scanner.hasNext()) {
            rowParser();
            this.headerHandled = true;
        }
    }

    private void headerGrabber(int headerTreatment) {
        if(headerTreatment == 1 || headerTreatment == 2) {
            this.headerHandled = false;
        }
        else {
            this.headerHandled = true;
        }
    }

    private void rowParser() {
        int beginString = 0; // inclusive
        String unparsedRow = scanner.nextLine();
        int rowLength = unparsedRow.length();
        for(int count = 0; count < rowLength; count++) {
            if(unparsedRow.charAt(count) == this.delimiter) {
                if(this.headerHandled == false) {
                    headerArr.add(unparsedRow.substring(beginString, count));
                    beginString = count + 1;
                }
                else {
                    rowDataArr.add(unparsedRow.substring(beginString, count));
                    beginString = count + 1;
                }
            }
            if (count+1 == rowLength && this.headerHandled) {
                rowDataArr.add(unparsedRow.substring(beginString, count+1));
                beginString = count + 1;
            }
            else if(count+1 == rowLength && !this.headerHandled){
                headerArr.add(unparsedRow.substring(beginString, count+1));
                beginString = count + 1;
            }
        }

    }

    public ArrayList<String> getHeader() {
        return headerArr;
    }

    public ArrayList<String> getData() {
        return rowDataArr;
    }

    @Override
    public String toString() {
        return rowDataArr.toString();
    }
}
