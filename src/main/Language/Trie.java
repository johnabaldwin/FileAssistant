package main.Language;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Trie {

    /**
     * Node class to represent the nodes in the Trie.
     */
    private static class Node {

        //Value of node if it is the termination of a string
        private String val;
        //Character at this node
        private final char c;
        //Child nodes
        private Node left, mid, right;

        /**
         * Constructor to create new node of character {@code c}
         * @param c - character to place at this node
         */
        public Node(char c) {
            this.c = c;
            left = mid = right = null;
            val = null;
        }
    }

    /**
     * Word class to represent misspelled words
     */
    private static class Word {

        //Misspelled word
        private final String error;

        //Possible replacements
        private final PriorityQueue<Replacement> replacements = new PriorityQueue<>();

        /**
         * Word constructor to create new misspelled word
         * @param key - word that is misspelled
         */
        public Word(String key) {
            error = key;
        }
    }

    /**
     * Class that represents replacements to a misspelled word
     */
    private static class Replacement implements Comparable<Replacement> {

        //Hamming distance between this word and the misspelled word
        private final int difference;

        //The replacement word
        private final String word;


        /**
         * Constructor for replacements based on difference and word
         * @param diff - hamming distance between typo and replacement
         * @param w - replacement word
         */
        public Replacement(int diff, String w) {
            difference = diff;
            word = w;
        }

        /**
         * Compare to override for sorting words by hamming distance
         * @param o - replacement to compare to
         * @return the replacement that is a better match
         */
        @Override
        public int compareTo(Replacement o) {
            return o.difference - this.difference;
        }

        /**
         * Compares two {@code Replacement}'s to check word equality
         * @param o - the other replacement to compare
         * @return whether or not these replacements represent the same word
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Replacement) {
                Replacement temp = (Replacement) o;
                return this.word.equals(temp.word);
            }
            return false;
        }

        /**
         * Override of toString method
         * @return - the word this {@code Replacement} represents
         */
        @Override
        public String toString() {
            return word;
        }
    }

    /**
     * Root {@code Node} of this {@code Trie}, should always be empty
     */
    public Node root;

    /**
     * The typo currently being checked
     */
    private Word typo;

    /**
     * Set of all words in this {@code Trie} for fast contains checking
     */
    private final HashSet<String> fastContains = new HashSet<>();

    /**
     * Size of the trie in terms of number of words
     */
    private int size = 0;


    /**
     * Constructor to create a new {@code Trie} from a given dictionary
     * @param dictionary - the file containing all the words to add to this {@code Trie}
     * @throws IOException if the file does not exist
     */
    public Trie(File dictionary) throws IOException {
        Scanner in = new Scanner(dictionary);
        while (in.hasNext()) {
            String cur = in.next();
            put(cur);
        }
    }

    /**
     * Checks for the specified key in the {@code Trie}
     * @param key - the key to check for
     * @return a boolean representing whether or not the {@code key} is in the {@code Trie}
     */
    public boolean contains(String key) {
        return fastContains.contains(key);
    }

    /**
     * Finds the closest replacement to a given misspelled word
     * @param key - the misspelled word
     * @return {@code String} replacement word
     */
    public String findBestTypo(String key) {
        PriorityQueue<Replacement> result = findTypo(key);
        if (result.peek() == null)
            return key;
        return findTypo(key).poll().word;
    }

    /**
     * Public method to find where the typo is in {@code key} and get possible replacements
     * @param key - the word assumed to be a typo
     * @return {@code PriorityQueue<Replacement>} containing possible replacements for the typo
     */
    public PriorityQueue<Replacement> findTypo(String key) {
        typo = new Word(key);
        findTypo(root, 0);
        return typo.replacements;
    }

    /**
     * Private method for determining if there is a typo then finding likely substitutes
     * @param cur - the current {@code Node} in the {@code Trie}
     * @param d - the depth of the current {@code Node}
     * @return null or the {@code Node} with the value of the word assumed to have been a typo
     */
    private Node findTypo(Node cur, int d) {
        char c = typo.error.charAt(d);
        if (cur == null)
            return null;
        if (c == cur.c) {
            if (d == typo.error.length() - 1)
                return cur;
            else
                return findTypo(cur.mid, d + 1);
        }
        Node temp;
        if (c < cur.c) {
            temp = findTypo(cur.left, d);
        } else {
            temp = findTypo(cur.right, d);
        }
        if (temp == null) {
            findWords(cur);
        }

        return null;
    }

    /**
     * Gets all possible replacement words for {@code typo}
     * @param cur - the current {@code Node}
     */
    private void findWords(Node cur) {
        if (cur.val != null) {
            int diff = hammingDistance(typo.error, cur.val);
            Replacement insert = new Replacement(diff, cur.val);
            /*
            This logic is a bit convoluted but it says that if the misspelled word and its match
            are different by at most half the words length, or the word has a smaller difference
            than the current word with the smallest difference, and the new word is not in the
            queue, then it should be inserted.
             */
            if ((diff <= typo.error.length()/2 + 1 || (!typo.replacements.isEmpty() &&
                    diff <= typo.replacements.peek().difference)) &&
                    !typo.replacements.contains(insert))
                typo.replacements.add(insert);
        }
        if (cur.right != null)
            findWords(cur.right);
        if (cur.mid != null)
            findWords(cur.mid);
        if (cur.left != null)
            findWords(cur.left);
    }

    /**
     * Public method to get the specified key from the {@code Trie}
     * @param key - the key to find in the trie
     * @return null or the value of the key
     */
    public String get(String key) {
        Node nd = this.get(root, key, 0);
        if (nd == null)
            return null;
        return nd.val;
    }

    /**
     * Private method to get the specified key from the {@code Trie}
     * @param x - current {@code Node}
     * @param key - the string to find in the {@code Trie}
     * @param d - {@code Node} depth
     * @return null or the {@code Node} with value corresponding to the key
     */
    private Node get(Node x, String key, int d) {
        if (x == null)
            return null;
        char c = key.charAt(d);
        if (x.c == c) {
            if (d == key.length() - 1)
                return x;
            else
                return get(x.mid, key, d + 1);
        } else if (c < x.c) {
            return get(x.left, key, d);
        } else
            return get(x.right, key, d);
    }

    /**
     * Gets the number of words in the {@code Trie}
     * @return an int representing the number of words in the {@code Trie}
     */
    public int getSize() { return size; }

    /**
     * Find the hamming distance between two words
     * @param key - the original word
     * @param comp - the word to compare
     * @return an integer distance between two words
     */
    //TODO - change to %similarity rather than hamming distance
    private int hammingDistance(String key, String comp) {
        int diff = 0;
        diff += Math.abs(key.length() - comp.length());
        for (int i = 0; i < Math.min(key.length(), comp.length()); i++) {
            if (comp.charAt(i) != key.charAt(i))
                diff++;
        }
        return diff;
    }

    /**
     * Public method to put a specified string into the {@code Trie}
     * @param key - the string to insert into the {@code Trie}
     */
    public void put(String key) {
        if (fastContains.add(key)) {
            root = this.put(root, key, 0);
            size++;
        }
    }

    /**
     * Private method to put a specified key in the {@code Trie}
     * @param cur - the current {@code Node} in the {@code Trie}
     * @param key - the key to insert in the {@code Trie}
     * @param d - the depth of the current {@code Node}
     * @return {@code Node} currently being recursed over
     */
    private Node put(Node cur, String key, int d) {
        char c = key.charAt(d);
        if (cur == null)
            cur = new Node(c);
        if (c == cur.c) {
            if (d == key.length() - 1)
                cur.val = key;
            else
                cur.mid = put(cur.mid, key, d + 1);// d+1 means we go down one level
        } else if (c < cur.c)
            cur.left = put(cur.left, key, d);// do not increment if the current char is not matched
        else
            cur.right = put(cur.right, key, d);
        return cur;
    }

    public static void main(String[] args) throws IOException {
        Trie test = new Trie(new File("words.txt"));
        System.out.println("hello world");
        if (test.get("intuiyionally") == null) {
            System.out.println("enter");
            PriorityQueue<Replacement> list = test.findTypo("intuiyionally");
            System.out.println(list.size());
            System.out.println(list);
            while (!list.isEmpty())
                System.out.println(list.poll().word);
        }
    }
}
