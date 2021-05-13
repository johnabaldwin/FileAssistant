package main.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;

public final class Search {

    /**
     * Current users home directory path
     */
    private static final File HOME_DIRECTORY = new File(System.getProperty("user.home"));

    /**
     * List of results from the search
     */
    private static final ArrayList<String> results = new ArrayList<>();

    /**
     * Private constructor prevents object creation of static class
     */
    private Search() {}

    /**
     * Method intended to search all files and sub-files in this users {@code HOME_DIRECTORY}.
     * The file name is guaranteed to be of a specific file type or have a wildcard file extension.
     * @param fileNames - The names of the files that are being searched for.
     * @throws IOException - searching for files is likely to throw {@code IOException}'s
     */
    public static void findFileSet(ArrayList<String> fileNames) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("files.out")));
        results.clear();
        for (String fileName : fileNames)
            search(writer, fileName, HOME_DIRECTORY);
        writer.close();
    }

    /**
     * Method for finding the first instance of a given file name in the users {@code HOME_DIRECTORY}
     * @param fileName - The name of the file to search for
     * @throws IOException - searching for files is likely to throw {@code IOException}'s
     */
    public static void findFirst(final String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("files2.out")));
        results.clear();
        ArrayDeque<File> queue = new ArrayDeque<>();
        queue.add(HOME_DIRECTORY);

        //BFS on all directories starting from this users {@code HOME_DIRECTORY}
        while(!queue.isEmpty()) {
            File currentDir = queue.removeFirst();
            if (currentDir.isFile()) {
                continue;
            }
            File[] filesInDir = currentDir.listFiles();
            if (isNull(filesInDir)) {
                continue;
            }

            for(File file : filesInDir) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    results.add(file.getPath());
                    writer.println(file.getPath());
                    writer.close();
                    return;
                }
                if (file.isDirectory()) {
                    queue.addLast(file);
                }
            }
        }
        writer.close();
    }

    /**
     * Method intended to search all files and sub-files in this users {@code HOME_DIRECTORY}.
     * The file name is guaranteed to be of a specific file type or have a wildcard file extension.
     * @param fileName - The name of the file that is being searched for.
     * @throws IOException - searching for files is likely to throw {@code IOException}'s
     */
    public static void findSingleFile(final String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("files.out")));
        results.clear();
        search(writer, fileName, HOME_DIRECTORY);
        writer.close();
    }

    /**
     * Gets the results list of the most recently performed search
     * @return - the results list of the most recently performed search
     */
    public static ArrayList<String> getResults() { return results; }

    public static String getHome() {
        return HOME_DIRECTORY.getPath();
    }

    /**
     * Method for checking if {@code checkNull} is null
     * @param checkNull - Object to check if is null
     * @return - Returns a boolean stating whether the object passed in is null
     */
    private static boolean isNull(Object checkNull) {
        return checkNull == null;
    }

    /**
     * Private method for performing DFS style search on file system
     * @param writer - PrintWriter to write found file paths to output
     * @param fileName - File name to search for
     * @param currentDir - Current working directory
     */
    private static void search(final PrintWriter writer, final String fileName, File currentDir) {
        File[] filesInDir = currentDir.listFiles();
        if (isNull(filesInDir)) {
            return;
        }

        //DFS on all directories in {@code currentDir}
        for (File checkFile : filesInDir) {
            if (checkFile.getName().equalsIgnoreCase(fileName)) {
                results.add(checkFile.getPath());
                writer.println(checkFile.getPath());
            } else if (checkFile.isDirectory() && checkFile.exists()) {
                search(writer, fileName, checkFile);
            }
        }
    }
}
