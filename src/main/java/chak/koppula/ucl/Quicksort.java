package chak.koppula.ucl;

import java.util.ArrayList;

public class Quicksort {

    public long quicksortComps = 0;
    public long quicksortSwaps = 0;
    public ArrayList<Integer> array = new ArrayList<>();

    //swapping procedure
    private void swap(int a, int b) {
        quicksortSwaps++;
        int temp = array.get(a);
        array.set(a, array.get(b));
        array.set(b, temp);
    }

    //uses number at end of the array as the partition key each time.
    private int partition(int start, int end) {
        int key = array.get(end);
        int i = start - 1;

        for(int index = start; index < end; index++) {
            quicksortComps++;
            if (array.get(index) <= key) {
                i++;
                swap(index, i);
            }
        }

        swap(i + 1, end);
        return i + 1;
    }

    public void quicksort(int start, int end) {
        if (start < end) {
            int partition = partition(start, end);
            quicksort(start, partition - 1);
            quicksort(partition + 1, end);
        }
    }
}
