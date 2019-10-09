/*
 * Copyright 2014, Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.cs.sort.hybrid;

import edu.emory.cs.sort.AbstractSort;
import edu.emory.cs.sort.comparison.HeapSort;
import edu.emory.cs.sort.comparison.InsertionSort;
import edu.emory.cs.sort.comparison.ShellSortKnuth;
import edu.emory.cs.sort.divide_conquer.IntroSort;
import edu.emory.cs.sort.divide_conquer.MergeSort;
import edu.emory.cs.sort.divide_conquer.QuickSort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HybridSortPractice<T extends Comparable<T>> extends AbstractSort<T> implements HybridSort<T> {
    private AbstractSort<T> engine;
    private AbstractSort<T> engine1;
    private AbstractSort<T> engine3;
    private T[] temp;

    public HybridSortPractice() {
        super(Comparator.naturalOrder());
        engine = new ShellSortKnuth<>();
        engine1 = new QuickSort<>();
        engine3 = new InsertionSort<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] sort(T[][] input) {
        int[] count = new int[input.length+1];
        count[0]=0;
//sorting individual arrays
        for(int row = 0;row<input.length;row++){
            int countU=0;
            int countD=0;
            if(input[row].length>10) {
                for (int col = 0; col < 10; col++) {
                    if (input[row][col].compareTo(input[row][col + 1]) < 0) {
                        countU++;
                    } else if (input[row][col].compareTo(input[row][col + 1]) > 0) {
                        countD++;
                    }
                }
                if(countU>9) engine3.sort(input[row]);
                else if(countD>9){
                    engine.sort(input[row]);
                }
                else engine1.sort(input[row]);
            }
            else{
                engine3.sort(input[row]);
            }
            count[row+1]=input[row].length;
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



        for(int i=0;i<count.length-1;i++){
            merge(output,count[0],count[i],count[i+1]);
        }
        //merge(output,0,5,10,);
        //engine.sort(output);

        return output;
    }

    @Override
    public void sort(T[] array, int beginIndex, int endIndex) {

    }
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
        //assignments += N;
    }


    private void printArray(T[] x){
        for(int i=0;i<x.length;i++) {
            System.out.print(x[i]+"\t");
        }
    }



    public static void main(String[] args) {
        Integer[][] x = new Integer[][]{{1,2,3,4,5},{5,4,3,2,1},{0,1,2,4,5,6},{9,7,6,5,8,3},{1,2,5,7,2,4}};
        //Integer[][] x = new Integer[][]{{1,2,3,4,5},{5,4,3,2,1}};
        HybridSortPractice<Integer> j = new HybridSortPractice<>();
        j.printArray(j.sort(x));
    }



}
/*
    private void merge(T[] array, int[] count) {

        for(int i=0;i<count.length-1;i++){
            count[i+1]=count[i]+count[i+1];
        }
        for(int i=0;i<count.length-2;i+=2){
            sort(array,count[i],count[i+2],count[i]);
        }
    }
    public void sort(T[] array, int beginIndex, int endIndex,int middleIndex) {
    temp = Arrays.copyOf(array,endIndex-beginIndex);
        int fst = beginIndex, snd = middleIndex;
        for (int k = beginIndex; k < endIndex; k++) {
            if( ){

            }

        }
    }
*/