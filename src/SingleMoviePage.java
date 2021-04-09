import java.io.*;

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

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMoviePage extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedbexample")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");
        System.out.println(id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "select" +
                    "  m.id as Movie_ID," +
                    "  m.title as Title," +
                    "  m.year as Year," +
                    "  m.director as Director," +
                    "  r.rating as Rating," +
                    "  GROUP_CONCAT(s.id) AS Stars_ID," +
                    "  GROUP_CONCAT(DISTINCT g.name) AS Genres," +
                    "  GROUP_CONCAT(s.name) AS Stars" +
                    " from" +
                    "  movies m," +
                    "  ratings r," +
                    "  stars s," +
                    "  stars_in_movies sm," +
                    "  genres_in_movies gm," +
                    "  genres g" +
                    " where" +
                    "  m.id = \""+id+"\"" +
                    "  AND m.id = r.movieID" +
                    "  AND m.id = sm.movieID" +
                    "  AND m.id = gm.movieID" +
                    "  AND s.id = sm.starID" +
                    "  AND g.id = gm.genreID" +
                    " GROUP BY" +
                    "  m.title";


            System.out.println("------- Movie's ID is " + id);

            System.out.println("--- 1Query: >>" + query + "<<");

            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
//            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                // id, title, year, director
                String movie_id = rs.getString("Movie_ID");
                String movie_title = rs.getString("Title");
                String movie_year = rs.getString("Year");
                String movie_director = rs.getString("Director");
                String movie_rating = rs.getString("Rating");
                String stars = rs.getString("Stars");
                String starids = rs.getString("Stars_ID");
                String genres = rs.getString("Genres");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("starids", starids);
                jsonObject.addProperty("genres", genres);

                jsonArray.add(jsonObject);
            }

            System.out.println("SQLresult: "+jsonArray.toString());

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

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        }
        out.close();

    }

}
