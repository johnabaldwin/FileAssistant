package main.IO;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public abstract class FastScanner {
    BufferedReader br;
    StringTokenizer st;

    protected final String path;

    protected final String fileName;

    protected final File file;

    protected final String extension;

    protected final String newParagraph = "¬ ¬ ¬ NP  ¬ ¬ ¬";

    public FastScanner(String p) throws IOException {
        path = p;
        file = new File(p);
        fileName = file.getName();
        extension = p.substring(p.lastIndexOf('.'));
        br = new BufferedReader(new FileReader(p));
        st = new StringTokenizer("");
    }

    public String next() throws IOException {
        if (st.hasMoreTokens())
            return st.nextToken();
        else
            st = new StringTokenizer(br.readLine());
        return next();
    }

    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }

    public boolean hasNext() {
        boolean result = false;

        try {
            result = br.ready();
        } catch (IOException e) {
            System.err.println("Buffered Reader not ready");
        }
        return result;
    }

    public void close() throws IOException {
        br.close();
    }

    public String getPath() {
        return path;
    }

    public abstract Queue<String> getDoc() throws IOException;

    public String getExtension() {
        return extension;
    }

    public String getNewParagraph() { return  newParagraph; }

    public abstract String getNewRun();

    public abstract void write(ArrayDeque<String> words) throws IOException;
}
