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
            String cur = words.getFirst();
            do {
                if (!trie.contains(cur) && !cur.equals(scanner.getNewParagraph())
                        && !cur.equals(scanner.getNewRun())) {
                    cur = trie.fineTypo(cur);
                }
                words.push(cur);
                cur = words.getFirst();
            } while (!cur.equals(" ¬ ¬ ¬BEGIN¬ ¬ ¬"));
            scanner.write(words);

        } else {
            scanner = new TextScanner(new File(filePath));
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();

            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                String cur = words.getFirst();
                if (!trie.contains(cur)) {
                    cur = trie.fineTypo(cur);
                }
                writer.print(cur);
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
                if (words.peek().trim().equals("NEWLINE")) {
                    writer.println();
                    continue;
                }
                writer.print(words.poll());
            }
            writer.close();
        }
    }

    public static void replaceWords(String filePath, HashMap<String, String> replacements) throws IOException{
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath), replacements);

        } else {
            scanner = new TextScanner(new File(filePath), replacements);
        }
    }

    //TODO
    @Deprecated
    public static void diffCheck(String filePath1, String filePath2) {

    }

}
