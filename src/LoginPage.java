import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginPage extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        JsonObject responseJsonObject = new JsonObject();
        if (username.equals("anteater") && password.equals("123456")) {
            // Login success:

            // set this user into the session
            request.getSession().setAttribute("user", new User(username));

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

        } else {
            // Login fail
            responseJsonObject.addProperty("status", "fail");

            // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
            if (!username.equals("anteater")) {
                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            } else {
                responseJsonObject.addProperty("message", "incorrect password");
            }
        }
        response.getWriter().write(responseJsonObject.toString());
    }
}

//
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
//@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
//public class LoginPage extends HttpServlet {
//    private static final long serialVersionUID = 3L;
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
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//     */
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("application/json"); // Response mime type
//
//        // Retrieve parameter id from url request.
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        System.out.println(" " + username + " " + password);
//
//
//        try {
//            Connection dbcon = dataSource.getConnection();
//            String query = String.format("SELECT * FROM customers where email = ? and password = ?;");
//            // Declare our statement
//            PreparedStatement statement = dbcon.prepareStatement(query);
//            // Replace ? with username
//            statement.setString(1,username);
//            statement.setString(1,password);
//
//            // if i get result - > pass
//            // else -> no
//            // Perform the query
//            ResultSet rs = statement.executeQuery();
//
//            JsonObject responseJsonObject = new JsonObject();
//            while (rs.next()) {
//                String authPassword = rs.getString("password");
//                System.out.println(authPassword);
//
//                if (password.equals(authPassword)) {
//                    // Login success:
//                    // set this user into the session
//                    System.out.println("Password correct! ...");
//                    request.getSession().setAttribute("user", new User(username));
//                    responseJsonObject.addProperty("status", "success");
//                    responseJsonObject.addProperty("message", "success");
//                } else {
//                    // Login fail
//                    responseJsonObject.addProperty("status", "fail");
//
//                    // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.
//                    if (!username.equals("anteater")) {
//                        responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
//                    } else {
//                        responseJsonObject.addProperty("message", "incorrect password");
//                    }
//                }
//
//            }
//
//            response.getWriter().write(responseJsonObject.toString());
//            response.setStatus(200);
//
//        } catch (Exception e) {
//            // write error message JSON object to output
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("errorMessage", e.getMessage());
//
//            // set reponse status to 500 (Internal Server Error)
//            response.setStatus(500);
//        }
//
//    }
//}
