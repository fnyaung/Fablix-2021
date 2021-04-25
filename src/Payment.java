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

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class Payment extends HttpServlet{
    private static final long serialVersionUID = 4L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        }catch(NamingException e){
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        // Retrieve parameter fname, lname, cardnumber. and expDate from url request
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String expDate = request.getParameter("expDate");
        String cardnumber = request.getParameter("cardnumber");

        System.out.println(fname + " " + lname + " " + cardnumber + " " + expDate);

        JsonObject responseJsonObject = new JsonObject();

        try{
            Connection dbcon = dataSource.getConnection();
            String query = "SELECT * FROM creditcards WHERE firstName = ? and LastName = ? and expiration = ? and id = ?";
            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);
            // Replace ? in the query
            statement.setString(1, fname);
            statement.setString(2, lname);
            statement.setString(3, expDate);
            statement.setString(4, cardnumber);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){ // there is a result
                // successfully found user's payment info
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "Payment Success: Enjoy your movies!");
            }else{ // there is no result
                // failed the find user's payment info
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Payment Failed: Re-enter payment info");
            }
            response.getWriter().write(responseJsonObject.toString());
        }catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }

}
