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

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {


            Statement statement = conn.createStatement();

            // query to get the 20
            String query = "select " +
                    " m.id," +
                    " m.title, " +
                    " m.year, " +
                    " m.director," +
                    " r.rating," +
                    " (select" +
                    "    istar.name" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 0,1) as star1," +
                    " (select" +
                    "    istar.name" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 1,1) as star2," +
                    " (select" +
                    "    istar.name" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 2,1) as star3," +
                    " (select" +
                    "    istar.id" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 0,1) as starid1," +
                    " (select" +
                    "    istar.id" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 1,1) as starid2," +
                    " (select" +
                    "    istar.id" +
                    "   from stars_in_movies isim, stars istar" +
                    "   where" +
                    "    isim.movieId=m.id and" +
                    "    istar.id=isim.starId" +
                    "    order by istar.id" +
                    "    limit 2,1) as starid3," +
                    "  (select " +
                    "   ig.name " +
                    "  from genres_in_movies igim, genres ig" +
                    "  where" +
                    "   igim.movieId=m.id and" +
                    "   ig.id=igim.genreId" +
                    "  order by ig.id" +
                    "   limit 0,1) as genre1," +
                    " (select " +
                    "   ig.name " +
                    "  from genres_in_movies igim, genres ig" +
                    "  where" +
                    "   igim.movieId=m.id and" +
                    "   ig.id=igim.genreId" +
                    "   order by ig.id" +
                    "   limit 1,1) as genre2," +
                    " (select " +
                    "   ig.name " +
                    "  from genres_in_movies igim, genres ig" +
                    "  where" +
                    "   igim.movieId=m.id and" +
                    "   ig.id=igim.genreId" +
                    "   order by ig.id" +
                    "   limit 2,1) as genre3" +
                    " from movies m, ratings r" +
                    " where m.id=r.movieId" +
                    " order by r.rating" +
                    " desc limit 20";

            // perform the query
            // return the result relation as rs.
            ResultSet rs = statement.executeQuery(query);

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
                String star1 = rs.getString("star1");
                String star2 = rs.getString("star2");
                String star3 = rs.getString("star3");
                String starid1 = rs.getString("starid1");
                String starid2 = rs.getString("starid2");
                String starid3 = rs.getString("starid3");
                String genre1 = rs.getString("genre1");
                String genre2 = rs.getString("genre2");
                String genre3 = rs.getString("genre3");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("star1", star1);
                jsonObject.addProperty("star2", star2);
                jsonObject.addProperty("star3", star3);
                jsonObject.addProperty("starid1", starid1);
                jsonObject.addProperty("starid2", starid2);
                jsonObject.addProperty("starid3", starid3);
                jsonObject.addProperty("genre1", genre1);
                jsonObject.addProperty("genre2", genre2);
                jsonObject.addProperty("genre3", genre3);


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