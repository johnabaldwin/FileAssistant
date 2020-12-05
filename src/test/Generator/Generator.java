package test.Generator;

import java.util.Scanner;

public class Generator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of text documents needed: ");
        int tdocs = in.nextInt();
        System.out.println("Enter the number of word documents needed: ");
        int wdocs = in.nextInt();
        for (int i = 0; i < tdocs; i++) {
            RandomText.generate();
        }
        for (int i = 0; i < wdocs; i++) {
            RandomWord.generate();
        }
    }
}
