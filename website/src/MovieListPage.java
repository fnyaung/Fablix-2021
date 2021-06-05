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
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


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
        long TSstartTime = System.nanoTime();

        // retrieve title, year, director, and star from url request
        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");
        String cur_page = request.getParameter("page");
        String sort = request.getParameter("sort");

        //~~~~~~ New
        String limit = request.getParameter("limit");


//        System.out.println(">>>> Parameter: " + title + " " + year + " " + director + " " +star + " "+ genre +" " + cur_page + " " + sort + " " + limit);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            long TJstartTime = System.nanoTime();

            Connection conn = dataSource.getConnection();
            String query = "";
//            System.out.println("1~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//            System.out.println(title);
//            System.out.println(title.equals("*"));

            if (!title.equals("*")){
//                System.out.println("!! normal query");
                query = "select " +
                        "m.id, " +
                        "m.year, " +
                        "m.title, " +
                        "m.director, " +
                        "rim.rating as rating, " +
                        "substring_index(group_concat(distinct g.name ORDER BY g.name ASC), ',', 3 ) as genresName, " +
                        "substring_index(group_concat(distinct star.name order by star.name), ',', 3) as starsName, " +
                        "substring_index(group_concat(distinct star.id order by star.name), ',', 3) as starsId " +
                        ",count(*) OVER() AS Total "+
                        "from " +
                        "(select m.id as movieId, r.rating as rating from movies as m left join ratings as r on r.movieId = m.id) as rim, " +
                        "(select s.name, s.id, sm.movieId as movieId from stars s, stars_in_movies sm where s.id=sm.starId GROUP BY sm.starID ORDER BY count(sm.movieID) DESC, s.name ASC) as star,  " +
                        "movies as m, genres_in_movies as gm, genres as g " +
                        "where " +
                        "star.movieId = m.id and g.Id = gm.genreId and m.id = gm.movieId and rim.movieId=m.id ";
                if(title.length() != 0){
                    query += "and MATCH(title) AGAINST(? IN BOOLEAN MODE)";
                }
                query += " group by m.id " +
                        "having  (starsName like ? or starsName like ?)  " +
                        "and (director like ?) " +
                        "and (year like ?)  " +
                        "and (genresName like ? or ?)  " +
                        "order by %s " +
                        "limit ?, ? ";
            }
            else{
//                System.out.println("!! * * query ");
                query = "select " +
                        "m.id, m.year, " +
                        "m.title, " +
                        "m.director, " +
                        "rim.rating as rating, " +
                        "substring_index(group_concat(distinct g.name ORDER BY g.name ASC), ',', 3 ) as genresName, " +
                        "substring_index(group_concat(distinct star.name order by star.name), ',', 3) as starsName, " +
                        "substring_index(group_concat(distinct star.id order by star.name), ',', 3) as starsId " +
                        ",count(*) OVER() AS Total "+
                        "from " +
                        "(select m.id as movieId, r.rating as rating from movies as m left join ratings as r on r.movieId = m.id) as rim, " +
                        "(select s.name, s.id, sm.movieId as movieId from stars s, stars_in_movies sm where s.id=sm.starId GROUP BY sm.starID ORDER BY count(sm.movieID) DESC, s.name ASC) as star,  " +
                        "movies as m, genres_in_movies as gm, genres as g " +
                        "where " +
                        "star.movieId = m.id and g.Id = gm.genreId and m.id = gm.movieId and rim.movieId=m.id " +
                        "group by m.id " +
                        "having  title regexp '^[^a-zA-Z0-9]' " +
                        "order by %s " +
                        "limit ?, ? ";

            }

