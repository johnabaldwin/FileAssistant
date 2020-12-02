package IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import IO.FastScanner;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordScanner extends FastScanner {
    //most likely unnecessary
    //private final FileInputStream fileStream;

    private final XWPFDocument XMLDoc;

    private final XWPFWordExtractor XMLExtractor;

    private HashMap<String, String> findReplace;

    private Set<String> keys;

    final private String space = " ";

    final private String tab = "	";

    final private String newLine = "";

    public WordScanner(File path) throws IOException, FileNotFoundException {
        super(path.getAbsolutePath());
        XMLDoc = new XWPFDocument(new FileInputStream(path));
        XMLExtractor = new XWPFWordExtractor(XMLDoc);

    }

    public WordScanner(File path, HashMap<String, String> fR) throws IOException, FileNotFoundException {
        super(path.getAbsolutePath());
        XMLDoc = new XWPFDocument(new FileInputStream(path));
        XMLExtractor = new XWPFWordExtractor(XMLDoc);
        findReplace = fR;
        keys = fR.keySet();
        System.out.println(keys);

        //changeWhiteSpace();

        search();
    }

    private void search() throws IOException, FileNotFoundException {
        Iterator paragraphIterator = XMLDoc.getParagraphsIterator();
        XWPFParagraph paragraph;
        while (paragraphIterator.hasNext()) {
            paragraph = (XWPFParagraph) paragraphIterator.next();
            for (String key : keys) {
                //String replacement = paragraph.getParagraphText().replaceAll(key, findReplace.get(key));
                List<XWPFRun> runs = paragraph.getRuns();
                String last = "";
                for (XWPFRun r : runs) {
                    System.out.println(r);
                    String replacement = r.getText(0);

                    replacement = checkSpaces(replacement);
                    if (replacement != null && replacement.contains(key)) {
                        r.setText(replacement.replace(key, findReplace.get(key)), 0);
                    }
                    last = replacement;
                }
            }
        }
        XMLDoc.write(new FileOutputStream(getPath()));
    }

    private String checkSpaces(String r) {
        if (r != null && r.charAt(0) == ' ') {
            System.out.println("enter");
            r = r.replaceFirst(" ", "\n");
        }
        if (r != null && r.endsWith(" ")) {
            r = r.trim();
        }
        if (r != null && r.equals(" ")) {
            r = "";
        }
        System.out.println(r);
        return r;
    }

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

    public void getPictures() {
        List<XWPFPictureData> pictures = XMLDoc.getAllPictures();
    }

    /**
     * @param args
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        HashMap<String, String> testMap = new HashMap<>();
        testMap.put(" {2,}", "\n");
        WordScanner test = new WordScanner(new File("C:\\Users\\John\\Documents\\costa concordia.docx"), testMap);
    }

}