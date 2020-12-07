package main.IO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class TextScanner extends FastScanner{

    /**
     * Constructor for passing the {@code File} to be read
     * @param p - the file to be read
     * @throws IOException - IOExceptions are possible if the file does not exist
     */
    public TextScanner(File p) throws IOException {
        super(p.getAbsolutePath());
    }

    /**
     * Constructor for passing the file path of the file to be read
     * @param p - path of the file to be read
     * @throws IOException - IOExceptions are possible if the file does not exist
     */
    public TextScanner(String p) throws IOException {
        super(p);
    }

    /**
     * Returns the entire text document as a {@code Queue} given line by line
     * @return - the entire text document as a {@code Queue}
     * @throws IOException - reading from files is likely to cause IOExceptions
     */
    @Override
    public Queue<String> getDoc() throws IOException {
        Charset s = StandardCharsets.UTF_8;
        List<String> list = Files.readAllLines(file.toPath(), s);
        return new ArrayDeque<>(list);
    }

    /**
     * Deprecated method for text documents
     * @return - {@code null} rather than newRun
     */
    @Override
    @Deprecated
    public String getNewRun() {
        return null;
    }

    /**
     * Deprecated method for text documents
     * @param words - the {@code ArrayDeque} containing words to write to the file
     */
    @Override
    @Deprecated
    public void write(ArrayDeque<String> words) {
    }
}
