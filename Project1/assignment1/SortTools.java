package assignment1;

public class SortTools {
    /**
      * Return whether the first n elements of x are sorted in non-decreasing
      * order.
      * @param x is the array
      * @param n is the size of the input to be checked
      * @return true if array is sorted
      */
    public static boolean isSorted(int[] x, int n) {
        // stub only, you write this!
    	if(x == null) {
    		return false;
    	}
    	if(n > x.length || n <= 0) {
    		return false;
    	}
    	if(x.length == 0) {
    		return false;
    	}
    	for(int i = 0; i < n - 1; i++) {
    		if(x[i] > x[i + 1]) {
    			return false;
    		}
    	}
        // TODO: complete it
        return true;
    }

    /**
     * Return an index of value v within the first n elements of x.
     * @param x is the array
     * @param n is the size of the input to be checked
     * @param v is the value to be searched for
     * @return any index k such that k < n and x[k] == v, or -1 if no such k exists
     */
    public static int find(int[] x, int n, int v) {
        if(isSorted(x, n)) {
        	int start = 0;
        	int end = n - 1;
        	int mid = 0;
        	while(start <= end) {
        		mid = (start + end)/2;
        		if(x[mid] < v) {
        			start = mid + 1;
        		}
        		else if(x[mid] > v) {
        			end = mid - 1;
        		}
        		else {
        			return mid;
        		}
        	}
        	return -1;
        	
        }
        return -1;
    }

    /**
     * Return a sorted, newly created array containing the first n elements of x
     * and ensuring that v is in the new array.
     * @param x is the array
     * @param n is the number of elements to be copied from x
     * @param v is the value to be added to the new array if necessary
     * @return a new array containing the first n elements of x as well as v
     */
    public static int[] copyAndInsert(int[] x, int n, int v) {
    	if(isSorted(x, n)) {
    		boolean exists = false;
    		int replace = n;
    		for(int i = n - 1; i >= 0; i--) {
            	if(x[i] == v) {
            		exists = true;
            	}
            	if(v < x[i]) {
            		replace = i;
            	}
            }
    		if(exists) {
    			int[] array = new int[n];
    			for(int i = 0; i < n; i++) {
    				array[i] = x[i];
    			}
    			return array;
    		}
    		int[] array = new int[n + 1];
    		int ind = 0;
            for(int i = 0; i <= n; i++) {
            	if(i == replace) {
            		array[i] = v;
            	} else {
            		array[i] = x[ind];
            		ind++;
            	}
            }
            return array;
    	}
    	return null;
    }

    /**
     * Insert the value v in the first n elements of x if it is not already
     * there, ensuring those elements are still sorted.
     * @param x is the array
     * @param n is the number of elements in the array
     * @param v is the value to be added
     * @return n if v is already in x, otherwise returns n+1
     */
    public static int insertInPlace(int[] x, int n, int v) {
        if(isSorted(x, n) && n < x.length && n > 0){
    		boolean exists = false;
    		for(int i = 0; i < n; i++) {
            	if(x[i] == v) {
            		exists = true;
            	}
            }
    		if(exists) {
    			return n;
    		}
    		int[] newArr = copyAndInsert(x, n, v);
    		for(int i = 0; i <= n; i++) {
    			x[i] = newArr[i];
    		}
            return n + 1;
        }
        return -1;
    }

    /**
     * Sort the first n elements of x in-place in non-decreasing order using
     * insertion sort.
     * @param x is the array to be sorted
     * @param n is the number of elements of the array to be sorted
     */
    public static void insertSort(int[] x, int n) {
        for(int i = 1; i < n; i++) {
        	int key = x[i];
        	int j = i - 1;
        	while(j >= 0 && x[j] > key) {
        		x[j + 1] = x[j];
        		j = j - 1;
        	}
        	x[j + 1] = key;
        }
    	
    }
}
