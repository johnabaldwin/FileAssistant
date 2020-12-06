package test.Generator;

import main.Language.ActionController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class Generator {

    public static int EXIT = 0;

    public static void main(String[] args) throws IOException {
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
        int next = in.nextInt();
        while (next != EXIT) {
            switch (next) {
                case 1:
                    ActionController.spellCheck("test.docx");
                    break;
                case 2:
                    ActionController.fixWhiteSpace("text.docx");
                    break;
            }
            next = in.nextInt();

        }
    }
}
