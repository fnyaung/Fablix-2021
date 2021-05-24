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
        System.out.println(">>>> Single Movie Servelt");
        System.out.println(id);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection conn = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query = "select   " +
                    "m.id,   " +
                    "m.title,  " +
                    "m.year,  " +
                    "m.director,  " +
                    "r.rating,  " +
                    "substring_index(group_concat(DISTINCT g.name ORDER BY g.id separator ','), ',', 3) as genresName,  " +
                    "substring_index(group_concat(DISTINCT s.name ORDER BY s.id separator ','), ',', 3) as starsName,  " +
                    "substring_index(group_concat(DISTINCT s.id separator ','), ',', 3) as starsId  " +
                    "from   " +
                    "movies as m   " +
                    "left outer join   " +
                    "  stars_in_movies as sm on m.id = sm.movieId   " +
                    "left outer join   " +
                    "  stars as s on s.id = sm. starId  " +
                    "left outer join   " +
                    " ratings as r on m.id = r.movieId  " +
                    " left outer join   " +
                    "  genres_in_movies as gm on m.id = gm.movieId   " +
                    "left outer join   " +
                    "  genres as g on g.id = gm.genreId  " +
                    "where " +
                    "m.id = ?" +
                    "group by id ";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,  id );

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            System.out.println(statement);

            // Iterate through each row of rs
            while (rs.next()) {

                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String movie_rating = rs.getString("rating");
                String stars = rs.getString("starsName");
                String star_id = rs.getString("starsId");
                String genres = rs.getString("genresName");


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("star_id", star_id);
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
            conn.close();

        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set reponse status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }


    }

}

//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.servlet.ServletConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-movie"
//@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
//public class SingleMoviePage extends HttpServlet {
//    private static final long serialVersionUID = 2L;
//
//    // Create a dataSource which registered in web.xml
//    private DataSource dataSource;
//
//    public void init(ServletConfig config) {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//     *      response)
//     */
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//
//        response.setContentType("application/json"); // Response mime type
//        System.out.println("Single Movie.. ");
//
//        // Retrieve parameter id from url request.
//        String id = request.getParameter("id");
//        System.out.println(id);
//
//        // Output stream to STDOUT
//        PrintWriter out = response.getWriter();
//
//        try {
//            // Get a connection from dataSource
//            Connection conn = dataSource.getConnection();
//
//            // Construct a query with parameter represented by "?"
//            String query = "select  m.id,  m.title, m.year, m.director, r.rating,  " +
//                    "substring_index(group_yconcat(DISTINCT g.name ORDER BY g.id separator ','), ',', 3) as genresName,  " +
//                    "substring_index(group_concat(DISTINCT s.name ORDER BY s.id separator ','), ',', 3) as starsName,  " +
//                    "substring_index(group_concat(DISTINCT s.id separator ','), ',', 3) as starsId  " +
//                    "from  movies as m   " +
//                    "left outer join stars_in_movies as sm on m.id = sm.movieId   " +
//                    "left outer join    stars as s on s.id = sm. starId  " +
//                    "left outer join   ratings as r on m.id = r.movieId   " +
//                    "left outer join    genres_in_movies as gm on m.id = gm.movieId   " +
//                    "left outer join    genres as g on g.id = gm.genreId    " +
//                    "where m.id = ? ";
//
//            // Declare our statement
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setString(1,  id );
//
//            // Perform the query
//            ResultSet rs = statement.executeQuery();
//
//            JsonArray jsonArray = new JsonArray();
//
//            System.out.println(statement);
//
//            // Iterate through each row of rs
//            while (rs.next()) {
//
//                String movie_id = rs.getString("id");
//                String movie_title = rs.getString("title");
//                String movie_year = rs.getString("year");
//                String movie_director = rs.getString("director");
//                String movie_rating = rs.getString("rating");
//                String stars = rs.getString("starsName");
//                String star_id = rs.getString("starsId");
//                String genres = rs.getString("genresName");
//
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("movie_id", movie_id);
//                jsonObject.addProperty("movie_title", movie_title);
//                jsonObject.addProperty("movie_year", movie_year);
//                jsonObject.addProperty("movie_director", movie_director);
//                jsonObject.addProperty("movie_rating", movie_rating);
//                jsonObject.addProperty("stars", stars);
//                jsonObject.addProperty("star_id", star_id);
//                jsonObject.addProperty("genres", genres);
//
//                jsonArray.add(jsonObject);
//            }
//
//            System.out.println("SQLresult: "+jsonArray.toString());
//
//            // write JSON string to output
//            out.write(jsonArray.toString());
//            // set response status to 200 (OK)
//            response.setStatus(200);
//
//            rs.close();
//            statement.close();
//            conn.close();
//
//        } catch (Exception e) {
//            // write error message JSON object to output
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("errorMessage", e.getMessage());
//            out.write(jsonObject.toString());
//
//            // set reponse status to 500 (Internal Server Error)
//            response.setStatus(500);
//        } finally {
//            out.close();
//        }
//
//
//    }
//
//}
