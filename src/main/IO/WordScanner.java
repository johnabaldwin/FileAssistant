package main.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Queue;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordScanner extends FastScanner {

    private final XWPFDocument XMLDoc;

    private HashMap<String, String> findReplace = new HashMap<>();

    private Set<String> keys;

    final private String newLine = "";

    final private String newRun = "¬ ¬ ¬ NR  ¬ ¬ ¬";

    final private String space = " ";

    final private String tab = "	";

    /**
     * Constructor for passing {@code File} to create new scanner
     * @param path - the file to be read
     * @throws IOException - IOExceptions are possible if the file does not exist
     */
    public WordScanner(File path) throws IOException {
        super(path.getAbsolutePath());
        XMLDoc = new XWPFDocument(new FileInputStream(path));

    }

    /**
     * Deprecated method for changing whitespace, internal documentation not provided as such
     */
    @Deprecated
    private void changeWhitespace() {
        if (findReplace.containsKey("\t")) {
            findReplace.put(tab, findReplace.get("\t"));
            keys.add(tab);
            keys.remove("\t");
            findReplace.remove("\t");
        }
        if (findReplace.containsKey(" {2,}")) {
            findReplace.put(space, findReplace.get(" "));
            keys.add(space);
            keys.remove(" {2,}");
            findReplace.remove(" ");
        }
        String[] arr = (String[]) keys.toArray();
        for (int i = 0; i < keys.size(); i++) {
            if (findReplace.get(arr[i]).equals("\n")) {
                findReplace.remove(arr[i]);
                findReplace.put(arr[i], newLine);
            }
        }
    }

    /**
     * Reads all text from the Word document into a Queue for parsing
     * @return {@code Queue<String>} containing all words in the document, run data,
     *          and paragraph data
     */
    @Override
    public Queue<String> getDoc() {
        List<XWPFParagraph> paragraphs = XMLDoc.getParagraphs();
        ArrayDeque<String> text = new ArrayDeque<>();
        //For each paragraph in the document
        for (XWPFParagraph p : paragraphs) {
            //Read through all runs
            for (XWPFRun r : p.getRuns()) {
                //Get the text contained in each run
                String run = r.getText(0);
                //parse using a string tokenizer and add each word to the list
                StringTokenizer st = new StringTokenizer(run);
                while (st.hasMoreTokens()) {
                    text.add(st.nextToken());
                }
                //add newRun delimiter
                text.add(newRun);
            }
            //add newParagraph delimiter
            text.add(newParagraph);
        }
        return text;
    }

    /**
     * Returns the {@code newRun} delimiter
     * @return - the {@code newRun} delimiter
     */
    public String getNewRun() {
        return newRun;
    }

    /**
     * Gets a list of all pictures in the document
     * @return - a {@code List<XWPFPictureData>} containing all pictures in this document
     */
    public List<XWPFPictureData> getPictures() {
        return XMLDoc.getAllPictures();
    }


    /**
     * Writes all words back into the file in their original layout
     * @param words - the {@code ArrayDeque} containing words to write to the file
     * @throws IOException if the file is not found or is open in Word
     */
    public void write(ArrayDeque<String> words) throws IOException {
        ArrayDeque<String> newParagraphs = new ArrayDeque<>();
        StringBuilder paragraph = new StringBuilder();
        //Reassemble words into their original runs and paragraphs based on delimiters
        while(!words.isEmpty()) {
            String cur = words.poll();
            if (cur.equals(newParagraph) || cur.equals(newRun)) {
                newParagraphs.add(paragraph.toString());
                paragraph = new StringBuilder();
            } else {
                paragraph.append(cur).append(" ");
            }
        }

        //Change the text of each run
        List<XWPFParagraph> paragraphs = XMLDoc.getParagraphs();
        for (XWPFParagraph p : paragraphs) {
            for (XWPFRun r : p.getRuns()) {
                r.setText(newParagraphs.poll(),0);
            }
        }
        //Rewrite the data
        XMLDoc.write(new FileOutputStream(getPath()));
    }

    public static void main(String[] args) throws IOException {

    }
}