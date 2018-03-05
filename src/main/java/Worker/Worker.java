package Worker;

import Response.Response;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

public class Worker implements Runnable {
    public static String root = "";
    private static final String indexFileName = "index.html";
    private final Response response = new Response();
    private static HashMap<String, String> typeFiles;

    private Socket socket;
    private BufferedReader in;
    private OutputStream raw;
    private Writer out;
    private String[] tokens;

    public Worker(Socket socket) {
        this.setTypes();

        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.raw = new BufferedOutputStream(this.socket.getOutputStream());
            this.out = new OutputStreamWriter(raw);
        } catch (IOException e) {
            System.out.println("problem with in/out");
        }
    }

    private void setTypes() {
        final HashMap<String, String> typeFiles = new HashMap<>();

        typeFiles.put("html", "text/html");
        typeFiles.put("css", "text/css");
        typeFiles.put("js", "text/javascript");
        typeFiles.put("html", "text/html");
        typeFiles.put("jpg", "image/jpeg");
        typeFiles.put("jpeg", "image/jpeg");
        typeFiles.put("png", "image/png");
        typeFiles.put("gif", "image/gif");
        typeFiles.put("swf", "application/x-shockwave-flash");

        Worker.typeFiles = typeFiles;
    }

    private File getFile(String fileName) {
        return new File(root + fileName);
    }

    private void sendFile(File file, String version) throws IOException {
        try (final InputStream ios = new FileInputStream(file)) {
            final byte[] buffer = new byte[1024];

            int read;
            while ((read = ios.read(buffer)) != -1) {
                raw.write(buffer, 0, read);
                raw.flush();
            }

        } catch (IOException e) {
            sendHeader(response.notFound(version));
        }
    }

    private void Response() throws IOException {
        final String method = tokens[0];
        final String filePath = tokens[1];
        final String version = tokens[2];

        if (method.toUpperCase().equals("GET") || method.toUpperCase().equals("HEAD")) {
            String fileName = URLDecoder.decode(filePath, "UTF-8");
            final int posQuestion = fileName.indexOf('?');
            if (posQuestion != -1) {
                fileName = fileName.substring(0, posQuestion);
            }
            final int posLastDot = fileName.lastIndexOf('.');

            String contentType;
            if (posLastDot != -1) {
                try {
                    contentType = typeFiles.get(fileName.substring(posLastDot + 1));
                } catch (Exception e) {
                    System.out.println("Unsupported type=" + fileName.substring(posLastDot + 1));
                    sendHeader(response.notAllowed(version));
                    return;
                }
            } else {
                if (fileName.endsWith("/")) {
                    fileName += indexFileName;
                    contentType = typeFiles.get("html");
                } else {
                    sendHeader(response.forbidden(version));
                    return;
                }
            }

            final File file = getFile(fileName);

            sendResponse(file, version, method, contentType);
        } else {
            sendHeader(response.notAllowed(version));
        }
    }

    private void sendResponse(File file, String version, String method, String contentType) throws IOException {
        if ((file.canRead()) && (file.getCanonicalPath().startsWith(root))) {
            sendHeader(response.ok((int) file.length(), contentType, version));
            if (method.toUpperCase().equals("GET")) {
                sendFile(file, version);
            }

        } else {
            sendHeader(response.notFound(version));
        }
    }

    private void parseRequest() throws IOException {
        this.tokens = requestToString().split("\\s+");
    }

    private String requestToString() throws IOException {
        final StringBuilder requestLine = new StringBuilder();

        while (true) {
            final String line = in.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            requestLine.append(line + "\n");
        }
        return requestLine.toString();
    }

    @Override
    public void run() {
        try {
            parseRequest();
            if (tokens.length >= 3) {
                Response();
            }
        } catch (Exception e) {
            System.out.println("catch in execute");
            e.printStackTrace();
        } finally {
            finish();
        }
    }

    private void finish() {
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("can not close");
            e.printStackTrace();
        }
    }

    private void sendHeader(String responseHeader) {
        try {
            out.write(responseHeader);
            out.flush();
        } catch (Exception e) {
            System.out.println("catch in sendHeader" + e.getMessage());
        }
    }
}