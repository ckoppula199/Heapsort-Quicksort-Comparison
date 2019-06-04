package chak.koppula.ucl;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

public class Main {

    private static final String EXCEL_FILE_LOCATION = "Comparisons.xls"; //creates spreadsheet if not already there. Overwrites spreadsheet if it already exists.
    private static WritableWorkbook spreadsheet = null;
    private static WritableSheet excelSheet = null;

    private long[] inputSize = new long[10];
    private float[] quickTimes = new float[10];
    private float[] heapTimes = new float[10];
    private long[] quickCompSwaps = new long[10];
    private long[] heapCompSwaps = new long[10];

    private void writeHeaders(WritableSheet excelSheet, int i) throws WriteException {
        Label label = new Label(i * 8, 0, "Input Size");
        excelSheet.addCell(label);
        excelSheet.setColumnView(1 + i * 8, 14);
        label = new Label(1 + i * 8, 0, "Quicksort Time");
        excelSheet.addCell(label);
        excelSheet.setColumnView(2 + i * 8, 13);
        label = new Label(2 + i * 8, 0, "Heapsort Time");
        excelSheet.addCell(label);
        label = new Label(3 + i * 8, 0, "Input Size");
        excelSheet.addCell(label);
        excelSheet.setColumnView(4 + i * 8, 22);
        label = new Label(4 + i * 8, 0, "Quicksort Comps+Swaps");
        excelSheet.addCell(label);
        excelSheet.setColumnView(5 + i * 8, 21);
        label = new Label(5 + i * 8, 0, "Heapsort Comps+Swaps");
        excelSheet.addCell(label);
    }

    private void writeIntColumns(WritableSheet excelSheet, long[] array, int col) throws WriteException {
        int row = 0;
        for(long item : array) {
            Number number = new Number(col, row + 1, item);
            excelSheet.addCell(number);
            row++;
        }
    }

    private void writeFloatColumns(WritableSheet excelSheet, float[] array, int col) throws WriteException {
        int row = 0;
        for (float item : array) {
            Number number = new Number(col, row + 1, item);
            excelSheet.addCell(number);
            row++;
        }
    }

    private void writeData(WritableSheet excelSheet, float[] quicktimes, float[] heaptimes, int i) throws WriteException{
        writeHeaders(excelSheet, i);
        writeIntColumns(excelSheet, inputSize,i * 8);
        writeFloatColumns(excelSheet, quicktimes, 1 + i * 8);
        writeFloatColumns(excelSheet, heaptimes, 2 + i * 8);
        writeIntColumns(excelSheet, inputSize,3 + i * 8);
        writeIntColumns(excelSheet, quickCompSwaps, 4 + i * 8);
        writeIntColumns(excelSheet, heapCompSwaps,5 + i * 8);
    }

    private void writeAverageTimes(WritableSheet excelSheet) throws WriteException{
        for(int j = 0; j < inputSize.length; j++) {
            Formula avg = new Formula(81, 1 + j, "AVERAGE(BV" + (2 + j) + ",BN" + (2 + j) + ",BF" + (2 + j) + ",AX" + (2 + j) + ",AP" + (2 + j) + ",AH" + (2 + j) + ",Z" + (2 + j) + ",R" + (2 + j) + ",J" + (2 + j) + ",B" + (2 + j) + ")");
            excelSheet.addCell(avg);
        }
        for(int j = 0; j < inputSize.length; j++) {
            Formula avg = new Formula(82, 1 + j, "AVERAGE(BW" + (2 + j) + ",BO" + (2 + j) + ",BG" + (2 + j) + ",AY" + (2 + j) + ",AQ" + (2 + j) + ",AI" + (2 + j) + ",AA" + (2 + j) + ",S" + (2 + j) + ",K" + (2 + j) + ",C" + (2 + j) + ")");
            excelSheet.addCell(avg);
        }
    }

    private void writeAverageComparisons(WritableSheet excelSheet) throws WriteException {
        for(int j = 0; j < inputSize.length; j++) {
            Formula avg = new Formula(84, 1 + j, "AVERAGE(BY" + (2 + j) + ",BQ" + (2 + j) + ",BI" + (2 + j) + ",BA" + (2 + j) + ",AS" + (2 + j) + ",AK" + (2 + j) + ",AC" + (2 + j) + ",U" + (2 + j) + ",M" + (2 + j) + ",E" + (2 + j) + ")");
            excelSheet.addCell(avg);
        }
        for(int j = 0; j < inputSize.length; j++) {
            Formula avg = new Formula(85, 1 + j, "AVERAGE(BZ" + (2 + j) + ",BR" + (2 + j) + ",BJ" + (2 + j) + ",BB" + (2 + j) + ",AT" + (2 + j) + ",AL" + (2 + j) + ",AD" + (2 + j) + ",V" + (2 + j) + ",N" + (2 + j) + ",F" + (2 + j) + ")");
            excelSheet.addCell(avg);
        }
    }

