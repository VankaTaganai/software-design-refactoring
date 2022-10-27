package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final DBDao dbDao;

    public GetProductsServlet(DBDao dbDao) {
        this.dbDao = dbDao;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> body;

        try {
            List<Product> products = dbDao.selectAll();

            body = products.stream()
                    .map(product -> product.name + "\t" + product.price + "</br>")
                    .collect(Collectors.toList());

            HttpResponseUtils.writeResponse(response, Optional.empty(), body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
