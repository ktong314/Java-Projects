/*
 * This file is how you might test out your code.  Don't submit this, and don't
 * have a main method in SortTools.java.
 */

package assignment1;

import java.util.Arrays;

public class Main {
    public static void main(String [] args) {
        // call your test methods here
    	int[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    	int[] y = {1, 3, 5, 7, 8, 11, 15, 234, 1234, 123456};
    	int[] z = {-5, -4, -3, -2, -1, 0};
    	int[] w = {123, 51, 32, 754, 21, 1234, 22, 1, -15, -123};
    	
    	if(SortTools.isSorted(z, z.length)) {
    		System.out.println("sorted");
    	} else {
    		System.out.println("not sorted");
    	}
    	int index = SortTools.find(z, z.length, -3);
    	System.out.println(index);
    	
    	int[] copied = SortTools.copyAndInsert(y, 8, 16);
    	System.out.println("new array: " + Arrays.toString(copied));
    	
    	int number = SortTools.insertInPlace(y, 9, 8);
    	System.out.println(number + " new array: " + Arrays.toString(y));
        // SortTools.isSorted() etc.
    	
    	System.out.println("before sort: " + Arrays.toString(w));
    	SortTools.insertSort(w, 10);
    	System.out.println("after sort: " + Arrays.toString(w));
    	
    }
}
