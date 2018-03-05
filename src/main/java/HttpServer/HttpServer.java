package HttpServer;

import ConfigReader.*;
import ThreadPool.*;
import Worker.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static int CPU = 4;
    public static int PORT = 80;
    public static int THREADS = 256;
    public static String CONFIG = "/etc/httpd.conf";

    public static void main(String[] args) throws IOException {
        ConfigReader.read();
        ConfigReader.print();
        ServerSocket serverSocket = new ServerSocket(PORT);
        final ThreadPool threadPool = new ThreadPool(THREADS);

        while(true) {
            Socket socket = serverSocket.accept();
            Worker worker = new Worker(socket);
            threadPool.execute(worker);
        }
    }
}
