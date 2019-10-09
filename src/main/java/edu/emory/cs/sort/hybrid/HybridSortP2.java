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
public class HybridSortP2<T extends Comparable<T>> extends AbstractSort<T> implements HybridSort<T> {
    private AbstractSort<T> engine;
    private AbstractSort<T> engine1;
    private AbstractSort<T> engine3;
    private T[] temp;
    int[] count;

    public HybridSortP2() {
        super(Comparator.naturalOrder());
        engine = new ShellSortKnuth<>();
        engine1 = new QuickSort<>();
        engine3 = new InsertionSort<>();
    }
    @Override
    @SuppressWarnings("unchecked")
    public T[] sort(T[][] input) {
        count = new int[input.length+1];
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
        //System.out.println(size);
        T[] output = (T[])Array.newInstance(input[0][0].getClass(), size);
        int beginIndex = 0;
        for (T[] t : input) {
            System.arraycopy(t, 0, output, beginIndex, t.length);
            beginIndex += t.length;
        }

        for(int i=0;i<count.length-1;i++){
            count[i+1]=count[i]+count[i+1];
        }
        int n = (int)Math.sqrt(input.length);
        int a = (count.length-1)% (int)Math.pow(4,n);
        if(a!=0) {
            if (count.length < 5) {
                for (int i = 0; i < a; i++) {
                    merge(output, count[0], count[i], count[i + 1]);
                }
            } else {
                for (int i = 0; i < a; i++) {
                    merge(output, count[0], count[i], count[i + 1]);
                }
            }
            for (int i = 0; i < count.length - a - 1; i++) {
                count[i + 1] = count[i + a + 1];
            }
            int[] temp = new int[count.length - a];
            for (int i = 0; i < temp.length; i++)
                temp[i] = count[i];

            count = Arrays.copyOf(temp, temp.length);
            //System.out.println();
        }
            merge(output,count);

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

    private void merge(T[] array, int[] count) {
        if(count.length<=2){
            return;
        }
        else {
            //System.out.print(count.length+"\n");
            for (int i = 0; i < count.length-2; i += 2) {
                int fst = count[i], snd = count[i + 1];
                copy(array, count[i], count[i + 2]);
                for (int k = count[i]; k < count[i + 2]; k++) {
                    if (fst >= count[i + 1])                    // no key left in the 1st half
                        assign(array, k, temp[snd++]);
                    else if (snd >= count[i + 2])                  // no key left in the 2nd half
                        assign(array, k, temp[fst++]);
                    else if (compareTo(temp, fst, snd) < 0)    // 1st key < 2nd key
                        assign(array, k, temp[fst++]);
                    else
                        assign(array, k, temp[snd++]);
                }
            }
            int[] temp = new int[(count.length+1)/2];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = count[i * 2];
            }
            this.count = new int[temp.length];
            count = Arrays.copyOf(temp, temp.length);
            merge(array, count);
        }
        //System.out.print(count.length+"\n");
        //System.out.print(temp.length+"\n");
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
/*

    private void printArray(T[] x){
        for(int i=0;i<x.length;i++) {
            System.out.print(x[i]+"\t");
        }
    }
    public static void main(String[] args) {
        //Integer[][] input = {{0, 1, 2, 3}, {3,2,1,0}, {1,3,2,4}, {4, 7, 6, 5}, {9, 8, 11, 10}};
        //Integer[][] x = new Integer[][]{{1,2,3,4,5},{1,2,3,4,5},{1,2,3,4,5},{5,4,3,2,1,2,3,4,5,1,2,3}};
        //Integer[][] x = new Integer[][]{{1,2,3},{1,2,3},{3,4,5},{5,4,3},{5,4,3},{5,4,3},{5,4,3}};
        //Integer[][] x = new Integer[][]{{1,2,3,4,5},{-1,-2,-3,-4,-5},{12,24,35125,4125,5123123},{1245,45125,1233,-123152,-123151,2,3,4,5,1,2,3},{5,4,3,-123152,-123151},{5,4,3,2,1}};

        Integer[][] x = new Integer[][]{{0,2,7},{0,4,3},{3,4,5},{1,5,6},{18,11,13},{13,17,5},{12,10,3},{12,10,3},
                {12,10,3},{1,2,7},{5,4,3},{3,4,5},{1,5,6},{18,11,13},{13,17,5},
                {12,10,3},{12,10,3},{1,2,7},{5,4,3},{3,4,5},{1,72,6},{18,11,13},{13,17,5},{12,10,3},{1,5,6},{4,3,13}
                ,{12,10,3},{12,10,3},{1,2,7},{5,4,3},{1,2,7},{5,78,3},{3,4,5},{1,5,6},{18,21,22}};


        HybridSortP2<Integer> j = new HybridSortP2<>();
        j.printArray(j.sort(x));
    }

 */
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