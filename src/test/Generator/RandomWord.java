package test.Generator;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//TODO
public class RandomWord {

    public static String generate() {
        try {
            String path = WordWrapper.getRandomWord() + ".docx";
            int wordCount = WordWrapper.generateWordCount();
            int paragraphCount = WordWrapper.generateParagraphCount();

            XWPFDocument doc = new XWPFDocument();
            ArrayList<XWPFParagraph> paragraphs = new ArrayList<>();
            for (int i = 0; i < paragraphCount; i++) {
                paragraphs.add(doc.createParagraph());
            }
            for (int i = 0; i < paragraphs.size(); i++) {
                XWPFRun temp = paragraphs.get(i).createRun();
                int runLength = WordWrapper.generateRunLength();
                String run = "";
                for (int j = 0; j < runLength; j++) {
                    if (j % 17 == 0) {
                        run += WordWrapper.getTypo() + " ";
                    } else {
                        run += WordWrapper.getRandomWord() + " ";
                    }
                }
                temp.setText(run);
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
