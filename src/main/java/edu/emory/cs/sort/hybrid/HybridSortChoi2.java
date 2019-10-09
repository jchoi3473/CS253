package edu.emory.cs.sort.hybrid;
import edu.emory.cs.sort.AbstractSort;
import edu.emory.cs.sort.comparison.InsertionSort;
import edu.emory.cs.sort.comparison.ShellSortKnuth;
import edu.emory.cs.sort.divide_conquer.QuickSort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;


public class HybridSortChoi2<T extends Comparable<T>> extends AbstractSort<T> implements HybridSort<T> {
    private AbstractSort<T> engine;
    private AbstractSort<T> engine1;
    private AbstractSort<T> engine3;
    private T[] temp;

    public HybridSortChoi2() {
        super(Comparator.naturalOrder());
        engine = new ShellSortKnuth<>(); //shell sort for most sorted
        engine1 = new QuickSort<>(); //quick sort for random cases
        engine3 = new InsertionSort<>(); //insertion sort for all sorted
    }
    @Override
    @SuppressWarnings("unchecked")
    public T[] sort(T[][] input) {
        int[] count;
        count = new int[input.length+1]; //index counter
        count[0]=0; //declare 0, begin index for the first merge
        //sorting individual arrays
        for(int row = 0;row<input.length;row++){//where counters are kept and utilized to sort individual arrays
            int countU=0;
            int countD=0;
            if(input[row].length>20) {  //if array is larger than 20, compare elements to see whether ascending or descending
                for (int col = 0; col < 20; col++) {
                    if (input[row][col].compareTo(input[row][col + 1]) < 0) {
                        countU++;
                    } else if (input[row][col].compareTo(input[row][col + 1]) > 0) {
                        countD++;
                    }
                }
                if(countU==20) engine3.sort(input[row]); //if ascending 20 times in a row, it can be predicted that its sorted
                else if(countD==20)engine3.sort(reverseArray(input[row])); //if descending 20 times in a row, it can be predicted that its sorted
                else if(countU>15) engine.sort(input[row]); //if more than 15 is ascending, it can be predicted that it is somewhat sorted
                else if(countD>15) engine.sort(reverseArray(input[row])); //if more than 15 is descending, it can be predicted that it is somewhat sorted
                else engine1.sort(input[row]); //else, its considered random
            }
            else{
                engine3.sort(input[row]); //if less than 20, because size is small, insertion sort is used
            }
            count[row+1]=input[row].length; //keep size of each row for future reference
        }
        int size = Arrays.stream(input).mapToInt(t -> t.length).sum();
        T[] output = (T[])Array.newInstance(input[0][0].getClass(), size);
        int beginIndex = 0;
        for (T[] t : input) {
            System.arraycopy(t, 0, output, beginIndex, t.length);
            beginIndex += t.length;
        }
        for(int i=0;i<count.length-1;i++){
            count[i+1]=count[i]+count[i+1];
        }
        merge(output,count);
        return output;
    }
    //reverse array method
    private T[] reverseArray(T[] array){
        T[] result = Arrays.copyOf(array, array.length);
        for(int i=0;i<array.length;i++){
            result[i]=array[array.length-1-i];
        }
        return result;
    }
    //main merge function to merge individual rows
    private void merge(T[] array, int[] count) {
        if (count.length > 2) { //base case for recursion
            int[] temp = new int[(count.length / 2) + 1]; //create temp for the next merge
            temp[0] = 0; //keep 0 in the first spot so that it can be a begin index
            for (int i = 0; i < count.length - 2; i += 2) { //call for merge method from merge sort
                merge(array, count[i], count[i + 1], count[i + 2]);
            }
            for (int j = 1; j < temp.length - 1; j++) { //updated temp array with new index for next merge
                temp[j] = count[j * 2];
            }
            temp[temp.length - 1] = count[count.length - 1]; //manually putting the last index
            //this needs to be called seperately because number of elements in count array
            //can either be odd(even number of arrays) or even(odd number of arrays).
            count = new int[temp.length]; //new count array for next merge
            for (int i = 0; i < temp.length; i++) {
                count[i] = temp[i];
            }
            merge(array, count);
        }
    }
    //Same Merge and copy methods from merge sort
    private void merge(T[] array, int beginIndex,int middleIndex, int endIndex) {
        int fst = beginIndex, snd = middleIndex;
        copy(array, beginIndex, endIndex);
        for (int k = beginIndex; k < endIndex; k++) {
            if (fst >= middleIndex)                    // no key left in the 1st half
                assign(array, k, temp[snd++]);
            else if (snd >= endIndex)                  // no key left in the 2nd half
                assign(array, k, temp[fst++]);
            else if (compareTo(temp, fst, snd) < 0)    // 1st key < 2nd key
                assign(array, k, temp[fst++]);
            else
                assign(array, k, temp[snd++]);
        }
    }

    private void copy(T[] array, int beginIndex, int endIndex) {
        int N = array.length;
        if (temp == null || temp.length < N)
            temp = Arrays.copyOf(array, N);
        else {
            N = endIndex - beginIndex;
            System.arraycopy(array, beginIndex, temp, beginIndex, N);
        }
        assignments += N;
    }
    @Override
    public void sort(T[] array, int beginIndex, int endIndex) {

    }
/* testing case
    private void printArray(T[] x){
        for(int i=0;i<x.length;i++) {
            System.out.print(x[i]+"\t");
        }
    }

    public static void main(String[] args) {
        //Integer[][] x = new Integer[][]{{1,2,3},{1,2,5},{1,4,5},{5,4,3},{5,4,3},{5,4,3},{5,4,3},{5,4,1},{5,4,1},{5,4,1},{5,4,1},{5,4,1}};
        Integer[][] x = new Integer[][]{{1,2,7},{5,4,3},{3,4,5},{1,5,6},{18,11,13},{13,17,5},{12,10,3},{12,10,3},
                {12,10,3},{1,2,7},{5,4,3},{3,4,5},{1,5,6},{18,11,13},{13,17,5},
                {12,10,3},{12,10,3},{1,2,7},{5,4,3},{3,4,5},{1,5,6},{18,11,13},{13,17,5},{12,10,3},{1,5,6},{4,3,13}
                ,{12,10,3},{12,10,3},{1,2,7},{5,4,3}};
        //Integer[][] x = new Integer[][]{{1,2,3,4,5},{-1,-2,-3,-4,-5},{12,24,35125,4125,5123123},{1245,45125,1233,-123152,-123151,2,3,4,5,1,2,3},{5,4,3,-123152,-123151},{5,4,3,2,1}};
        //Integer[][] x = new Integer[][]{{1,2,3,4,5},{1,2,3,4,5},{1,2,3,4,5},{5,4,3,2,1,2,3,4,5,1,2,3},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1},{5,4,3,2,1}};
        //Integer[][] x = new Integer[][]{{15},{-1},{2},{2},{1},{2},{3},{4},{5},{6},{7},{8},{10},{6},{4},{3},{1},{2},{3},{2},{-1}};
        HybridSortP4<Integer> j = new HybridSortP4<>();
        j.printArray(j.sort(x));
    }

 */
}
