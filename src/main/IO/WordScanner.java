package main.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    final private String space = " ";

    final private String tab = "	";

    final private String newLine = "";

    final private String newRun = "¬ ¬ ¬ NR  ¬ ¬ ¬";

    public WordScanner(File path) throws IOException {
        super(path.getAbsolutePath());
        XMLDoc = new XWPFDocument(new FileInputStream(path));

    }

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

    @Override
    public Queue<String> getDoc() {
        List<XWPFParagraph> paragraphs = XMLDoc.getParagraphs();
        ArrayDeque<String> text = new ArrayDeque<>();
        for (XWPFParagraph p : paragraphs) {
            for (XWPFRun r : p.getRuns()) {
                String run = r.getText(0);
                StringTokenizer st = new StringTokenizer(run);
                while (st.hasMoreTokens()) {
                    text.add(st.nextToken());
                }
                text.add(newRun);
            }
            text.add(newParagraph);
        }
        return text;
    }

    public List<XWPFPictureData> getPictures() {
        return XMLDoc.getAllPictures();
    }


    public void write(ArrayDeque<String> words) throws IOException {
        ArrayDeque<String> newParagraphs = new ArrayDeque<>();
        StringBuilder paragraph = new StringBuilder();
        while(!words.isEmpty()) {
            String cur = words.poll();
            if (cur.equals(newParagraph) || cur.equals(newRun)) {
                newParagraphs.add(paragraph.toString());
                paragraph = new StringBuilder();
            } else {
                paragraph.append(cur).append(" ");
            }
        }

        List<XWPFParagraph> paragraphs = XMLDoc.getParagraphs();
        for (XWPFParagraph p : paragraphs) {
            for (XWPFRun r : p.getRuns()) {
                r.setText(newParagraphs.poll(),0);
            }
        }
        XMLDoc.write(new FileOutputStream(getPath()));
    }

    public String getNewRun() {
        return newRun;
    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException {

    }
}