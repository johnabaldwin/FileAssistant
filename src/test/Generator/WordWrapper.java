package test.Generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class WordWrapper {

    private static HashMap<Integer, String> words = new HashMap<>();
    private static int WORD_COUNT = 0;
    private static Random random = new Random(System.currentTimeMillis());
    static {
        try {
            Scanner in = new Scanner(new File("words.txt"));
            int count = 0;
            while (in.hasNextLine()) {
                words.put(count, in.nextLine());
                count++;
            }
            WORD_COUNT = count + 1;
        } catch (IOException e) {
            System.out.println("File read error: 001");
            WORD_COUNT = 0;
        }
    }

    private WordWrapper() {}

    public static int getWordCount() {
        return WORD_COUNT;
    }

    public static String getRandomWord() {
        int word = random.nextInt(WORD_COUNT);
        return words.get(word);
    }

    public static int generateWordCount() {
        return random.nextInt(WORD_COUNT * 2);
    }

    public static String getTypo() {
        String word = words.get(random.nextInt(WORD_COUNT));
        int typo = random.nextInt(word.length());
        char c = (char)(random.nextInt(26) + 'a');
        word = word.substring(0, typo) + c + word.substring(typo + 1);
        System.out.println(word);
        return word;
    }

}
