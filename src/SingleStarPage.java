import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
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

@WebServlet(name = "SingleStarPage", urlPatterns = "/api/single-star")
public class SingleStarPage extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 3L;

    // create a db according to registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        String id = request.getParameter("id");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // query to get the 20
            String query = "SELECT s.id as Star_ID, s.name as Star_Name, s.birthYear as Birth_Year, GROUP_CONCAT(DISTINCT m.id) AS Movies_ID, GROUP_CONCAT(DISTINCT m.title) AS Movies FROM movies m, stars s, stars_in_movies sm WHERE m.id = sm.movieID AND s.id = sm.starID AND s.id = '"+ id +"'GROUP BY s.id";


            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            System.out.println("------- Star's ID is " + id);
//            statement.setString(1, id);

            System.out.println("--- Query: >>" + query + "<<");
            // perform the query
            // return the result relation as rs.
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Loop through the rows of rs
            // get it through until there is no more next
            while (rs.next()) {
                // id, title, year, director
                String star_id = rs.getString("Star_ID");
                String star_name = rs.getString("Star_Name");
                String birth_year = rs.getString("Birth_Year");
                String movies_id = rs.getString("Movies_ID");
                String movies_list = rs.getString("Movies");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("birth_year", birth_year);
                jsonObject.addProperty("movies_id", movies_id);
                jsonObject.addProperty("movies_list", movies_list);

                jsonArray.add(jsonObject);
            }
            System.out.println(jsonArray.toString());
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set respond status to 500 (Internal Server Error)
            response.setStatus(500);

        }
        out.close();

    }
}