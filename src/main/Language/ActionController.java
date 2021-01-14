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

    /**
     * Trie for spellchecking
     */
    private static Trie trie;

    /**
     * Scanner for reading and writing to documents
     */
    private static FastScanner scanner;

    /*
      Static block for creating the new {@code Trie} with the dictionary "words.txt"
     */
    static {
        try {
            trie = new Trie(new File("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ActionController() {}

    //TODO
    @Deprecated
    public static void diffCheck(String[] filePaths) throws IOException {
        StringBuilder[] fileContent = new StringBuilder[2];
        //read all content from files into two separate StringBuilders
        for (int i = 0; i < 2; i++) {
            String extension = filePaths[i].substring(filePaths[i].lastIndexOf('.'));
            if (extension.equals(".doc") || extension.equals(".docx")) {
                scanner = new WordScanner(new File(filePaths[i]));
                ArrayDeque<String> doc = (ArrayDeque<String>) scanner.getDoc();
                //read doc, if it is the last word don't put a space
                while (!doc.isEmpty()) {
                    if (doc.size() > 1) {
                        fileContent[i].append(doc.removeFirst());
                        fileContent[i].append(" ");
                    } else {
                        fileContent[i].append(doc.removeFirst());
                    }
                }
            } else {
                scanner = new TextScanner(filePaths[i]);
                ArrayDeque<String> doc = (ArrayDeque<String>) scanner.getDoc();
                //read doc, if it is the last word don't put a space
                while (!doc.isEmpty()) {
                    StringTokenizer stk = new StringTokenizer(doc.removeFirst());
                    while (stk.hasMoreTokens()) {
                        if (stk.countTokens() == 1 || doc.isEmpty()) {
                            fileContent[i].append(stk.nextToken());
                        } else {
                            fileContent[i].append(stk.nextToken());
                            fileContent[i].append(" ");
                        }
                    }
                }
            }
        }
        String key;
        String pattern;
        if (fileContent[0].length() > fileContent[1].length()) {
            key = fileContent[0].toString();
            pattern = fileContent[1].toString();
        } else {
            key = fileContent[1].toString();
            pattern = fileContent[0].toString();
        }
        //DiffCheck.findDifferences(key, pattern);
    }

    /**
     * Static method to fix white space in the given file
     * @param filePath - the file path of the file to be fixed
     * @throws IOException may be thrown in case of file not found or if files are open during editing
     */
    public static void fixWhiteSpace(String filePath) throws IOException{
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            //Reading entire document removes any anomalous whitespace as is
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            scanner.write(words);

        } else {
            scanner = new TextScanner(new File(filePath));
            //Read document line by line
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            //Write the entire document back out as normal replaces the anomalous whitespace
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

    /**
     * Static method to replace specified words in the document with their given replacements
     * @param filePath - the file path of the file to be read
     * @param replacements - the words to replace and their replacements
     * @throws IOException may be thrown in case of file not found or if files are open during editing
     */
    public static void replaceWords(String filePath, HashMap<String, String> replacements) throws IOException{
        //Get extension to determine the file type
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        //If word doc create a new WordScanner
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            //Read entire document word by word
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            //Add begin element to prevent looping
            words.addFirst(" ¬ ¬ ¬BEGIN¬ ¬ ¬");
            String cur = words.removeFirst();
            do {
                //If the current word is in the map, replace it with its value
                if (replacements.containsKey(cur)) {
                    cur = replacements.get(cur);
                }
                //Add back into the queue
                words.add(cur);
                cur = words.removeFirst();
            } while (!cur.equals(" ¬ ¬ ¬BEGIN¬ ¬ ¬"));
            //Write words back into the document
            scanner.write(words);
        } else {
            scanner = new TextScanner(new File(filePath));
            //Read document line by line
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            //Open new PrintWriter
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                String curLine = words.removeFirst();
                //Read lines word by word
                StringTokenizer tk = new StringTokenizer(curLine);
                while (tk.hasMoreTokens()) {
                    String cur = tk.nextToken();
                    //If the current word is in the map, replace it with its value
                    if (replacements.containsKey(cur)) {
                        cur = replacements.get(cur);
                    }
                    //Write words out to document
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

    /**
     * Static method to perform spellcheck functionality
     * @param filePath - the file path of the document to spellcheck
     * @throws IOException thrown in case of a file not found or documents open during execution
     */
    public static void spellCheck(String filePath) throws IOException {
        //Get the extension to determine the file type
        String extension = filePath.substring(filePath.lastIndexOf('.'));
        //If document is a word doc create a WordScanner
        if (extension.equals(".doc") || extension.equals(".docx")) {
            scanner = new WordScanner(new File(filePath));
            //Read entire document
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            //Add begin point to prevent looping
            words.addFirst(" ¬ ¬ ¬BEGIN¬ ¬ ¬");
            String cur = words.removeFirst();
            do {
                //If the word isn't in the trie and it isn't a delimiter, get the best matching word
                if (!trie.contains(cur) && !cur.equals(scanner.getNewParagraph())
                        && !cur.equals(scanner.getNewRun())) {
                    cur = trie.findBestTypo(cur);
                }
                words.add(cur);
                cur = words.removeFirst();
            } while (!cur.equals(" ¬ ¬ ¬BEGIN¬ ¬ ¬"));
            //Call WordScanner.write to write the new list back into the document
            scanner.write(words);

        } else { //If doc is anything else use a TextScanner
            scanner = new TextScanner(new File(filePath));
            //Read entire document
            ArrayDeque<String> words = (ArrayDeque<String>) scanner.getDoc();
            //Open PrintWriter for writing corrected words as it goes
            PrintWriter writer = new PrintWriter(new FileWriter(new File(filePath)));
            while (!words.isEmpty()) {
                //Read in current line
                String curLine = words.removeFirst();
                //Parse words
                StringTokenizer tk = new StringTokenizer(curLine);
                while (tk.hasMoreTokens()) {
                    String cur = tk.nextToken();
                    //If word is not in trie get the best match
                    if (!trie.contains(cur)) {
                        cur = trie.findBestTypo(cur);
                    }
                    //While more words on the line print word and space
                    if (tk.hasMoreTokens())
                        writer.print(cur + " ");
                    else //Otherwise print a new line
                        writer.print(cur);
                }
                if (!words.isEmpty())
                    writer.println();
            }
            writer.close();
        }
    }

}
