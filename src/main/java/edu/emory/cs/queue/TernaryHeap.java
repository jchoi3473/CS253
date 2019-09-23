package edu.emory.cs.queue;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;

public class TernaryHeap <T extends Comparable<T>> extends AbstractPriorityQueue<T>{
    private List<T> keys;
    public TernaryHeap(Comparator<T> comparator){ //constructor with parameter
        super(comparator);
        keys = new ArrayList<>();
        keys.add(null); //initialize the first element in the ArrayList as null
    }
    public TernaryHeap(){ //constructor without paramete
        this(Comparator.naturalOrder());
    }

    public void add(T key){//add method to add keys into the ArrayList. After the addition, it uses swim method
        keys.add(key);
        swim(size());
    }
    public T remove(){//remove and return a max key in the arraylist.
        if (isEmpty()) return null;
        Collections.swap(keys, 1, size());
        T max = keys.remove(size());
        sink(1);//this will find the new highest value and will store
        return max;
    }
    public int size(){
        return keys.size()-1;
    }
    //this is the sinking method that gets used during remove
    public void sink(int k){
        //unlike binary, (k*3)-1 is used to set i as the left most item in the heap
        for (int i = (k * 3)-1; i <= size(); k = i, i = (i*3)-1) {
            //this if statement is to determine whether the middle element of the three items in the children is the biggest. if middle one is the biggest,
            //then we add 1 in order to switch i to the middle element. Because we want to examine keys.get(i+2), its very important to set i<size()-2
            //unless, it will throw array out of bounds error.
            if (i < size()-2 && comparator.compare(keys.get(i), keys.get(i+1)) < 0&&comparator.compare(keys.get(i+2), keys.get(i+1))<0) i++;
            //same logic applies here. This time, the right most element gets examined to verify whether its the biggest element or not.
            else if (i < size()-2 && comparator.compare(keys.get(i), keys.get(i+2)) < 0&&comparator.compare(keys.get(i+1), keys.get(i+2))<0) i=i+2;
            //when there are 2 or less elements, that same concept used from binary heap can be used.
            else if (i < size() && comparator.compare(keys.get(i), keys.get(i+1)) < 0) i++;
            //when k becomes equal to or bigger than i, we break the loop since no swap or searching for bigger element is required.
            if (comparator.compare(keys.get(k), keys.get(i)) >= 0) break;
            Collections.swap(keys, k, i);
        }
    }

public void swim(int k){
    //calculation of k+1/3 for all 3 children will lead to only one parent.
    //ex)(2+1)/3=1,(3+1)/3=3,(4+1)/3=3 because they're types are int.
    while (1 < k && comparator.compare(keys.get((k+1)/3), keys.get(k)) < 0) {
        //from while loop, if newly added value is proven to have larger keys, it then uses swap.
        Collections.swap(keys, (k+1)/3, k);
        //after swap, it needs to find the new parent. K becomes the new parent after the calculation and while loop continues
        k = (k+1)/3;
    }
}

/*

    public void swim(int k){
        while (1 < k && comparator.compare(keys.get((int)Math.round(k/3.0)), keys.get(k)) < 0) {
            Collections.swap(keys, (int)Math.round(k/3.0), k);
            k = (int)Math.round(k/3.0);
        }
    }

 */
}
