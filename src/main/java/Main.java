import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static int CPU = 4;
    private static int PORT = 80;
    private static int THREADS = 256;
    private static String CONFIG = "/Users/vova/httpServer/httpd.conf";

    public static void configReader() throws IOException {
        FileInputStream fstream = new FileInputStream(CONFIG);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            final String[] conf = line.split(" ");
            if (conf[0].equals("listen")) {
                PORT = Integer.parseInt(conf[1]);
                System.out.println("Port: " + PORT);
            }
            if (conf[0].equals("cpu_limit")) {
                CPU = Integer.parseInt(conf[1]);
                System.out.println("max cpu: " + CPU);
            }
            if (conf[0].equals("thread_limit")) {
                THREADS = Integer.parseInt(conf[1]);
                System.out.println("max threads: " + THREADS);
            }
            if (conf[0].equals("document_root")) {
                System.out.println("root: " + conf[1]);
            }
        }
        fstream.close();
        bufferedReader.close();
    }

    public static void main(String[] args) throws IOException {
        configReader();
    }
}
