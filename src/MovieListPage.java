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


@WebServlet(name = "MovieListPage", urlPatterns = "/api/movie-list")
public class MovieListPage extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 1L;

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
        // title year director star

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {

            System.out.println("1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            Statement statement = conn.createStatement();
            // if == search
            // if == browse
            //
            // query to get the 20

            String query = "select " +
                    "m.id, " +
                    "m.title," +
                    "m.year," +
                    "m.director," +
                    "r.rating," +
                    "substring_index(group_concat(DISTINCT g.name ORDER BY g.id separator ','), ',', 3) as genresName," +
                    "substring_index(group_concat(DISTINCT s.name ORDER BY s.id separator ','), ',', 3) as starsName," +
                    "substring_index(group_concat(DISTINCT s.id separator ','), ',', 3) as starsId" +
                    " from " +
                    "movies as m " +
                    "left outer join " +
                    "  stars_in_movies as sm on m.id = sm.movieId " +
                    "left outer join " +
                    "  stars as s on s.id = sm. starId" +
                    " left outer join " +
                    " ratings as r on m.id = r.movieId" +
                    " left outer join " +
                    "  genres_in_movies as gm on m.id = gm.movieId " +
                    "left outer join " +
                    "  genres as g on g.id = gm.genreId " +
                    "group by id " +
                    "order by " +
                    "r.rating DESC "+
                    "limit 20";
            // perform the query
            // return the result relation as rs.
            ResultSet rs = statement.executeQuery(query);

            String title = request.getParameter("title");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String star = request.getParameter("star");

            System.out.println("2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            String location = request.getParameter("location");
            out.println("<p>location : "+location+"<p");
            System.out.println("location: "+ location);
            out.println("<p>title : "+title+"<p");
            System.out.println("title: "+ title);
            System.out.println("year: "+year);
            System.out.println("director: "+director);
            System.out.println("star: "+star);
            System.out.println("3~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            JsonArray jsonArray = new JsonArray();

            // Loop through the rows of rs
            // get it through until there is not next
            while (rs.next()) {
                // id, title, year, director
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");
                String genres = rs.getString("genresName");
                String stars = rs.getString("starsName");
                String star_id = rs.getString("starsId");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("genres", genres);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("star_id", star_id);

                jsonArray.add(jsonObject);
            }

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

        }
        out.close();

    }
}