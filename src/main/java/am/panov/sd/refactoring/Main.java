package am.panov.sd.refactoring;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.servlet.AddProductServlet;
import am.panov.sd.refactoring.servlet.GetProductsServlet;
import am.panov.sd.refactoring.servlet.QueryServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author akirakozov
 */
public class Main {
    private static final String DB_CONNECTION_URL = "jdbc:sqlite:test.db";

    public static void main(String[] args) throws Exception {
        DBDao dbDao = new DBDao(DB_CONNECTION_URL);

        dbDao.createTable();

        Server server = new Server(8082);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(dbDao)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(dbDao)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(dbDao)),"/query");

        server.start();
        server.join();
    }
}
