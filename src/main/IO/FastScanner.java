package main.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public abstract class FastScanner {

    /**
     * Buffered reader for faster file reading
     */
    protected BufferedReader br;

    /**
     * String tokenizer to parse words from Buffered Reader line read
     */
    protected StringTokenizer st;

    /**
     * Extension of file to be read
     */
    protected final String extension;

    /**
     * File to be read
     */
    protected final File file;

    /**
     * Name of file to be read
     */
    protected final String fileName;

    /**
     * Path of file to be read
     */
    protected final String path;

    /**
     * New paragraph delimiter for parsing and writing text from files
     */
    protected final String newParagraph = "¬ ¬ ¬ NP  ¬ ¬ ¬";

    /**
     * Constructor for new {@code FastScanner} object, will instantiate all necessary variables
     * @param p - file path of file to be read
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public FastScanner(String p) throws IOException {
        path = p;
        file = new File(p);
        fileName = file.getName();
        extension = p.substring(p.lastIndexOf('.'));
        br = new BufferedReader(new FileReader(p));
        st = new StringTokenizer("");
    }

    /**
     * Closes the FastScanner and all associated objects
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public void close() throws IOException {
        br.close();
    }

    /**
     * Abstract method for reading every word in a fle into a {@code Queue}
     * @return - a {@code Queue} containing every word in the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public abstract Queue<String> getDoc() throws IOException;

    /**
     * Returns the extension of the file being read
     * @return - the extension of the file being read
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Returns the {@code newParagraph} delimiter
     * @return - the {@code newParagraph} delimiter
     */
    public String getNewParagraph() { return  newParagraph; }

    /**
     * Abstract method for Word documents to return the newRun delimiter
     * @return - the newRun delimiter
     */
    public abstract String getNewRun();

    /**
     * Returns the path of the file being read
     * @return - the path of the file being read
     */
    public String getPath() {
        return path;
    }

    /**
     * Checks for another line in the buffered reader
     * @return - whether or not there is another line in the buffered reader
     */
    public boolean hasNext() {
        boolean result = false;

        try {
            result = br.ready();
        } catch (IOException e) {
            System.err.println("Buffered Reader not ready");
        }
        return result;
    }

    /**
     * Reads the next word available in this file
     * @return - the next word from the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public String next() throws IOException {
        //Reads the next available token from the string tokenizer, if none, gets a new line
        if (st.hasMoreTokens())
            return st.nextToken();
        else
            st = new StringTokenizer(br.readLine());
        return next();
    }

    /**
     * Returns the next double from the file if available, throws an error if not
     * @return - the next double from the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }

    /**
     * Returns the next integer from the file if available, throws an error if not
     * @return - the next integer from the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    /**
     * Returns the next long from the file if available, throws an error if not
     * @return - the next long from the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public long nextLong() throws IOException {
        return Long.parseLong(next());
    }

    /**
     * Abstract method for writing all words in an {@code ArrayDeque} to a file
     * @param words - the {@code ArrayDeque} containing words to write to the file
     * @throws IOException - reading files is likely to throw various {@code IOException}'s
     */
    public abstract void write(ArrayDeque<String> words) throws IOException;
}
