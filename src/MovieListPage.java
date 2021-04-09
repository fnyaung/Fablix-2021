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

@WebServlet(name = "MovieListPage", urlPatterns = "/api/movie-list")
public class MovieListPage extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 1L;

    // create a db according to registered in web.xml
    @Resource(name = "jdbc/moviedbexample")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            Statement statement = dbcon.createStatement();

            // query to get the 20
            String query = "SELECT" +
                    "  m.id as Movie_ID," +
                    "  m.title as Title," +
                    "  m.year as Year," +
                    "  m.director as Director," +
                    "  r.rating as Rating," +
                    "  substring_index(group_concat(s.id SEPARATOR ','), ',', 3) as Stars_ID," +
                    "  substring_index(group_concat(DISTINCT g.name SEPARATOR ','), ',', 3) as Genres," +
                    "  substring_index(group_concat(DISTINCT s.name SEPARATOR ','), ',', 3) as Stars" +
                    " FROM" +
                    "  movies m," +
                    "  ratings r," +
                    "  stars s," +
                    "  stars_in_movies sm," +
                    "  genres_in_movies gm," +
                    "  genres g" +
                    " WHERE" +
                    "  m.id = r.movieID" +
                    "  AND m.id = sm.movieID" +
                    "  AND m.id = gm.movieID" +
                    "  AND s.id = sm.starID" +
                    "  AND g.id = gm.genreID" +
                    " GROUP BY" +
                    "  m.title" +
                    " ORDER BY" +
                    "  r.rating DESC" +
                    " limit" +
                    "  20";

            // perform the query
            // return the result relation as rs.
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Loop through the rows of rs
            // get it through until there is not next
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