    private void writeAverages(WritableSheet excelSheet, int i) throws WriteException{
        writeHeaders(excelSheet, i);
        writeIntColumns(excelSheet, inputSize,i * 8);
        writeAverageTimes(excelSheet);
        writeIntColumns(excelSheet, inputSize,3 + i * 8);
        writeAverageComparisons(excelSheet);
    }

    private WritableWorkbook setupWorkbook() throws IOException {
        if (spreadsheet == null) {
            spreadsheet = Workbook.createWorkbook(new File(EXCEL_FILE_LOCATION));
        }
        return spreadsheet;
    }

    private WritableSheet setupSheet1() {
        if (excelSheet == null) {
            excelSheet = spreadsheet.createSheet("Algorithms", 0);
        }
        return excelSheet;
    }

    private void writeToExcel(int i) throws IOException, WriteException {
        WritableWorkbook spreadsheet = setupWorkbook();
        WritableSheet excelSheet = setupSheet1();

        writeData(excelSheet, quickTimes, heapTimes, i);

        if (i == 9) {
            writeAverages(excelSheet, i + 1);
            spreadsheet.write();
            spreadsheet.close();
        }
    }


    //-------------Code below handles array generation and sorting as well as storing data in arrays----------

    //resets arrays and counters for each run
    private void reset(Quicksort qs, HeapSort hs) {
        qs.array.clear();
        hs.heapArray.clear();
        qs.quicksortSwaps = 0;
        qs.quicksortComps = 0;
        hs.heapsortSwaps = 0;
        hs.heapsortComps = 0;
    }

    private void quicksort(Quicksort qs, int i) {
        int start1 = (int) System.currentTimeMillis();
        qs.quicksort(0, qs.array.size() - 1);
        int end1 = (int) System.currentTimeMillis();
        float quickRunTime = ((float) (end1 - start1)) / 1000; //works out time taken for quicksort in seconds
        System.out.println("Quicksort took: " + quickRunTime + " seconds");
        quickTimes[i] = quickRunTime; //adds data to array in position corresponding to input size array
        System.out.println("Quicksort had " + qs.quicksortComps + " comparisons");
        System.out.println("Quicksort had " + qs.quicksortSwaps + " swaps\n");
        quickCompSwaps[i] = qs.quicksortComps + qs.quicksortSwaps; //adds data to array in position corresponding to input size array
    }

    private void heapsort(HeapSort hs, int i) {
        int start2 = (int) System.currentTimeMillis();
        hs.heapSort();
        int end2 = (int) System.currentTimeMillis();
        float heapRunTime = ((float) (end2 - start2)) / 1000; //works out time taken for heapsort in seconds
        System.out.println("\nHeapsort took: " + heapRunTime + " seconds");
        heapTimes[i] = heapRunTime; //adds data to array in position corresponding to input size array
        System.out.println("Heapsort had " + hs.heapsortComps + " comparisons");
        System.out.println("Heapsort had " + hs.heapsortSwaps + " swaps\n");
        heapCompSwaps[i] = hs.heapsortSwaps + hs.heapsortComps; //adds data to array in position corresponding to input size array
    }

    private void addValues(Random random, Quicksort qs, HeapSort hs, int numOfValues) {
        for (int j = 0; j < numOfValues; j++) {
            //both arrays given same value
            int value = random.nextInt();
            qs.array.add(value);
            hs.heapArray.add(value);
        }
    }

    private void runSorts(Random random, Quicksort qs, HeapSort hs, int numOfValues) {
        //does 10 runs each on an increasing input size
        for (int i = 0; i < 10; i++) {
            reset(qs, hs);
            numOfValues += 100;      //increment of 100,000. Can be changed for varying results
            System.out.println("Array Size: " + numOfValues);
            inputSize[i] = numOfValues; //adds size to inputSize array in ascending order
            addValues(random, qs, hs, numOfValues);
            quicksort(qs, i);
            heapsort(hs, i);
        }
    }

    private void go() throws IOException, WriteException {
        Quicksort qs = new Quicksort();
        HeapSort hs = new HeapSort();
        Random random = new Random();

        //repeats entire experiment 10 (so each input size will have 10 runs)
        int numOfValues = 0;
        for (int a = 0; a < 10; a++) {
            runSorts(random, qs, hs, numOfValues);
            writeToExcel(a);
        }
    }

    public static void main(String[] args) {
        Main run = new Main();
        try {
            run.go();
        } catch (IOException | WriteException ioe) {
            ioe.printStackTrace();
        }
    }
}
