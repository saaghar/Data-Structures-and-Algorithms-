
import java.util.Arrays;
import java.util.Comparator;

public class RangeBinarySearch {

    // Returns the index of the *first* element in `a` that equals the search key,
    // according to the given comparator, or -1 if there is no matching element.
    // Precondition: `a` is sorted according to the given comparator.
    // Complexity: O(log N) comparisons where N is the length of `a`
    public static<T> int firstIndexOf(T[] a, T key, Comparator<T> comparator) {

        int lo = 0;
        int hi = a.length-1;
        int mid;
        int found = -1;


        while(lo <= hi) {
            mid = (lo + hi) / 2;

            if (comparator.compare(a[mid], key) < 0) {
                lo = mid + 1;
            }
            else if (comparator.compare(a[mid], key) > 0) {
                hi = mid - 1;
            }
            else {
                found = mid;
                hi = mid - 1;
            }
        }
        return found;
    }

    // Returns the index of the *last* element in `a` that equals the search key,
    // according to the given comparator, or -1 if there are is matching element.
    // Precondition: `a` is sorted according to the given comparator.
    // Complexity: O(log N) comparisons where N is the length of `a`
    public static<T> int lastIndexOf(T[] a, T key, Comparator<T> comparator) {


        int lo = 0;
        int hi = a.length-1;
        int mid;
        int found = -1;


        while(lo <= hi) {
            mid = (lo + hi) / 2;

            //System.out.println(comparator.compare(a[mid],key ));
            if (comparator.compare(a[mid], key) < 0) {
                lo = mid + 1;
            }
            else if (comparator.compare(a[mid], key) > 0) {
                hi = mid - 1;
            }
            else {
                found = mid;
                lo = mid + 1;
            }
        }
        return found;
    }


    // For testing purposes.
    public static void main(String[] args) {
        // Here you can write some tests if you want.
        String s1 = "a";
        String s2 = "b";
        String s3 = "c";
        String[] s_list = new String[]{s1, s2, s3, s1};

        class ComparatorTest implements Comparator<String> {
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        }

        Arrays.sort(s_list, new ComparatorTest());

        //System.out.println("here");

        int test1 = firstIndexOf(s_list, s2, new ComparatorTest());

        //System.out.println("or here");

        int test2 = firstIndexOf(s_list, "y", new ComparatorTest());

        //System.out.println("test2");

        int test3 = firstIndexOf(s_list, s1, new ComparatorTest());

        //System.out.println("test 3");

        if (test1 == 2) {
            System.out.println("Nice!");
        } else System.out.println("Test 1 FAIL!!!");

        if (test2 == -1) {
            System.out.println("Nice!");
        } else System.out.println("Test 2 FAIL!!!");

        if (test3 == 0) {
            System.out.println("Nice!");
        } else System.out.println("Test 3 FAIL!!!");

        int test4 = lastIndexOf(s_list, s2, new ComparatorTest());

        int test5 = lastIndexOf(s_list, "y", new ComparatorTest());

        int test6 = lastIndexOf(s_list, s1, new ComparatorTest());

        if (test4 == 2) {
            System.out.println("Nice!");
        } else System.out.println("Test 4 FAIL!!!");

        if (test5 == -1) {
            System.out.println("Nice!");
        } else System.out.println("Test 5 FAIL!!!");

        if (test6 == 1) {
            System.out.println("Nice!");
        } else System.out.println("Test 6 FAIL!!!");
    }
}
