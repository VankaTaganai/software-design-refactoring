package am.panov.sd.refactoring.servlet;

import am.panov.sd.refactoring.dao.DBDao;
import am.panov.sd.refactoring.models.Product;
import org.junit.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServletTest {

    private static final String CONNECTION_URL = "jdbc:sqlite:test.db";
    private static final Product HLEB = new Product("Hleb", 1000);
    private static final Product KVAS = new Product("Kvas", 2000);
    private static final Product SOL = new Product("Sol", 500);

    private static Connection connection;
    private static final DBDao dbDAO = new DBDao(CONNECTION_URL);

    private StringWriter stringWriter;
    private HttpServletRequest request;
    private HttpServletResponse response;


    @BeforeClass
    public static void beforeAll() {
        try {
            connection = DriverManager.getConnection(CONNECTION_URL);
            dbDAO.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void before() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        try {
            when(response.getWriter()).thenReturn(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM PRODUCT WHERE 1 = 1");

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void afterAll() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addElementToEmptyBase() throws IOException {
        when(request.getParameter("name")).thenReturn("hleb");
        when(request.getParameter("price")).thenReturn("1000");

        AddProductServlet addProductsServlet = new AddProductServlet(dbDAO);

        addProductsServlet.doGet(request, response);

        Assert.assertEquals(
                stringWriter.toString(),
                "<html><body>\n" +
                "OK\n" +
                "</body></html>\n"
        );
    }

    @Test
    public void getElementFromEmptyBase() throws IOException {
        GetProductsServlet getProductsServlet = new GetProductsServlet(dbDAO);

        getProductsServlet.doGet(request, response);

        Assert.assertEquals(
                stringWriter.toString(),
                "<html><body>\n" +
                        "</body></html>\n"
        );
    }

    @Test
    public void getElementFromNonEmptyBase() throws IOException, SQLException {
        GetProductsServlet getProductsServlet = new GetProductsServlet(dbDAO);

        fillDatabase();
        getProductsServlet.doGet(request, response);

        Assert.assertEquals(
                stringWriter.toString(),
                "<html><body>\n" +
                        "Hleb\t1000</br>\n" +
                        "Kvas\t2000</br>\n" +
                        "Sol\t500</br>\n" +
                        "</body></html>\n"
        );
    }

    @Test
    public void minElement() throws IOException, SQLException {
        queryTest(
                "min",
                "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "Sol\t500</br>\n" +
                        "</body></html>\n"
        );
    }

    @Test
    public void maxElement() throws IOException, SQLException {
        queryTest(
                "max",
                "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "Kvas\t2000</br>\n" +
                        "</body></html>\n"
        );
    }

    @Test
    public void sumElement() throws IOException, SQLException {
        queryTest(
                "sum",
                "<html><body>\n" +
                        "Summary price: \n" +
                        "3500\n" +
                        "</body></html>\n"
        );
    }

    @Test
    public void countElement() throws IOException, SQLException {
        queryTest(
                "count",
                "<html><body>\n" +
                        "Number of products: \n" +
                        "3\n" +
                        "</body></html>\n"
        );
    }

    private void queryTest(String command, String result) throws IOException, SQLException {
        QueryServlet queryServlet = new QueryServlet(dbDAO);

        when(request.getParameter("command")).thenReturn(command);

        fillDatabase();
        queryServlet.doGet(request, response);

        Assert.assertEquals(
                stringWriter.toString(),
                result
        );
    }

    private void fillDatabase() throws SQLException {
        Statement stmt = connection.createStatement();

        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + HLEB.name + "\"," + HLEB.price + ")," +
                                     "(\"" + KVAS.name + "\"," + KVAS.price + ")," +
                                     "(\"" + SOL.name  + "\"," + SOL.price +  ")";

        stmt.executeUpdate(sql);

        stmt.close();
    }
}
