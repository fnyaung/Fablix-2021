//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.servlet.ServletConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpSession;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.HashMap;
//import java.time.LocalDate;
//import java.util.*;
//
//@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
//public class Payment extends HttpServlet{
//    private static final long serialVersionUID = 4L;
//
//    // Create a dataSource which registered in web.xml
//    private DataSource dataSource;
//
//    public void init(ServletConfig config){
//        try{
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
//        }catch(NamingException e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//     */
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
//
//        // Retrieve cookies
//        Cookie cookie = null;
//        Cookie[] cookies = null;
//
//        // Get an array of Cookies associated with this domain
//        cookies = request.getCookies();
//        String cartValue = "";
//        if(cookies != null){
//            System.out.println("--Cookies found!--");
//
//            for(int i = 0; i< cookies.length; i++){
//                cookie = cookies[i];
//                if(cookie.getName().equals("movie_ids")){
//                    cartValue = cookie.getValue();
//                }
////                System.out.println("Name:" + cookie.getName() + ",");
////                System.out.println("Value:" + cookie.getValue() + ",");
////                System.out.println("Domain:" + cookie.getDomain() + ",");
////                System.out.println("MaxAge:" + cookie.getMaxAge() + ",");
////                System.out.println("MaxPath:" + cookie.getPath() + ",");
//            }
//        }else{
//            System.out.println("--NO Cookies found!--");
//        }
//
//        System.out.println(">>>> Cart Value: " + cartValue);
//
//        // Retrieve parameter fname, lname, cardnumber. and expDate from url request
//        String fname = request.getParameter("fname");
//        String lname = request.getParameter("lname");
//        String expDate = request.getParameter("expDate");
//        String cardnumber = request.getParameter("cardnumber");
//
//        System.out.println(fname + " " + lname + " " + cardnumber + " " + expDate);
//
//        JsonObject responseJsonObject = new JsonObject();
//
//        try {
//            Connection conn = dataSource.getConnection();
//            String query = "SELECT * FROM creditcards WHERE firstName = ? and LastName = ? and expiration = ? and id = ?";
//            // Declare our statement
//            PreparedStatement statement = conn.prepareStatement(query);
//            // Replace ? in the query
//            statement.setString(1, fname);
//            statement.setString(2, lname);
//            statement.setString(3, expDate);
//            statement.setString(4, cardnumber);
//
//            ResultSet rs = statement.executeQuery();
//
//            if(rs.next()){ // there is a result
//                // successfully found user's payment info
//                System.out.println("~~~ Credit Card ACCEPTED ~~~");
//                responseJsonObject.addProperty("status", "success");
//                responseJsonObject.addProperty("message", "Payment Success: Enjoy your movies!");
//
//                // parsing and evaluating cart values + putting into database
//                String[] cart_movies = cartValue.split("&.&");
//                // -Key: Movie_id&&Movie Title -Value: Quantity
//                HashMap<String, Integer> cartValue_dict = new HashMap<>();
//                // store cart movie and its quantity in a dictionary
//                for(String movie : cart_movies){
//                    // if the movie already exist add +1 to quantity
//                    if(cartValue_dict.containsKey(movie)){
//                        cartValue_dict.put(movie, cartValue_dict.get(movie)+1);
//                    }else{
//                        // create a quantity of 1 for new movie
//                        cartValue_dict.put(movie, 1);
//                    }
//                }
//
//                // get current highest saleID
//                String getMaxSaleID_query = "SELECT MAX(s.id) AS maxID FROM sales as s";
//                rs = statement.executeQuery(getMaxSaleID_query);
//                rs.next();
//                int max_saleID = rs.getInt("maxID");
//
//                HttpSession session = request.getSession();
//                int customerId = ((User) session.getAttribute("user")).getUserID();
//                LocalDate saleDate = LocalDate.now();
//
//                String movieSaleID = "";
//
//                // insert user's movie purchase into sales database
//                for(String movieID : cartValue_dict.keySet()){
//                    int quantity = cartValue_dict.get(movieID);
//
//                    for(int i=0; i<quantity; i++){
//                        max_saleID += 1;
//                        String queryInsert = "INSERT INTO sales(id, customerID, movieId, saleDate) "
//                                + "VALUES (" + max_saleID + "," + customerId + ", '" + movieID + "', '" + saleDate +"')";
//                        System.out.println("Inserted: " + max_saleID + " " + customerId + " " + movieID + " " + saleDate);
//
//                        movieSaleID += movieID + "&&" + max_saleID + "&.&";
//
//                        System.out.println(">>> movieSaleID = " + movieSaleID);
//
//                        int count = statement.executeUpdate(queryInsert);
//                        System.out.println("Execute Update Count = " + count);
//                    }
//                }
//
//                System.out.println("~~~~  Final MovieSaleID = " + movieSaleID);
//                responseJsonObject.addProperty("movieSaleID", movieSaleID);
//            }else{ // there is no result
//                // failed the find user's payment info
//                System.out.println("~~~ Credit Card FAILED ~~~");
//                responseJsonObject.addProperty("status", "fail");
//                responseJsonObject.addProperty("message", "Payment Failed: Re-enter payment info");
//            }
//            response.getWriter().write(responseJsonObject.toString());
//
//            // Closing after opening
//            rs.close();
//            statement.close();
//            conn.close();
//
//        }catch (Exception e) {
//            // write error message JSON object to output
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("errorMessage", e.getMessage());
//
//            // set response status to 500 (Internal Server Error)
//            response.setStatus(500);
//        }
//    }
//
//}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//not working but needed to replace to prepared statement
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.*;

