package main.Language;

import main.IO.FastScanner;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Trie {
    private static class Node {
        private String val;
        private final char c;
        private Node left, mid, right;

        public Node(char c) {
            this.c = c;
            left = mid = right = null;
            val = null;
        }
    }

    private static class Word {
        private final String error;
        private PriorityQueue<Replacement> replacements = new PriorityQueue<>();

        public Word(String key) {
            error = key;
        }
    }

    private static class Replacement implements Comparable<Replacement> {
        private final int difference;
        private final String word;

        public Replacement(int diff, String w) {
            difference = diff;
            word = w;
        }

        @Override
        public int compareTo(Replacement o) {
            return o.difference - this.difference;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof Replacement) {
                Replacement temp = (Replacement) o;
                return this.word.equals(temp.word);
            }
            return false;
        }

        @Override
        public String toString() {
            return word;
        }
    }

    public Node root;
    private Word typo;

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
        System.out.println(temp);
        if (temp == null) {
            System.out.println(cur.c);
            findWords(cur);
        }

        return null;
    }

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

    public Trie(File dictionary) throws IOException {
        Scanner in = new Scanner(dictionary);
        while (in.hasNext()) {
            String cur = in.next();
            put(cur);
        }
    }


    public boolean contains(String key) {
        return get(key) != null;
    }

    public String get(String key) {
        Node nd = this.get(root, key, 0);
        if (nd == null)
            return null;
        return nd.val;
    }

    public void put(String key) {
        root = this.put(root, key, 0);
    }

    public PriorityQueue<Replacement> findTypo(String key) {
        typo = new Word(key);
        findTypo(root, 0);
        return typo.replacements;
    }

    public String fineTypo(String key) {
        return findTypo(key).poll().word;
    }

    public static void main(String[] args) throws IOException {
        Trie test = new Trie(new File("test.txt"));
        System.out.println("hello world");
        if (test.get("hte") == null) {
            PriorityQueue<Replacement> list = test.findTypo("hte");
            System.out.println(list.size());
            System.out.println(list);
            while (!list.isEmpty())
                System.out.println(list.poll().word);
        }
    }
}
