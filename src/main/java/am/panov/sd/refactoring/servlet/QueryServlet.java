package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final DBDao dbDao;

    public QueryServlet(DBDao dbDao) {
        this.dbDao = dbDao;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                Optional<Product> maxRes = dbDao.max();

                PrintWriter writer = response.getWriter();

                writer.println("<html><body>");
                writer.println("<h1>Product with max price: </h1>");

                maxRes.ifPresent(product -> writer.println(product.name + "\t" + product.price + "</br>"));

                writer.println("</body></html>");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                Optional<Product> minRes = dbDao.min();

                PrintWriter writer = response.getWriter();

                writer.println("<html><body>");
                writer.println("<h1>Product with min price: </h1>");

                minRes.ifPresent(product -> writer.println(product.name + "\t" + product.price + "</br>"));

                writer.println("</body></html>");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                int sum = dbDao.sum();

                PrintWriter writer = response.getWriter();

                writer.println("<html><body>");
                writer.println("Summary price: ");
                writer.println(sum);
                writer.println("</body></html>");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                int count = dbDao.count();

                PrintWriter writer = response.getWriter();

                writer.println("<html><body>");
                writer.println("Number of products: ");
                writer.println(count);
                writer.println("</body></html>");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
