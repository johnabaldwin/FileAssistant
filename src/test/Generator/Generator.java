package test.Generator;

import java.util.HashSet;
import java.util.Scanner;

public class Generator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of text documents needed: ");
        int tdocs = in.nextInt();
        System.out.println("Enter the number of word documents needed: ");
        int wdocs = in.nextInt();
        HashSet<String> paths = new HashSet<String>();
        for (int i = 0; i < tdocs; i++) {
            String s = RandomText.generate();
            if (s != null)
                paths.add(s);
        }
        for (int i = 0; i < wdocs; i++) {
            String s = RandomWord.generate();
            if (s != null)
                paths.add(s);
        }
    }
}
