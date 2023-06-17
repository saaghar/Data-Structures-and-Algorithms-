import java.util.Arrays;
import java.util.Comparator;

public class Autocompleter {
    private Term[] dictionary;

    // Initializes the dictionary from the given array of terms.
    public Autocompleter(Term[] dictionary) {
        this.dictionary = dictionary;
        sortDictionary();
    }

    // Sorts the dictionary in *case-insensitive* lexicographic order.
    // Complexity: O(N log N) where N is the number of dictionary terms
    private void sortDictionary() {
        Arrays.sort(dictionary, Term.byLexicographicOrder);
    }

    // Returns the number of terms that start with the given prefix.
    // Precondition: the internal dictionary is in lexicographic order.
    // Complexity: O(log N) where N is the number of dictionary terms
    public int numberOfMatches(String prefix) {
        Term t = new Term(prefix, 0);

        int first = RangeBinarySearch.firstIndexOf(dictionary, t, Term.byPrefixOrder(prefix.length()));
        if(first == -1)
            return 0;
        int last = RangeBinarySearch.lastIndexOf(dictionary, t, Term.byPrefixOrder(prefix.length()));

        return last - first + 1;
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    // Precondition: the internal dictionary is in lexicographic order.
    // Complexity: O(log N + M log M) where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        Term[] tList = new Term[numberOfMatches(prefix)];

        Term t = new Term(prefix, 0);

        int first = RangeBinarySearch.firstIndexOf(dictionary, t, Term.byPrefixOrder(prefix.length()));

        for(int i = 0; i < tList.length; i++){
            tList[i] = dictionary[first];
            first++;
        }
        Arrays.sort(tList, Term.byReverseWeightOrder);
        return tList;
    }
}
