package test.Generator;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Class to generate random word documents
 */
public class RandomWord {

    /**
     * Static method to generate a random word document with unknown word count, length, and typos
     * @return the file path
     */
    public static String generate() {
        try {
            String path = WordWrapper.getRandomWord() + ".docx";
            int paragraphCount = WordWrapper.generateParagraphCount();

            XWPFDocument doc = new XWPFDocument();
            ArrayList<XWPFParagraph> paragraphs = new ArrayList<>();
            for (int i = 0; i < paragraphCount; i++) {
                paragraphs.add(doc.createParagraph());
            }
            for (XWPFParagraph paragraph : paragraphs) {
                XWPFRun temp = paragraph.createRun();
                int runLength = WordWrapper.generateRunLength();
                StringBuilder run = new StringBuilder();
                for (int j = 0; j < runLength; j++) {
                    if (j % 17 == 0) {
                        run.append(WordWrapper.getTypo()).append(" ");
                    } else {
                        run.append(WordWrapper.getRandomWord()).append(" ");
                    }
                }
                temp.setText(run.toString());
            }
            FileOutputStream out = new FileOutputStream(new File(path));
            doc.write(out);
            doc.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Create File Error: (100)");
        }
        return null;
    }
}
