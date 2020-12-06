package main.IO;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class TextScanner extends FastScanner{

    public TextScanner(String p) throws IOException {
        super(p);
    }

    public TextScanner(File p) throws IOException {
        super(p.getAbsolutePath());
    }

    @Override
    public Queue<String> getDoc() throws IOException {
        Charset s = StandardCharsets.UTF_8;
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
    public void write(ArrayDeque<String> words) {
    }
}
