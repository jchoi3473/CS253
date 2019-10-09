package edu.emory.cs.sort.hybrid;

import edu.emory.cs.sort.AbstractSort;
import edu.emory.cs.sort.distribution.MSDRadixSort;
import edu.emory.cs.sort.distribution.RadixSort;

import java.lang.reflect.Array;
import java.util.Arrays;

public class HybridSortP1<T extends Comparable<T>>  implements HybridSort<T>{
    private AbstractSort<T> engine1;
    private RadixSort engine2;
    private AbstractSort<T> engine3;
    private T[] temp;
    public HybridSortP1() {
        engine2 = new MSDRadixSort();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] sort(T[][] input) {
        int size = Arrays.stream(input).mapToInt(t -> t.length).sum();
        T[] output = (T[])Array.newInstance(input[0][0].getClass(), size);
        int beginIndex = 0;
        for (T[] t : input) {
            System.arraycopy(t, 0, output, beginIndex, t.length);
            beginIndex += t.length;
        }
        //sort(output,beginIndex,output.length+1);
        //engine1.sort(output);
        engine3.sort(output);
        return output;
    }
    /*
    private void printArray(T[] x){
        for(int i=0;i<x.length;i++) {
            System.out.print(x[i]+"\t");
        }
    }
    */
/*

    public static void main(String[] args) {
        Integer[][] x = new Integer[][]{{1,2,3,4,5},{5,4,3,2,1},{0,1,2,4,5,6},{9,7,6,5,8,3},{1,2,5,7,2,4}};
        HybridSortChoi2<Integer> j = new HybridSortChoi2<Integer>();
        j.printArray(j.sort(x));
    }
*/
}
