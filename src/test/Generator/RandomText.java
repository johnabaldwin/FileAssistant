package test.Generator;

import java.io.*;

public class RandomText {

    public static String generate() {
        try {
            String path = WordWrapper.getRandomWord() + ".txt";
            File temp = new File(path);
            temp.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(path));
            int wordCount = WordWrapper.generateWordCount();
            for (int i = 0; i < wordCount; i++) {
                if (i % 17 == 0)
                    out.print(WordWrapper.getTypo());
                else
                    out.print(WordWrapper.getRandomWord());
            }
            out.close();
            return path;
        } catch (IOException e) {
            System.out.println("Create file error: 010");
        }
        return null;
    }

}
