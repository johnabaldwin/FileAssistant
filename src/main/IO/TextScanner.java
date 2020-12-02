package IO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class TextScanner extends FastScanner{

    public TextScanner(String p) throws IOException {
        super(p);
    }

    @Override
    public Queue<String> getDoc() throws IOException {
        Charset s = Charset.forName("UTF-8");
        List<String> list = Files.readAllLines(file.toPath(), s);
        return new ArrayDeque<>(list);
    }
}
