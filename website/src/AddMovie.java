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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.io.*;


@WebServlet(name = "AddMovie", urlPatterns = "/api/addmovie")
public class AddMovie extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 6L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            // change to masterdb
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/masterdb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // title year director starname genre from url request
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String starname = request.getParameter("starname");
        String genre = request.getParameter("genre");

        System.out.println(">>>> Add moive Parameter: " + title + year + director + starname + genre );

//        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();
//
//            add_movie( movie_title , m_year ,  m_director , s_name ,  g_name)
            String query = "call add_movie(?, ?, ?, ?, ?); ";

            PreparedStatement statement = conn.prepareStatement(query);

            System.out.println("statement");
            System.out.println(statement);

            // Declare our statement

            // replace ? in the query

            statement.setString(1, title);
            statement.setString(2, year);
            statement.setString(3, director);
            statement.setString(4, starname);
            statement.setString(5, genre);

            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();

            if(rs.next()) { // there is a result
                String answer = rs.getString("answer");
                jsonObject.addProperty("msg", answer);
            }

            jsonArray.add(jsonObject);


            rs.close();
            statement.close();
            conn.close();

            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
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