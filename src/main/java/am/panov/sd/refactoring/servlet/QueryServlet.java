package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        try {
            if ("max".equals(command)) {
                Optional<Product> maxRes = dbDao.max();

                List<String> body = maxRes.stream()
                        .map(product -> product.name + "\t" + product.price + "</br>")
                        .collect(Collectors.toList());

                HttpResponseUtils.writeResponse(response, Optional.of("Product with max price: "), body);
            } else if ("min".equals(command)) {
                Optional<Product> minRes = dbDao.min();

                List<String> body = minRes.stream()
                        .map(product -> product.name + "\t" + product.price + "</br>")
                        .collect(Collectors.toList());

                HttpResponseUtils.writeResponse(response, Optional.of("Product with min price: "), body);
            } else if ("sum".equals(command)) {
                int sum = dbDao.sum();

                HttpResponseUtils.writeResponse(response, Optional.empty(), List.of("Summary price: ", Integer.toString(sum)));
            } else if ("count".equals(command)) {
                int count = dbDao.count();

                HttpResponseUtils.writeResponse(response, Optional.empty(), List.of("Number of products: ", Integer.toString(count)));

            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