@WebServlet(name = "PaymentServlet", urlPatterns = "/api/payment")
public class Payment extends HttpServlet{
    private static final long serialVersionUID = 4L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            // change to masterdb
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/masterdb");
        }catch(NamingException e){
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{

        // Retrieve cookies
        Cookie cookie = null;
        Cookie[] cookies = null;

        // Get an array of Cookies associated with this domain
        cookies = request.getCookies();
        String cartValue = "";
        if(cookies != null){
            System.out.println("--Cookies found!--");

            for(int i = 0; i< cookies.length; i++){
                cookie = cookies[i];
                if(cookie.getName().equals("movie_ids")){
                    cartValue = cookie.getValue();
                }
//                System.out.println("Name:" + cookie.getName() + ",");
//                System.out.println("Value:" + cookie.getValue() + ",");
//                System.out.println("Domain:" + cookie.getDomain() + ",");
//                System.out.println("MaxAge:" + cookie.getMaxAge() + ",");
//                System.out.println("MaxPath:" + cookie.getPath() + ",");
            }
        }else{
            System.out.println("--NO Cookies found!--");
        }

        System.out.println(">>>> Cart Value: " + cartValue);

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
                System.out.println("~~~ Credit Card ACCEPTED ~~~");
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "Payment Success: Enjoy your movies!");

                // parsing and evaluating cart values + putting into database
                String[] cart_movies = cartValue.split("&.&");
                // -Key: Movie_id&&Movie Title -Value: Quantity
                HashMap<String, Integer> cartValue_dict = new HashMap<>();
                // store cart movie and its quantity in a dictionary
                for(String movie : cart_movies){
                    // if the movie already exist add +1 to quantity
                    if(cartValue_dict.containsKey(movie)){
                        cartValue_dict.put(movie, cartValue_dict.get(movie)+1);
                    }else{
                        // create a quantity of 1 for new movie
                        cartValue_dict.put(movie, 1);
                    }
                }

                // get current highest saleID
                String getMaxSaleID_query = "SELECT MAX(s.id) AS maxID FROM sales as s";
                rs = statement.executeQuery(getMaxSaleID_query);
                rs.next();
                int max_saleID = rs.getInt("maxID");

                HttpSession session = request.getSession();
                int customerId = ((User) session.getAttribute("user")).getUserID();
                LocalDate saleDate = LocalDate.now();

                String movieSaleID = "";

                // insert user's movie purchase into sales database
                for(String movieID : cartValue_dict.keySet()){
                    int quantity = cartValue_dict.get(movieID);

                    System.out.println("insert user's movie purchase into sales database");

                    for(int i=0; i<quantity; i++){
                        System.out.println("For Loop inside...");
                        max_saleID += 1;
//                        String queryInsert = "INSERT INTO sales(id, customerID, movieId, saleDate) "
//                                + "VALUES (" + max_saleID + "," + customerId + ", '" + movieID + "', '" + saleDate +"')";

                        // change to prepared statement
                        String queryInsert = "INSERT INTO sales(id, customerID, movieId, saleDate) VALUES (?, ?, ?, ?) ";

                        statement = dbcon.prepareStatement(queryInsert);

                        statement.setInt(1, max_saleID);
                        statement.setInt(2, customerId);
                        statement.setString(3, movieID);
                        statement.setString(4, String.valueOf(saleDate));

                        System.out.println(statement);

                        System.out.println("Inserted: " + max_saleID + " " + customerId + " " + movieID + " " + saleDate);

                        movieSaleID += movieID + "&&" + max_saleID + "&.&";

                        System.out.println(">>> movieSaleID = " + movieSaleID);

                        int count = statement.executeUpdate();
                        System.out.println("Execute Update Count = " + count);
                    }
                }

                System.out.println("~~~~  Final MovieSaleID = " + movieSaleID);
                responseJsonObject.addProperty("movieSaleID", movieSaleID);
            }else{ // there is no result
                // failed the find user's payment info
                System.out.println("~~~ Credit Card FAILED ~~~");
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "Payment Failed: Re-enter payment info");
            }
            response.getWriter().write(responseJsonObject.toString());


            // Closing after opening
            rs.close();
            statement.close();
            dbcon.close();

        }catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        }
    }

}
