package IO;

import java.io.*;
import java.util.Queue;
import java.util.StringTokenizer;

public abstract class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        protected final String path;

        protected final String fileName;

        protected final File file;

        public FastScanner(String p) throws IOException {
            path = p;
            file = new File(p);
            fileName = file.getName();
            br = new BufferedReader(new FileReader(p));
            st = new StringTokenizer("");
        }

        public String next() throws IOException {
            if(st.hasMoreTokens())
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

        public void close() throws IOException {
            br.close();
        }

        public abstract Queue<String> getDoc() throws IOException;
}
