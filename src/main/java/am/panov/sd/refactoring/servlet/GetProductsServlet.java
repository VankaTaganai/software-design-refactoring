package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        try {
            List<Product> products = dbDao.selectAll();

            response.getWriter().println("<html><body>");

            for (Product product : products) {
                response.getWriter().println(product.name + "\t" + product.price + "</br>");
            }

            response.getWriter().println("</body></html>");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
