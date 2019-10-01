package edu.emory.cs.sort.distribution;

import java.util.Comparator;

public class MSDRadixSort extends RadixSort {
    //For MSDRadixSort, I used a recursion method.
    //This method helps to find the maxBit and sends it to the recursive method.
    //I decided to declare maxBit here so that it only runs once and the value stays the same
    public void sort(Integer[] array, int beginIndex, int endIndex) {
        int maxBit = getMaxBit(array, beginIndex, endIndex);
        int bit = maxBit;
        sort(array, beginIndex,endIndex,maxBit);
    }

    //this is the recursive method that i used to operate MSD
    public void sort(Integer[] array, int beginIndex, int endIndex, int m) {
        //
        int[] counter = new int[12]; //this array contains index numbers so that i can keep track of the bucket within the array
        if(m<=0) return;
        int div = (int) Math.pow(10, m-1);
        sort(array,beginIndex,endIndex,k -> k/div);
        for(int i=0;i<10;i++){ //i made temp array in the bucket sort and this kept number of elements in each bucket. By this operation, it now contains index of numbers representing each bucket
            temp[i+1]+=temp[i];
        }
        for(int i=0;i<temp.length;i++) //because beginIndex starts from 0, i created counter array that has the first element as 0 and the rest same as temp, an index array.
            counter[i+1]=temp[i];
    /*
        for(int i=0;i<11;i++){
            System.out.print(temp[i]+"\t");
        }
        System.out.println();
        for(int i=0;i<11;i++){
        System.out.print(counter[i]+"\t");
        }
        System.out.println();
*/

        for(int i=0;i<10;i++){//this is the recursive method that i adopted, it goes around the index, and m(maxbit)-1 makes it to move to the lesser significant digit
            sort(array,beginIndex+counter[i],beginIndex+counter[i+1],m-1);
        }
        /*
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+"\t");
        }
        System.out.println();

         */
    }
    /*
        Integer[] copy = new Integer[array.length];
        if (m<=0) return;
            else {
                int[] count = new int[12];
                int div = (int) Math.pow(10, m - 1);
                sort(array, beginIndex, endIndex, k -> k / div);
                for (int i = 0; i < 10; i++) {
                    count[i] = temp[i];
                }
                for (int a = 0; a < 10; a++) {
                    count[a + 1] += count[a];
                }
                for (int j = 0; j < 10; ) {
                    for(int i=count[j];i<count[j+1];i++){
                        copy[i]=array[i];
                    }
                    index = index + temp[j];
                    j++;
                    m--;
                    sort(copy,count[j], count[j+1], m);

                }
            }
    }
*/
    /*
    temp = new int[array.length+2];
      if (m < 0) return;
        else {
            for(int k =0;k<array.length;k++){
                System.out.print(array[k]+"\t");
            }
            System.out.println();

            int div = (int) Math.pow(10, m - 1);
            sort(array, beginIndex, endIndex, k -> k / div);
                //index = index + temp[j];

            for (int j = 0; j < 10;j++) {
                sort(array, beginIndex + temp[j]-1, beginIndex+temp[j+1]-1, m-1);
            }
        }
    }
*/
    /*
        for (int bit = 0; bit <3; bit++) {
            if (beginIndex > endIndex) return;
            else {
                for (int k = 0; k < array.length; k++) {
                    System.out.print(array[k] + "\t");
                }
                System.out.println();

                int div = (int) Math.pow(10, m - 1);
                sort(array, beginIndex, endIndex, k -> k / div);
                //index = index + temp[j];
                    i++;
                    sort(array, beginIndex + temp[i-1], beginIndex + temp[i], m);
                }
            }
        }
*/
    /*
    public static void main(String[] args) {
    MSDRadixSort msd = new MSDRadixSort();
    Integer[] x = new Integer[]{123,414,23,41,343,1,515,231,727,834,923,256,568,423};
    msd.sort(x,0,14);

    }

     */
/*
    public static void main(String[] args) {
        MSDRadixSort md = new MSDRadixSort();
        Integer[] x = new Integer[]{1,231,123,415,234,23,24,25,21,25,27,283,848,593,474,567,568,562,564,991,912};
        md.sort(x,0,21);

    }


 */

}

