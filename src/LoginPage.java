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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jasypt.util.password.StrongPasswordEncryptor;


@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginPage extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Retrieve parameter id from url request.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String location = request.getParameter("location");

        // get recaptcha
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

        JsonObject responseJsonObject = new JsonObject();

        // Verify reCAPTCHA
        try {
            System.out.println("!!!");
            RecaptchaVerifyUtils.verify(gRecaptchaResponse);


            System.out.println("username: " + username);
            System.out.println("password: " + password);
            System.out.println("location: " + location);


//            JsonObject responseJsonObject = new JsonObject();

            try {
                String query;
                Connection dbcon = dataSource.getConnection();
//            query = "SELECT * FROM customers where email = ? ";

                if(location.equals("customer")){
                    // user
                    System.out.println("Customer");
                    query = "SELECT * FROM customers where email = ? ";
                }
                else{
                    // staff
                    System.out.println("Employee");
                    query = "SELECT * FROM employees where email = ? ";
                }

                // Declare our statement
                PreparedStatement statement = dbcon.prepareStatement(query);
                // Replace ? with username
                statement.setString(1,username);

                ResultSet rs = statement.executeQuery();

                System.out.println("statement");
                System.out.println(statement);
//            /*
                if(rs.next()) { // there is a result
                    System.out.println("rs.next()");

                    String encryptedPassword = rs.getString("password");
                    boolean success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);

                    System.out.println("Password success? : " + success);

                    if (success){
                        System.out.println("Inside the if statement : getting user into the session");

                        // set this user into the session
                        User user = new User(username);
                        request.getSession().setAttribute("user", user);

                        if(location.equals("customer")){
                            // set user with its corresponding ID
                            System.out.println(rs.getInt("id"));
                            user.setUserID(rs.getInt("id"));
                        }

                        // set the user with its user type
//                    user.setUserType(location);

                        responseJsonObject.addProperty("status", "success");
                        responseJsonObject.addProperty("usertype", location);
                        responseJsonObject.addProperty("message", "success");

                        System.out.println(responseJsonObject);
                    }
                    else{
                        System.out.println("Inside the else statement : getting user into the session");
                        responseJsonObject.addProperty("status", "fail");
                        if(!success) {
                            responseJsonObject.addProperty("message", "incorrect password");
                        }
                    }


                } else { // there is no result
                    // Login fail
                    responseJsonObject.addProperty("status", "fail");

                    // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.

                    if (!rs.next()) {
                        responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
                    }

                }
                response.getWriter().write(responseJsonObject.toString());
                response.setStatus(200);

                // Closing after opening
                rs.close();
                statement.close();
                dbcon.close();

            } catch (Exception e) {
                System.out.println("Error 1");
                // write error message JSON object to output
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("errorMessage", e.getMessage());

                // set reponse status to 500 (Internal Server Error)
                response.setStatus(500);
            }

        } catch (Exception e) {
            System.out.println("Error 2");

            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "reCAPTCHA fail");

            response.getWriter().write(responseJsonObject.toString());
            response.setStatus(200);


            // set reponse status to 500 (Internal Server Error)
//            response.setStatus(500);
        }

    }
}
