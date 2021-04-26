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

        response.setContentType("application/json"); // Response mime type

        // retrieve title, year, director, and star from url request
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");
        String cur_page = request.getParameter("page");
        String sort = request.getParameter("sort");

//        String page_limit_str = Integer.toString(page_limit);

        System.out.println(">>>> Parameter: " + title + " " + year + " " + director + " " +star + " "+ genre +" " + cur_page + " " + sort);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try (Connection conn = dataSource.getConnection()) {

            System.out.println("1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            String query = "select   " +
                    "a.Movie_ID,  " +
                    "a.Title,  " +
                    "a.Year,  " +
                    "a.Director,  " +
                    "a.Rating,  " +
                    "a.Stars,  " +
                    "a.Stars_Id,  " +
                    "GROUP_CONCAT(genre.name ORDER BY genre.name ASC) AS Genres  " +
                    ",count(*) OVER() AS Total " + // to count the total
                    "from  " +
                    "(select  " +
                    "m.id as Movie_ID,  " +
                    "m.title as Title,  " +
                    "m.year as Year,  " +
                    "m.director as Director,  " +
                    "r.rating as Rating,  " +
                    "GROUP_CONCAT(star.name) AS Stars,  " +
                    "GROUP_CONCAT(star.id) AS Stars_ID  " +
                    "from  " +
                    "movies m,  " +
                    "ratings r,  " +
                    "( select   " +
                    "    s.name,  " +
                    "    s.id,  " +
                    "    sm.movieId  " +
                    "  from   " +
                    "     stars s,  " +
                    "     stars_in_movies sm  " +
                    "   where   " +
                    "     s.id=sm.starId" +
                    "   group by sm.starID" +
                    "   order by count(sm.movieID) DESC, s.name ASC) star       " +
                    "where  " +
                    "r.movieId = m.id and  " +
                    "m.id = star.movieId  " +
                    "group by   " +
                    "m.id) a,  " +
                    "( select   " +
                    "     g.name,  " +
                    "     gm.movieId  " +
                    "  from  " +
                    "     genres g,  " +
                    "     genres_in_movies gm  " +
                    "  where   " +
                    "     g.id=gm.genreId " +
                    "     ) genre  " +
                    "where " +
                    "a.Movie_ID=genre.movieId  " +
                    "and (Stars like ? or Stars like ?) " +
                    "and (Title like ? or Title like ?) " +
                    "and (Director like ?) " +
                    "and (Year like ?) " +
                    "group by a.Movie_ID, genre.movieId " +
                    "having (Genres like ? or Genres like ?) " +
                    "order by %s" +
//                    "limit 20";
                    " limit ?, ?";

            if(sort.equals("T")){
                // sort by Title
                query = String.format(query, "a.Title ASC, a.Rating DESC");
            }else{
                // default sort: Rating
                query = String.format(query, "a.Rating DESC, a.Title ASC");
            }

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // set parameter represented by "?" in the query
            if(title == null){
                title = "";
            }
            if(star == null){
                star = "";
            }
            if(year == null){
                year = "";
            }
            if(director == null){
                director = "";
            }
            if(genre == null){
                genre = "";
            }
            if(cur_page == null){
                cur_page ="";
            }
            int page_limit_int = 0;
            // set the next page to + 20
            int cur_page_int =  Integer.parseInt(cur_page) - 1;
            page_limit_int = cur_page_int+20;


            System.out.println(title + " " + year + " " + director + " " +star + " "+ genre +" " + cur_page_int + " " + page_limit_int);
            statement.setString(1, "%," + star + "%");
            statement.setString(2, star + "%");
            statement.setString(3, "%" + title + "%");
            statement.setString(4, title + "%");
            statement.setString(5, "%" + director + "%");
            statement.setString(6, "%" + year + "%");
            statement.setString(7, "%," + genre + "%");
            statement.setString(8, genre + "%");
            // page
            statement.setInt(9, cur_page_int);
            statement.setInt(10, page_limit_int);

            // perform the query
            // return the result relation as rs.

            System.out.println(statement);

            ResultSet rs = statement.executeQuery();


            System.out.println("2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("title: "+ title);
            System.out.println("year: "+year);
            System.out.println("director: "+director);
            System.out.println("star: "+star);
            System.out.println("genre: "+genre);
            System.out.println("3~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
                String star_id = rs.getString("Stars_ID");
                String genres = rs.getString("Genres");
                int total = rs.getInt("Total");
                int no_of_page = 0;
//                System.out.println("~~total: "+ total);
                no_of_page = total / 20;
//                System.out.println("~~total: "+ no_of_page);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movie_id);
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("movie_rating", movie_rating);
                jsonObject.addProperty("stars", stars);
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("genres", genres);
                jsonObject.addProperty("no_of_page", no_of_page);

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