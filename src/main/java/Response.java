import org.jetbrains.annotations.NotNull;
import java.util.Date;

public class Response {
    private final String OK = "200 OK";
    private final String FORBIDDEN = "403 Forbidden";
    private final String NOT_FOUND = "404 Not Found";
    private final String NOT_ALLOWED = "405 Method Not Allowed";

    @NotNull
    private String template(String status) {
        return "HTTP/1.1 " + status + "\r\n" +
                "Date: " + new Date().toString() + "\r\n" +
                "Server: Pryahin \r\n"+
                "Connection: " + "Closed" + "\r\n\r\n";
    }

    @NotNull
    public String ok(int length, String contentType) {
        final String text = this.template(OK);
        return text.substring(0, text.length()-2) +
                "Content-Length: " + length + "\r\n" +
                "Content-type: " + contentType + "\r\n\r\n";
    }

    @NotNull
    public String forbidden() {
        return this.template(FORBIDDEN);
    }

    @NotNull
    public String notFound() {
        return this.template(NOT_FOUND);
    }

    @NotNull
    public String notAllowed() {
        return this.template(NOT_ALLOWED);
    }
}
