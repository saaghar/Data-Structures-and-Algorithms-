
import java.util.Comparator;
import java.lang.String;

public class Term {
    //public static Comparator<Term> byLexicographicOrder;
    private String word;
    private long weight;

    // Initializes a term with a given word and weight.
    public Term(String word, long weight) {
        this.word = word;
        this.weight = weight;
    }

    // Gets the word.
    public String getWord() {
        return word;
    }

    // Gets the weight.
    public long getWeight() {
        return weight;
    }

    // Extracts a prefix from the word.
    // If `len` is larger than the word length, the prefix is the entire word.
    public String getPrefix(int len) {
        if(word.length() < len)
            return word;
        else
            return word.substring(0, len);
    }

    // Compares two terms in case-insensitive lexicographic order.
    public static final Comparator<Term> byLexicographicOrder = new LexicoComparator();

    static class LexicoComparator implements Comparator<Term> {
        public int compare(Term a, Term b) {
            return a.word.compareToIgnoreCase(b.word);
        }
    }

    // Compares two terms in descending order by weight.
    public static final Comparator<Term> byReverseWeightOrder = new WeightComparator();

    static class WeightComparator implements Comparator<Term> {
        public int compare(Term a, Term b) {
            if (a.weight < b.weight) return 1;
            else if (a.weight == b.weight) return 0;
            else return -1;
        }
    }

    // This method returns a comparator that compares the two terms in case-insensitive
    // lexicographic order, but using only the first k characters of each word.
    public static Comparator<Term> byPrefixOrder(int k) {
        class PrefixComparator implements Comparator<Term> {
            public int compare(Term a, Term b) {
                String prefixA = a.getPrefix(k);
                String prefixB = b.getPrefix(k);
                return prefixA.compareToIgnoreCase(prefixB);
            }
        }
        return new PrefixComparator();
    }

    /*
    // If you are stuck with creating comparators, here is a silly example that considers all integers equal:
    public static final Comparator<Integer> example0 = new Comparator<Integer>() {
        public int compare(Integer a, Integer b) {
            return 0;
        }
    };

    // And here is the same example using functional syntax:
    public static final Comparator<Integer> example1 = (a, b) -> {
        return 0;
    };

    // This is the same as the following:
    public static final Comparator<Integer> example2 = (a, b) -> 0;
    */

    // Returns a string representation of this term on the form WORD:WEIGHT
    public String toString() {
        return this.getWord() + ":" + this.getWeight();
    }


    //////////////////////////////////////////////////////////////////////
    // For testing purposes.
    // Runs some simple tests on the comparators.
    public static void main(String[] args) {
        Term t1 = new Term("abc", 20);
        Term t2 = new Term("ABCD", 30);
        Term t3 = new Term("abd", 25);
        System.out.println("* Testing byLexicographicOrder:");
        testComparator(Term.byLexicographicOrder, t1, t2, -1);
        testComparator(Term.byLexicographicOrder, t2, t3, -1);
        testComparator(Term.byLexicographicOrder, t3, t1, 1);
        System.out.println();
        System.out.println("* Testing byReverseWeightOrder:");
        testComparator(Term.byReverseWeightOrder, t1, t2, 1);
        testComparator(Term.byReverseWeightOrder, t2, t3, -1);
        testComparator(Term.byReverseWeightOrder, t3, t1, -1);
        System.out.println();
        System.out.println("* Testing byPrefixOrder(3):");
        testComparator(Term.byPrefixOrder(3), t1, t2, 0);
        testComparator(Term.byPrefixOrder(3), t2, t3, -1);
        testComparator(Term.byPrefixOrder(3), t3, t1, 1);
        System.out.println();
        System.out.println("* Testing byPrefixOrder(2):");
        testComparator(Term.byPrefixOrder(2), t1, t2, 0);
        testComparator(Term.byPrefixOrder(2), t2, t3, 0);
        testComparator(Term.byPrefixOrder(2), t3, t1, 0);
        System.out.println();
    }

    // Tests one comparison with the given comparator, and prints the result.
    public static void testComparator(Comparator<Term> cmp, Term t1, Term t2, int correct) {
        int result = cmp.compare(t1, t2);
        System.out.println("compare(" + t1 + ", " + t2 + ") = " + result + (result == correct ? "" : " (ERROR: should be " + correct + ")"));
    }
}
