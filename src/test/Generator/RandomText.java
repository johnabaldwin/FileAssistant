package test.Generator;

import java.io.*;

/**
 * Class to generate new text documents filled with random words and typos
 */
public class RandomText {

    /**
     * Generates a file with a random name, word count, and typos
     * @return the path to this file
     */
    public static String generate() {
        try {
            String path = WordWrapper.getRandomWord() + ".txt";
            File temp = new File(path);
            temp.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(path));
            int wordCount = WordWrapper.generateWordCount();
            for (int i = 0; i < wordCount; i++) {
                if (i % 17 == 0)
                    out.print(WordWrapper.getTypo() + " ");
                else
                    out.print(WordWrapper.getRandomWord() + " ");

                if (WordWrapper.randomBoolean())
                    out.println();
            }
            out.close();
            return path;
        } catch (IOException e) {
            System.err.println("Create file error: 010");
        }
        return null;
    }

}
