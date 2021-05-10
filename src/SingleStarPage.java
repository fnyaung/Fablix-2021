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

@WebServlet(name = "SingleStarPage", urlPatterns = "/api/single-star")
public class SingleStarPage extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 3L;

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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        response.setContentType("application/json"); // Response mime type

        String id = request.getParameter("id");
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try  {
            // // Get a connection from dataSource
            Connection conn = dataSource.getConnection();


            // query to get the 20
//            String query = "SELECT s.id as Star_ID, s.name as Star_Name, s.birthYear as Birth_Year, GROUP_CONCAT(DISTINCT m.id ORDER BY m.year DESC, m.title ASC) AS Movies_ID, GROUP_CONCAT(DISTINCT m.title ORDER BY m.year DESC, m.title ASC) AS Movies FROM movies m, stars s, stars_in_movies sm WHERE m.id = sm.movieID AND s.id = sm.starID AND s.id = '"+ id +"'GROUP BY s.id";
            String query =
                    "SELECT " +
                    "s.id as Star_ID, s.name as Star_Name, s.birthYear as Birth_Year, m.id as Movies_ID, m.title as Movies_Title " +
                    "FROM " +
                    "stars as s " +
                    "join stars_in_movies as sm on s.id = sm.starId " +
                    "join movies as m on sm.movieId = m.id " +
                    "WHERE s.id = ?" +
                    "ORDER by Movies_Title ASC; ";

            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,  id );

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            System.out.println(statement);;

            // Loop through the rows of rs
            // get it through until there is no more next
            while (rs.next()) {
                // id, title, year, director
                String star_id = rs.getString("Star_ID");
                String star_name = rs.getString("Star_Name");
                String birth_year = rs.getString("Birth_Year");
                String movies_id = rs.getString("Movies_ID");
                String movies_title = rs.getString("Movies_Title");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("birth_year", birth_year);
                jsonObject.addProperty("movie_id", movies_id);
                jsonObject.addProperty("movie_title", movies_title);

                jsonArray.add(jsonObject);
            }

            System.out.println(jsonArray.toString());
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

            // set respond status to 500 (Internal Server Error)
            response.setStatus(500);

        } finally {
            out.close();
        }


    }
}