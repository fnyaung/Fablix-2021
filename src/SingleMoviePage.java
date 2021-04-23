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

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-movie"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMoviePage extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


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
            String query =" select  " +
                    "a.Movie_ID, " +
                    "a.Title, " +
                    "a.Year, " +
                    "a.Director, " +
                    "a.Rating, " +
                    "a.Stars, " +
                    "a.Stars_Id, " +
                    "GROUP_CONCAT(genre.name) AS Genres " +
                    "from " +
                    "(select " +
                    "m.id as Movie_ID, " +
                    "m.title as Title, " +
                    "m.year as Year, " +
                    "m.director as Director, " +
                    "r.rating as Rating, " +
                    "GROUP_CONCAT(star.name) AS Stars, " +
                    "GROUP_CONCAT(star.id) AS Stars_ID " +
                    "from " +
                    "movies m, " +
                    "ratings r, " +
                    "( select  " +
                    "    s.name, " +
                    "    s.id, " +
                    "    sm.movieId " +
                    "  from  " +
                    "     stars s, " +
                    "     stars_in_movies sm " +
                    "   where  " +
                    "     s.id=sm.starId) star      " +
                    "where " +
                    "r.movieId = m.id and " +
                    "m.id = '"+ id + "' and " +
                    "m.id = star.movieId " +
                    "group by  " +
                    "m.id) a, " +
                    "( select  " +
                    "     g.name, " +
                    "     gm.movieId " +
                    "  from " +
                    "     genres g, " +
                    "     genres_in_movies gm " +
                    "  where  " +
                    "     g.id=gm.genreId ) genre " +
                    "where " +
                    "a.Movie_ID=genre.movieId and " +
                    "a.Movie_ID = '"+ id + "'" +
                    "group by a.Movie_ID, genre.movieId";

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
                String star_id = rs.getString("Stars_ID");
                String genres = rs.getString("Genres");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("genres", genres);

//                System.out.println("SQLresult: "+jsonArray.toString());

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
