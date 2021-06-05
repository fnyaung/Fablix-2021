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


@WebServlet(name = "AddStar", urlPatterns = "/api/addstar")
public class AddStar extends HttpServlet {
    // The serialVersionUID is a universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly
    // to a serialized object. If no match is found, then an InvalidClassException is thrown.
    private static final long serialVersionUID = 5L;

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

        // retrieve title, year, director, and star from url request
        String starname = request.getParameter("starname");
        String birthyear = request.getParameter("birthyear");

        System.out.println(">>>> Add star Parameter: " + starname + " " + birthyear );

//        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            Connection conn = dataSource.getConnection();

            System.out.println("Getting the max id ...");
            String id_query = "select max(id) as id from stars;";

            PreparedStatement statement = conn.prepareStatement(id_query);
            ResultSet rs = statement.executeQuery();

            System.out.println("statement");
            System.out.println(statement);
            String id = "";

            if(rs.next()) { // there is a result
                //get the result id
                id = rs.getString("id");
            }
            String num_after = id.substring(2);
            int num_id = Integer.parseInt(num_after) + 1;
            String new_id = id.substring(0, 2) + String.valueOf(num_id);

            String insert_query;
            System.out.println(birthyear);
            if (birthyear == null){
                // no birth year
                System.out.println("No birth year");
                insert_query = "INSERT INTO stars (id, name) VALUES(?,?) ;";
            }
            else{
                // yes birth year
                System.out.println("Yes birth year");
                insert_query = "INSERT INTO stars (id, name, birthYear) VALUES(?, ?, ?); ";
            }

            // Declare our statement
            PreparedStatement insert_statement = conn.prepareStatement(insert_query);

            if(starname == null){
                starname = "";
            }
            if(birthyear == null){
                birthyear = "";
            }

            // replace ? in the query
            if (birthyear == null){
                //no birthyear
                insert_statement.setString(1, new_id);
                insert_statement.setString(2, starname);
            }
            else{
                // yes birth year
                insert_statement.setString(1, new_id);
                insert_statement.setString(2, starname);
                insert_statement.setInt(3, Integer.parseInt(birthyear));
            }

            // perform the query
            // return the rows affected
            int row = insert_statement.executeUpdate();

            System.out.println("Number of rows affected: " + row);

            JsonArray jsonArray = new JsonArray();

            // Loop through the rows of rs
            // get it through until there is not next
            if (row > 0) {
                JsonObject jsonObject = new JsonObject();
                if(birthyear != null){
                    jsonObject.addProperty("status", "sucess");
                    jsonObject.addProperty("new_id", new_id);
                    jsonObject.addProperty("starname", starname);
                    jsonObject.addProperty("birthyear", birthyear);
                }
                else{
                    jsonObject.addProperty("status", "fail");
                    jsonObject.addProperty("new_id", new_id);
                    jsonObject.addProperty("starname", starname);
                }

                jsonArray.add(jsonObject);
            }
            
            rs.close();
            statement.close();
            insert_statement.close();
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