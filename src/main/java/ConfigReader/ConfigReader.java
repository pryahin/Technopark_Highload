package ConfigReader;

import HttpServer.*;
import Worker.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConfigReader {
    public static void read() throws IOException {
        FileInputStream fstream = new FileInputStream(HttpServer.CONFIG);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            final String[] conf = line.split(" ");
            if (conf[0].equals("listen")) {
                HttpServer.PORT = Integer.parseInt(conf[1]);
                System.out.println("Port: " + HttpServer.PORT);
            }
            if (conf[0].equals("cpu_limit")) {
                HttpServer.CPU = Integer.parseInt(conf[1]);
                System.out.println("max cpu: " + HttpServer.CPU);
            }
            if (conf[0].equals("thread_limit")) {
                HttpServer.THREADS = Integer.parseInt(conf[1]);
                System.out.println("max threads: " + HttpServer.THREADS);
            }
            if (conf[0].equals("document_root")) {
                Worker.root = conf[1];
                System.out.println("root: " + conf[1]);
            }
        }
        fstream.close();
        bufferedReader.close();
    }
}
