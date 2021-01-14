package main.Language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DiffCheck {

    private static double SCALING_FACTOR = 0.1;

    private DiffCheck() {
    }

    public static double DiceSimilarity(String a, String b) {
        Set<String> docA = findBigrams(a);
        Set<String> docB = findBigrams(b);

        int sizeA = docA.size();
        int sizeB = docB.size();

        docA.retainAll(docB);

        int nt = docA.size();

        return (2.0 * (double) nt) / ((double) (sizeA + sizeB));


    }

    public static double JaroWinklerSimilarity(String a, String b) {
        String[] longer;
        String[] shorter;

        if (wordCount(a) > wordCount(b)) {
            longer = a.split(" ");
            shorter = b.split(" ");
        } else {
            longer = b.split(" ");
            shorter = a.split(" ");
        }

        int halfLength = (shorter.length / 2) + 1;

        String[] forwardCompare = getWordMatchSet(longer, shorter, halfLength);
        String[] reverseCompare = getWordMatchSet(shorter, longer, halfLength);

        if (forwardCompare.length == 0 || reverseCompare.length == 0)
            return 0.0;

        int transpositions = transposition(forwardCompare, reverseCompare);

        double dist = (reverseCompare.length / ((double) shorter.length) + forwardCompare.length / ((double) longer.length)
                + (reverseCompare.length - transpositions) / ((double) reverseCompare.length)) / 3.0;

        int prefixLength = commonPrefixLength(shorter, longer);

        return dist + (SCALING_FACTOR * prefixLength * (1.0 - dist));
    }

    public static double getScalingFactor() {
        return SCALING_FACTOR;
    }

    public static void setScalingFactor(double scalingFactor) {
        SCALING_FACTOR = scalingFactor;
    }

    private static int commonPrefixLength(String[] shorter, String[] longer) {
       int result = 0;

        // Iterate through the shorter string.
        for (int i = 0; i < shorter.length; i++) {
            if (!shorter[i].equalsIgnoreCase(longer[i])) {
                break;
            }
            result++;
        }

        // Limit the result to 4.
        return Math.min(result, 4);
    }

    private static Set<String> findBigrams(String doc) {
        ArrayList<String> bigrams = new ArrayList<>();
        String[] words = doc.split(" ");
        if (words.length < 2) {
            bigrams.add(doc);
        } else {
            for (int i = 1; i < words.length; i++) {
                String bigram = words[i - 1] +
                        " " +
                        words[i];
                bigrams.add(bigram);
            }
        }

        return new HashSet<>(bigrams);
    }

    private static String[] getWordMatchSet(String[] first, String[] second, int range) {
        StringBuilder common = new StringBuilder();

        String[] copy = second.clone();

        for (int i = 0; i < first.length; i++) {
            String findWord = first[i];
            boolean found = false;

            // See if the character is within the limit positions away from the original
            // position of that character.
            for (int j = Math.max(0, i - range); !found && j < Math.min(i + range, second.length); j++) {
                if (copy[j].equals(findWord)) {
                    found = true;
                    common.append(findWord);
                    common.append(" ");
                    copy[j] = "*";
                }
            }
        }
        return common.toString().trim().split(" ");
    }

    private static int transposition(String[] forward, String[] reverse) {
        int count = 0;

        for (int i = 0; i < forward.length; i++) {
            if (!forward[i].equalsIgnoreCase(reverse[i]))
                count++;
        }
        count /= 2;
        return count;
    }

    private static int wordCount(String a) {
        int result = 0;
        int i = 0;
        while (a.indexOf(" ", i) != -1) {
            result++;
            i = a.indexOf(" ") + 1;
        }
        return result;
    }

}



/* PERSONAL COMPARISON */

//    public static double findDifferences(String key, String pattern) {
//        final int length = key.length();
//        int begin = 0;
//        int end = binSearch(key, pattern);
//        double total = 0.0;
//        total += (double)(end - begin) / length;
//        while (end != key.length()) {
//            begin = end + 1;
//            begin = nextSpace(key, begin);
//        }
//        return total;
//    }
//
//    private static int binSearch(String key, String pattern) {
//        int mid = pattern.length() / 2;
//        mid = nextSpace(pattern, mid);
//        while (!key.substring(0, mid).equals(pattern.substring(0, mid))) {
//            if (!key.substring(0, mid).equals(pattern.substring(0, mid))) {
//                mid = mid / 2;
//            } else {
//                mid += (pattern.length() - mid) / 2;
//            }
//            if (mid > pattern.indexOf(" ")) {
//                mid = nextSpace(pattern, mid);
//            } else {
//                mid = 0;
//                break;
//            }
//        }
//        return mid;
//    }
//
//    private static int nextSpace(String pattern, int pos) {
//        if (!pattern.contains(" ") || pattern.charAt(pos) == ' ')
//            return pos;
//        return pattern.indexOf(" ", pos);
//    }