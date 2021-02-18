package main.Language;

import javafx.util.Pair;

import java.util.*;

public class DiffCheck {

    private static double SCALING_FACTOR = 0.1;
    private static double FALSE_POSITIVE_TOLERANCE = 0.2;

    private DiffCheck() {
    }

    public static double findSimilarity(String key, String pattern) {
        Pair<Double, String> dsPair = DiceSimilarity(key, pattern);
        double ds = dsPair.getKey();
        if (ds > 0.2 && ds < 1.0) {
            Pair<Double, String> jwPair = JaroWinklerSimilarity(key, pattern);
            double jw = jwPair.getKey();
            double lower = ds - (ds * FALSE_POSITIVE_TOLERANCE);
            //double upper = ds + (ds * FALSE_POSITIVE_TOLERANCE);
            if (jw >= lower) {
                return ds;
            } else {
                return jw;
            }
        }
        return ds;
    }

    public static Pair<Double, String> DiceSimilarity(String a, String b) {
        Set<String> docA = findBigrams(a);
        Set<String> docB = findBigrams(b);

        int sizeA = docA.size();
        int sizeB = docB.size();

        docA.retainAll(docB);
        System.out.println(docA);

        Iterator<String> slow = docA.iterator();
        Iterator<String> fast = docA.iterator();
        fast.next();
        while (fast.hasNext()) {
            String bigram1 = slow.next();
            String bigram2 = fast.next();
            String last = bigram1.split(" ")[1];
            String first = bigram2.split(" ")[0];
            if (last.equals(first)) {
                //Add mark to point in string
                int i = a.indexOf(bigram1);
                a = a.substring(0, i) + "¬¬" + a.substring(i);
                //Find end of match sequence
                while (fast.hasNext() && last.equals(first)) {
                    bigram1 = slow.next();
                    bigram2 = fast.next();
                    last = bigram1.split(" ")[1];
                    first = bigram2.split(" ")[0];
                }
                if (last.equals(first))
                    i = a.indexOf(bigram2) + bigram2.length();
                else
                    i = a.indexOf(bigram1) + bigram1.length();
                a = a.substring(0, i) + "¬¬" + a.substring(i);

            }
        }
        int nt = docA.size();

        double result = (2.0 * (double) nt) / ((double) (sizeA + sizeB));
        return new Pair<>(result, a);
    }

    public static Pair<Double, String> JaroWinklerSimilarity(String a, String b) {
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

        String[] compareAB = getWordMatchSet(longer, shorter, halfLength);
        String[] compareBA = getWordMatchSet(shorter, longer, halfLength);
        String[] doc = a.substring(a.indexOf(compareAB[0])).split(" ");

        int lower = 0;
        for (int i = 0; i < compareAB.length; i++) {
            int location = a.indexOf(compareAB[i], lower);
            lower = Math.max(lower, location);
            a = a.substring(0, location) + "¬¬" + a.substring(location);
            while (i + 1 < compareAB.length && doc[i + 1].equals(compareAB[i + 1])) {
                i++;
                lower = a.indexOf(doc[i-1]);
            }
            location = a.indexOf(compareAB[i], lower) + compareAB[i].length();
            lower = Math.max(lower, location);
            a = a.substring(0, location) + "¬¬" + a.substring(location);
        }

        if (compareAB.length == 0 || compareBA.length == 0)
            return new Pair<>(0.0, a);

        int transpositions = transposition(compareAB, compareBA);

        double dist = (compareBA.length / ((double) shorter.length) + compareAB.length / ((double) longer.length)
                + (compareBA.length - transpositions) / ((double) compareBA.length)) / 3.0;

        int prefixLength = commonPrefixLength(shorter, longer);

        double result = dist + (SCALING_FACTOR * prefixLength * (1.0 - dist));

        return new Pair<>(result, a);
    }

    public static double getScalingFactor() {
        return SCALING_FACTOR;
    }

    public static void setScalingFactor(double scalingFactor) {
        SCALING_FACTOR = Math.min(scalingFactor, 0.25);
    }

    public static double getFalsePositiveTolerance() {
        return FALSE_POSITIVE_TOLERANCE;
    }

    public static void setFalsePositiveTolerance(double falsePositiveTolerance) {
        FALSE_POSITIVE_TOLERANCE = Math.min(falsePositiveTolerance, 0.3);
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

    private static LinkedHashSet<String> findBigrams(String doc) {
        LinkedHashSet<String> bigrams = new LinkedHashSet<>();
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

        return bigrams;
    }

    private static String[] getWordMatchSet(String[] first, String[] second, int range) {
        StringBuilder common = new StringBuilder();

        String[] copy = second.clone();

        for (int i = 0; i < first.length; i++) {
            String findWord = first[i];
            boolean found = false;

            // See if the word is within the limit positions away from the original
            // position of that word.
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
        return a.split(" ").length;
    }

    public static void main(String[] args) {
        String a = "constuprate scalpriform interdetermination midnoons twangler intepmitted lazarette sorboside blepharitic vomituses bibliopegist MNRAS Fini scoptically hurly-burlies";
        String b  = "midnoons twangler intepmitted lazarette sorboside blepharitic MNRAS vomituses bibliopegist Fini scoptically hurly-burlies";
        Pair<Double, String> d = DiffCheck.JaroWinklerSimilarity(a, b);
        Pair<Double, String> d2 = DiffCheck.DiceSimilarity(a, b);
        System.out.println(d);
        System.out.println(d2);
    }
}