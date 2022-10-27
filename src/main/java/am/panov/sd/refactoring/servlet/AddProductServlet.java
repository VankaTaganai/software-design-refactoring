package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    private final DBDao dbDao;

    public AddProductServlet(DBDao dbDao) {
        this.dbDao = dbDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));

        try {
            dbDao.insert(new Product(name, price));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpResponseUtils.writeResponse(response, Optional.empty(), List.of("OK"));
    }
}
