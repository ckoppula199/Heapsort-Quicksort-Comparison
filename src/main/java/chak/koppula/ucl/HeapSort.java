package chak.koppula.ucl;

import java.util.ArrayList;

public class HeapSort {

    // Array that holds the values representing a heap.
    public ArrayList<Integer> heapArray = new ArrayList<>();
    public long heapsortSwaps = 0;
    public long heapsortComps = 0;

    //method that returns the smallest of two integers
    private boolean less(int a, int b) {
        heapsortComps++;
        return a < b;
    }

    //method that swaps two items
    private void swap(int a, int b) {
        heapsortSwaps++;
        int temp1 = heapArray.get(a);
        heapArray.set(a, heapArray.get(b));
        heapArray.set(b, temp1);
    }


    /*
    method takes two parameters, an index of the heap array and length, the length of the array to apply the operation
    to. Length is needed as in method sortdown, the end of the array represents the sorted list and so the length we need
    to search through and sort varies as the sort progresses. method checks if the left child of index is in the array, then
    checks if left child is less than right child. if index is greater than its greatest child the loop breaks. else it
    swaps places with its greatest child and index is set to the child and this repeats.
     */
    private void sink(int index, int length) {

        while (index * 2 + 1 <= length) {

            int j = 2 * index + 1;

            if (j < length && less(heapArray.get(j), heapArray.get(j + 1))) {
                j++;
            }

            if (!less(heapArray.get(index), heapArray.get(j))) {
                break;
            }

            swap(index, j);
            index = j;
        }
    }

    /*
    method that given an array of random numbers will create an array representation of a heap. starts halfway point of the
    heap and calls the sink methods on all points from the middle of the heap to the top of the heap.
     */
    private void makeHeap() {
        int size = heapArray.size() - 1;
        for (int i = (size / 2) -1; i >= 0; i--) {
            sink(i, size);
        }
    }

    /* swaps the head (highest value) with the last value. then decrements the size of the array and calls sink on the now
    head value and the size of the array -1. repeats until size = 0
     */
    private void sortdown() {
        int size = heapArray.size() - 1;
        while (size  > 0) {
            swap(0, size);
            size--;
            sink(0, size);
        }
    }

    public void heapSort() {
        makeHeap();
        sortdown();
    }
}
