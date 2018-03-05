package Response;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Response {
    private final String OK = "200 OK";
    private final String FORBIDDEN = "403 Forbidden";
    private final String NOT_FOUND = "404 Not Found";
    private final String NOT_ALLOWED = "405 Method Not Allowed";

    @NotNull
    private String template(String status, String version) {
        return version + " " + status + "\r\n" +
                "Date: " + new Date().toString() + "\r\n" +
                "Server: technopark-server \r\n" +
                "Connection: " + "Closed" + "\r\n\r\n";
    }

    @NotNull
    public String ok(int length, String contentType, String version) {
        final String text = this.template(OK, version);
        return text.substring(0, text.length() - 2) +
                "Content-Length: " + length + "\r\n" +
                "Content-type: " + contentType + "\r\n\r\n";
    }

    @NotNull
    public String forbidden(String version) {
        return this.template(FORBIDDEN, version);
    }

    @NotNull
    public String notFound(String version) {
        return this.template(NOT_FOUND, version);
    }

    @NotNull
    public String notAllowed(String version) {
        return this.template(NOT_ALLOWED, version);
    }
}
