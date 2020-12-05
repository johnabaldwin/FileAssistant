package main.IO;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class TextScanner extends FastScanner{

    public TextScanner(String p) throws IOException {
        super(p);
    }

    public TextScanner(File p) throws IOException {
        super(p.getAbsolutePath());
    }

    public TextScanner(File file, HashMap<String, String> replacements) throws IOException {
        super(file.getAbsolutePath());
        replace(replacements);
    }

    private void replace(HashMap<String, String> findAndReplace) throws IOException {
        ArrayDeque<String> document = (ArrayDeque<String>) getDoc();
        Set<String> keys = findAndReplace.keySet();
        PrintWriter writer = new PrintWriter(new FileWriter(new File(path)));
        while (!document.isEmpty()) {
            String cur = document.getFirst();
            if (cur.equals("NEWLINE")) {
                writer.println();
                continue;
            }
            if (keys.contains(cur)) {
                cur = findAndReplace.get(cur);
            }
            writer.print(cur);
        }
    }

    @Override
    public Queue<String> getDoc() throws IOException {
        Charset s = Charset.forName("UTF-8");
        List<String> list = Files.readAllLines(file.toPath(), s);
        return new ArrayDeque<>(list);
    }

    @Override
    @Deprecated
    public String getNewRun() {
        return null;
    }

    @Override
    @Deprecated
    public void write(ArrayDeque<String> words) throws IOException {
    }
}