//            title starsname director year genresName
            if(sort.equals("TD")){
                // sort by Title
                query = String.format(query, "m.title DESC, r.rating DESC");
            }else if(sort.equals("TA")){
                query = String.format(query, "m.title ASC, r.rating DESC");
            }else if(sort.equals("RA")){
                query = String.format(query, "r.rating ASC, m.title DESC");
            }else{
                // default sort: Rating (RD)
                query = String.format(query, "r.rating DESC, m.title ASC");
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

            //~~~~~~ New
            if(limit == null){
                limit = "";
            }

            //~~~~~~ New
            int page_limit_int = 0;
            // set the next page to + limit
            int cur_page_int =  Integer.parseInt(cur_page) - 1;
            page_limit_int = cur_page_int + Integer.parseInt(limit);
//            System.out.println("~~~~~~~~~~~~~");
//            System.out.println(cur_page_int);
//            System.out.println(page_limit_int);

//            int page_limit_int = 0;
//            // set the next page to + 20
//            int cur_page_int =  Integer.parseInt(cur_page) - 1;
//            page_limit_int = cur_page_int+20;

            //            star title director year
            // star title year genre director genre
//            System.out.println(title + " " + year + " " + director + " " +star + " "+ genre +" " + cur_page_int + " " + page_limit_int);
            if (!title.equals("*")){
                if(title.length() != 0){
                    title = parseFullText(title);
                    statement.setString(1, title);
                    statement.setString(2, "%" + star + "%");
                    statement.setString(3, "%," + star + "%");
                    statement.setString(4, "%" + director + "%");
                    statement.setString(5, "%" + year + "%");
                    statement.setString(6, "%" + genre + "%");
                    statement.setString(7, "%," + genre + "%");

                    // page
                    statement.setInt(8, cur_page_int);
                    statement.setInt(9, page_limit_int);
                }else{
                    statement.setString(1, "%" + star + "%");
                    statement.setString(2, "%," + star + "%");
                    statement.setString(3, "%" + director + "%");
                    statement.setString(4, "%" + year + "%");
                    statement.setString(5, "%" + genre + "%");
                    statement.setString(6, "%," + genre + "%");

                    // page
                    statement.setInt(7, cur_page_int);
                    statement.setInt(8, page_limit_int);
                }

            }else{
                statement.setInt(1, cur_page_int);
                statement.setInt(2, page_limit_int);
            }

            // perform the query
            // return the result relation as rs.

//            System.out.println(statement);

            ResultSet rs = statement.executeQuery();
            long TJendTime = System.nanoTime();


//            System.out.println("2~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//            System.out.println("title: "+ title);
//            System.out.println("year: "+year);
//            System.out.println("director: "+director);
//            System.out.println("star: "+star);
//            System.out.println("genre: "+genre);
//            System.out.println("3~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
                String stars = rs.getString("starsName");
                String star_id = rs.getString("starsId");
                String genres = rs.getString("genresName");
                int total = rs.getInt("total");
                int no_of_page = 0;
                no_of_page = (int) Math.ceil(total / Double.parseDouble(limit));

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

            long TSendTime = System.nanoTime();
            long TSelapsedTime = (TSendTime - TSstartTime)/1000000;
            long TJelapsedTime = (TJendTime - TJstartTime)/1000000;

            String file_path = "/Users/hyejinkim/Desktop/logfile.txt";
//            String file_path = "/home/ubuntu/logfile.txt";


            try(FileWriter fw = new FileWriter(file_path, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw))
            {
                File myObj = new File(file_path);
                Scanner myReader = new Scanner(myObj);
                String last_line = null;
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    last_line = data;
                }

                if (last_line != null) {
                    System.out.println("If the string is not null");
                    List<String> strList = Arrays.asList(last_line.split(","));
                    TSelapsedTime += Long.parseLong(strList.get(0));
                    TJelapsedTime += Long.parseLong(strList.get(1));
                }

                pw.write(TSelapsedTime + "," + TJelapsedTime + "\n");
                System.out.println(TSelapsedTime + "," + TJelapsedTime);

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


            rs.close();
            statement.close();
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

    public static String parseFullText(String text) {
        String[] text_split = text.split("\\s+");
        String result = "";
        for(String word : text_split) {
            result += "+";
            result += word;
            result += "*";
            result += " ";
        }
        result.trim(); // remove trailing space at end
        return result;
    }
}