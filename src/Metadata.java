import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import java.io.*;


@WebServlet(name = "Metadata", urlPatterns = "/api/metadata")
public class Metadata extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 7L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type


        System.out.println(">>>> metadata");

//        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();

            // Declare statement
            Statement statement = conn.createStatement();

            System.out.println(conn.getCatalog());

            // https://www.codejava.net/java-se/jdbc/how-to-read-database-meta-data-in-jdbc
            DatabaseMetaData md = conn.getMetaData();

            String[] types = {"TABLE"};

            ResultSet rstables = md.getTables(null, null, null, types);

            JsonArray jsonArray = new JsonArray();
            System.out.println(rstables.getRow());

            System.out.println("Starting . .. ");

            while (rstables.next()){
                String tableName = rstables.getString(3);
                if(tableName.equals("sys_config")){
                    continue;
                }

                // each obj add the table name
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("tableName", tableName);


                System.out.println("\n+++ Table Name : "+ tableName); // get the table name

                ResultSet rsCol = md.getColumns(null, "moviedb", tableName, null);

                String element_list = "";

                while (rsCol.next()) {

                    String colName = rsCol.getString("COLUMN_NAME");
                    String colType = rsCol.getString("TYPE_NAME");
                    element_list += colName + "&" + colType + "&";

                    System.out.println("\t" + colName + " - " + colType);
                }
                jsonObject.addProperty("element", element_list);

                rsCol.close();
                jsonArray.add(jsonObject);
            }
            System.out.println(jsonArray);
            out.write(jsonArray.toString());

            rstables.close();

            response.setStatus(200);

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set respond status to 500 (Internal Server Error)
            response.setStatus(500);

        } finally {
            out.close();
        }

    }
}