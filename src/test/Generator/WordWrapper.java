package test.Generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Class with various functions to help create new random files
 */
public class WordWrapper {

    /**
     * Hashmap to help randomly generate words by assigning each word a number
     */
    private static HashMap<Integer, String> words = new HashMap<>();

    /**
     * Number of words in the dictionary
     */
    private static int WORD_COUNT = 0;

    /**
     * Used for randomly generating numbers
     */
    private static Random random = new Random(System.currentTimeMillis());

    //Fills hashmap with words from dictionary
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
            System.err.println("File read error: 001");
            WORD_COUNT = 0;
        }
    }

    private WordWrapper() {}

    /**
     * @return the number of words in the dictionary
     */
    public static int getWordCount() {
        return WORD_COUNT;
    }

    /**
     * @return a random integer between 0 and the size of the dictionary modulo 23
     */
    public static int generateParagraphCount() { return random.nextInt(WORD_COUNT % 23); }

    /**
     * @return a random integer between 0 and the size of the dictionary modulo 599
     */
    public static int generateRunLength() { return random.nextInt(WORD_COUNT % 599); }

    /**
     * @return a random integer between 0 and twice the size of the dictionary
     */
    public static int generateWordCount() {
        return random.nextInt(WORD_COUNT * 2);
    }

    /**
     * Returns a word at random
     * @return {@code String} representing a random word from the dictionary
     */
    public static String getRandomWord() {
        int word = random.nextInt(WORD_COUNT);
        return words.get(word);
    }

    /**
     * @return a random word from the dictionary with a typo in a random position
     */
    public static String getTypo() {
        String word = words.get(random.nextInt(WORD_COUNT));
        int typo = random.nextInt(word.length());
        char c = (char)(random.nextInt(26) + 'a');
        word = word.substring(0, typo) + c + word.substring(typo + 1);
        return word;
    }

    /**
     * @return a boolean with a heavy bias towards being false
     */
    public static boolean randomBoolean() {
        if (Math.random() < 0.2) {
            return true;
        }
        return false;
    }

}
