package am.panov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class HttpResponseUtils {

    public static void writeResponse(HttpServletResponse response, Optional<String> header, List<String> body) throws IOException {
        PrintWriter writer = response.getWriter();

        writer.println("<html><body>");
        header.ifPresent(h -> writer.println("<h1>" + h + "</h1>"));
        body.forEach(writer::println);
        writer.println("</body></html>");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
