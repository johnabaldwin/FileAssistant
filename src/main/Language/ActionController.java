package main.Language;

import main.IO.FastScanner;
import main.IO.TextScanner;
import main.IO.WordScanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ActionController {

    private static Trie trie;

    private static FastScanner scanner;

    static {
        try {
            trie = new Trie(new File("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ActionController() {}

    public static void spellCheck(String filePath) throws IOException {
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            words.addFirst(" ¬ ¬ ¬BEGIN¬ ¬ ¬");
            String cur = words.removeFirst();
            do {
                if (!trie.contains(cur) && !cur.equals(scanner.getNewParagraph())
                        && !cur.equals(scanner.getNewRun())) {
                    cur = trie.findBestTypo(cur);
                }
                words.add(cur);
                cur = words.removeFirst();
            } while (!cur.equals(" ¬ ¬ ¬BEGIN¬ ¬ ¬"));
            scanner.write(words);

        } else {
            scanner = new TextScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                String curLine = words.removeFirst();
                StringTokenizer tk = new StringTokenizer(curLine);
                while (tk.hasMoreTokens()) {
                    String cur = tk.nextToken();
                    if (!trie.contains(cur)) {
                        cur = trie.findBestTypo(cur);
                    }
                    if (tk.hasMoreTokens())
                        writer.print(cur + " ");
                    else
                        writer.print(cur);
                }
                if (!words.isEmpty())
                    writer.println();
            }
            writer.close();
        }
    }

    public static void fixWhiteSpace(String filePath) throws IOException{
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            scanner.write(words);

        } else {
            scanner = new TextScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();

            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                String curLine = words.removeFirst();
                StringTokenizer tk = new StringTokenizer(curLine);
                while (tk.hasMoreTokens()) {
                    String cur = tk.nextToken();
                    if (tk.hasMoreTokens())
                        writer.print(cur + " ");
                    else
                        writer.print(cur);
                }
                if (!words.isEmpty())
                    writer.println();
            }
            writer.close();
        }
    }

    public static void replaceWords(String filePath, HashMap<String, String> replacements) throws IOException{
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            words.addFirst(" ¬ ¬ ¬BEGIN¬ ¬ ¬");
            String cur = words.removeFirst();
            do {
                if (replacements.containsKey(cur)) {
                    cur = replacements.get(cur);
                }
                words.add(cur);
                cur = words.removeFirst();
            } while (!cur.equals(" ¬ ¬ ¬BEGIN¬ ¬ ¬"));
            scanner.write(words);
        } else {
            scanner = new TextScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                String curLine = words.removeFirst();
                StringTokenizer tk = new StringTokenizer(curLine);
                while (tk.hasMoreTokens()) {
                    String cur = tk.nextToken();
                    if (replacements.containsKey(cur)) {
                        cur = replacements.get(cur);
                    }
                    if (tk.hasMoreTokens())
                        writer.print(cur + " ");
                    else
                        writer.print(cur);
                }
                if (!words.isEmpty())
                    writer.println();
            }
            writer.close();
        }
    }

    //TODO
    @Deprecated
    public static void diffCheck(String filePath1, String filePath2) {

    }

}
