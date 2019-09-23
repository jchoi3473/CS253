package edu.emory.cs.sort.comparison;

import java.util.Comparator;
import java.util.Collections;


public class ShellSortHibbard<T extends Comparable<T>> extends ShellSort<T> {
    public ShellSortHibbard(){
        this(Comparator.naturalOrder());
    }
    public ShellSortHibbard(Comparator<T> comparator){
        this(comparator, 1000);
    }
    public ShellSortHibbard(Comparator<T> comparator, int n){
        super(comparator, n);
    }
/*
Like Knuth, Only need to adjust Math.pow(2,t)-1, so it can satisfy the Hibbard Sequence.
 */
    protected void populateSequence(int n) {
        for (int t = sequence.size() + 1; ; t++) {
            int h = (int) ((Math.pow(2, t) - 1));
            if (h <= n) sequence.add(h);
            else break;
        }
    }
/*
Because Hibbard does not have conditions like Knuth, n/3 isn't necessary.
 */
    protected int getSequenceStartIndex(int n) {
        int index = Collections.binarySearch(sequence, n);
        if (index < 0) index = -(index + 1);
        if (index == sequence.size()) index--;
        return index;
    }
}
