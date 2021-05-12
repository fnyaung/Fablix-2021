import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import java.io.FileWriter;
/*

Note: Please change the username, password and the name of the datbase.

*/

public class BatchInsert {

    public static void main (String[] args) throws Exception, InstantiationException, IllegalAccessException, ClassNotFoundException{
        HashMap<String, List<DirectorFilms>> directorFilms;
        HashMap<String, Integer> actors;
        final long startTime = System.currentTimeMillis();

        MainsSaxParser msp = new MainsSaxParser();
        msp.runMainSAX();
        directorFilms = msp.getMainsData();

        ActorSAXParser asp = new ActorSAXParser();
        asp.runActorSAX();
        actors = asp.getActors();

        CastSAXParser csp = new CastSAXParser(directorFilms, actors);
        csp.runCastSAX();
        directorFilms = csp.getCastData();


        // Print out inconsistencies and duplicates
        try{
            FileWriter myWriterInc = new FileWriter("Inconsistencies.txt");
            FileWriter myWriterDup = new FileWriter("Duplicates.txt");

            Iterator<String> incM = msp.getInconsistencies().iterator();
            myWriterInc.write("Inconsistencies in mains243.xml: \n");
            while(incM.hasNext()){
                myWriterInc.write(incM.next() + "\n");
            }

            System.out.println("\n");
            // Inconsistencies and Duplicates from Cast124.xml
            Iterator<String> incC = csp.getInconsistencies().iterator();
            myWriterInc.write("Inconsistencies in Cast124.xml: \n");
            while(incC.hasNext()){
                myWriterInc.write(incC.next() + "\n");
            }

            Iterator<String> incA = asp.getInconsistencies().iterator();
            myWriterInc.write("Inconsistencies in actor63.xml: \n");
            while(incA.hasNext()){
                myWriterInc.write(incA.next() + "\n");
            }

            System.out.println("\n");

            Iterator<String> dup = csp.getDuplicates().iterator();
            myWriterDup.write("Duplicates in Cast124.xml: \n");
            while(dup.hasNext()){
                myWriterDup.write(dup.next() + "\n");
            }

            myWriterInc.close();
            myWriterDup.close();
        }catch(IOException e){
            System.out.println("Writing Failed!");
        }

        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedbex";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb_example";

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

        Iterator df = directorFilms.entrySet().iterator();

        /// query statements
        PreparedStatement movie_statement=null;
        PreparedStatement wy_statement=null;
        PreparedStatement woy_statement=null;


        int[] movieRows=null;
        int[] wyRows=null;
        int[] woyRows=null;

        String movie_query=null;
        String wy_query=null;
        String woy_query=null;

        movie_query="call add_movie_parse(?, ?, ?, ?, ?); ";

//        wy_query="call add_star_wy(?, ?, ?); ";
//        woy_query="call add_star_woy(?, ?); ";

        wy_query="call add_star_wy_new(?, ?, ?, ?); ";
        woy_query="call add_star_woy_new(?, ?, ?); ";

        movie_statement=conn.prepareStatement(movie_query);
        wy_statement=conn.prepareStatement(wy_query);
        woy_statement=conn.prepareStatement(woy_query);
        /// query statements


//        int movie_count = 0;
//        int wy_count = 0;
//        int woy_count = 0;
        int count =0;

        while(df.hasNext()){
            Map.Entry mapElement = (Map.Entry) df.next();
            List<DirectorFilms> curr_df = (List<DirectorFilms>) mapElement.getValue();
            Iterator<DirectorFilms> cdf = curr_df.iterator();

//            System.out.println("~~~~~~~~~~~~~~~1 many directors ~~~~~~~~~~~~~~~");

            while(cdf.hasNext()){

                // According to one director
//                System.out.println("~~~~~~~~~~~~~~~2 one director ~~~~~~~~~~~~~~~");

                DirectorFilms cur_directorFilm = cdf.next();
                // !!!! Movie Director name:
                String m_director = cur_directorFilm.getDirectorName(); // director name


                HashMap<String, Film> currFilmHash = cur_directorFilm.getfilmHash(); // fid : Film obj
                Iterator fi = currFilmHash.entrySet().iterator(); // iterating through films hash table

                conn.setAutoCommit(false);

                // director has many films
//                System.out.println("~~~~~~~~~~~~~~~3 many films from one director~~~~~~~~~~~~~~~");

                while(fi.hasNext()){
//                    System.out.println("~~~~~~~~~~~~~~~4 one film from one director~~~~~~~~~~~~~~~");
                    // each movie happens
                    Map.Entry me = (Map.Entry) fi.next();
                    Film curr_film = (Film) me.getValue();

                    // One movie
                    String movie_id = curr_film.getfId();
                    String movie_title = curr_film.getfTitle();
                    Integer m_year = curr_film.getfYear();
                    String g_name = curr_film.getGenreName();


                    // this is where i add movie
//                    System.out.println("~~~~One Movie\n");
//                    System.out.println("\m_director: " + m_director);
//                    System.out.println("\tmovie_id: " + movie_id);
//                    System.out.println("\tmovie_title: " + movie_title);
//                    System.out.println("\tm_year: " + m_year);
//                    System.out.println("\tg_name: " + g_name);


                    if (m_director == null){
                        movie_statement.setString(1, "null");
                    }
                    else{
                        movie_statement.setString(1, m_director);
                    }

                    if(movie_id == null){
                        break;
                    }
                    else{
                        movie_statement.setString(2, movie_id);
                    }

                    movie_statement.setString(3, movie_title);

                    if (m_year == null){
                        movie_statement.setInt(4, 0);
                    }
                    else{
                        movie_statement.setInt(4, m_year);
                    }

                    if (g_name == null){
                        movie_statement.setString(5, "null");
                    }
                    else{
                        movie_statement.setString(5, g_name);
                    }

                    movie_statement.addBatch();
//                    System.out.println(movie_statement);
//                    System.out.println("\t\t\tInsert Movies");
//                    movieRows = movie_statement.executeBatch();




                    // Retrieving actor's info
                    HashSet<Actor> fActorsList = curr_film.getActors();
                    Iterator<Actor> it = fActorsList.iterator();

                    int actor_count = 0;

//                    System.out.println("~~~~~~~~~~~~~~~5 many actors from one film~~~~~~~~~~~~~~~");
                    while(it.hasNext()){
                        // this is where actors in one moive from 2
//                        System.out.println("~~~~~~~~~~~~~~~6 one actor from one film~~~~~~~~~~~~~~~");
                        actor_count++;
                        // !!!! information about the actors in a movie:
                        Actor tempAtr = it.next();
                        String s_name = tempAtr.getaName();
                        Integer s_year = tempAtr.getBirthYear(); // currently NULL - need to parse still

                        // this is wehre i add actor in movie
//                        System.out.println("~~~~List of star in one film\n");
//                        System.out.println("\tmovie_id: " + movie_id);
//                        System.out.println("\ts_name: " + s_name);
//                        System.out.println("\ts_year: " + s_year);
                        if(s_year == null){
//                            System.out.println("Star has no year");
                            woy_statement.setString(1, movie_id);
                            woy_statement.setString(2, s_name);
                            woy_statement.setString(3, String.valueOf(actor_count));

                            woy_statement.addBatch();
//                            System.out.println(woy_statement);

                        }else
                        {
//                            System.out.println("Star has year");
                            wy_statement.setString(1, movie_id);
                            wy_statement.setString(2, s_name);
                            wy_statement.setInt(3, s_year);
                            wy_statement.setString(4, String.valueOf(actor_count));

                            wy_statement.addBatch();
//                            System.out.println(wy_statement);

                        }
                        actor_count++;

                        count++;

//
                    }

//                    System.out.println("\t\t\tInsert stars");
//                    wyRows = wy_statement.executeBatch();
//                    woyRows = woy_statement.executeBatch();
//                    conn.commit();

//                     looking thorugh all actors in one movie end
                    if(count == 5){
                        break;
                    }

                }
                // looking thorugh all movies with one director end
//                if(count == 5){
//                    break;
//                }
            }
            // looking thorugh all directors end
//            if(count == 5){
//                break;
//            }

//

        }
        System.out.println("Insert Movies..");
        movieRows = movie_statement.executeBatch();

        System.out.println("Insert Stars wy..");
        wyRows = wy_statement.executeBatch();

        System.out.println("Insert Stars woy..");
        woyRows = woy_statement.executeBatch();
        conn.commit();

//        conn.commit();
        System.out.println("finished");
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime)/60000);


    }

}
//33.04151667


//import javax.xml.transform.Result;
//import java.sql.*;
//
//import java.io.IOException;
//import java.util.*;
//
//import java.io.FileWriter;
///*
//
//Note: Please change the username, password and the name of the datbase.
//
//*/
//
//public class BatchInsert {
//
//    public static String getNextStarId(String starmax_id) {
//        String num_after = starmax_id.substring(2);
//        int num_id = Integer.parseInt(num_after) + 1;
//        String new_id = starmax_id.substring(0, 2) + String.valueOf(num_id);
//        return new_id;
//    }
//
//    public static void main(String[] args) throws Exception, InstantiationException, IllegalAccessException, ClassNotFoundException {
//        HashMap<String, List<DirectorFilms>> directorFilms;
//        HashMap<String, Integer> actors;
//        final long startTime = System.currentTimeMillis();
//
//        MainsSaxParser msp = new MainsSaxParser();
//        msp.runMainSAX();
//        directorFilms = msp.getMainsData();
//
//        ActorSAXParser asp = new ActorSAXParser();
//        asp.runActorSAX();
//        actors = asp.getActors();
//
//        CastSAXParser csp = new CastSAXParser(directorFilms, actors);
//        csp.runCastSAX();
//        directorFilms = csp.getCastData();
//
//
//        // Print out inconsistencies and duplicates
//        try {
//            FileWriter myWriterInc = new FileWriter("Inconsistencies.txt");
//            FileWriter myWriterDup = new FileWriter("Duplicates.txt");
//
//            Iterator<String> incM = msp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in mains243.xml: \n");
//            while (incM.hasNext()) {
//                myWriterInc.write(incM.next() + "\n");
//            }
//
//            System.out.println("\n");
//            // Inconsistencies and Duplicates from Cast124.xml
//            Iterator<String> incC = csp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in Cast124.xml: \n");
//            while (incC.hasNext()) {
//                myWriterInc.write(incC.next() + "\n");
//            }
//
//            Iterator<String> incA = asp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in actor63.xml: \n");
//            while (incA.hasNext()) {
//                myWriterInc.write(incA.next() + "\n");
//            }
//
//            System.out.println("\n");
//
//            Iterator<String> dup = csp.getDuplicates().iterator();
//            myWriterDup.write("Duplicates in Cast124.xml: \n");
//            while (dup.hasNext()) {
//                myWriterDup.write(dup.next() + "\n");
//            }
//
//            myWriterInc.close();
//            myWriterDup.close();
//        } catch (IOException e) {
//            System.out.println("Writing Failed!");
//        }
//
//        String loginUser = "mytestuser";
//        String loginPasswd = "My6$Password";
////        String loginUrl = "jdbc:mysql://localhost:3306/moviedbex";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb_example";
//
//        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//        Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
//
//        Iterator df = directorFilms.entrySet().iterator();
//
//        String get_genre_maxid_query = "select max(id) from genres;";
//
//        Statement genre_dbstatement = conn.createStatement();
//
//        int genremax_id = 0;
//        ResultSet rs = genre_dbstatement.executeQuery(get_genre_maxid_query);
//        if (rs.next()) {
//            genremax_id = rs.getInt("max(id)");
//        }
////        System.out.println(genremax_id);
//
//        /////
//
//        // -----------------------------------------
//        Statement star_dbstatement = conn.createStatement();
//
//        String get_star_maxid_query = "select max(id) from stars;";
//        String starmax_id = "";
//        rs = star_dbstatement.executeQuery(get_star_maxid_query);
//        if (rs.next()) {
//            starmax_id = rs.getString("max(id)");
//        }
//
////        String new_id = getNextStarId(starmax_id);
////        System.out.println(new_id);
////        starmax_id = new_id;
////        new_id = getNextStarId(starmax_id);
////        System.out.println(new_id);
//
//        //-----------------------------------------
//
//        String retreiveStars_query = "SELECT * FROM moviedb.stars";
//        PreparedStatement statement = conn.prepareStatement(retreiveStars_query);
//        ResultSet rs_stars = statement.executeQuery();
//
//        // Key: star's name ;  Value: star's ID
//        HashMap<String, String> oldActr = new HashMap<String, String>(); // actors from old database
//
//        // putting all old data into oldActr
//        while (rs_stars.next()) {
//            String s_id = rs_stars.getString("id");
//            String s_name = rs_stars.getString("name"); // current row's star's name
//            oldActr.put(s_name, s_id);
//        }
//
//        // check here
////        System.out.println(oldActr.containsKey("Alan Miller"));
//
//
////
////        System.out.println(oldActr);
//
//
//        // Name : DOB
//        HashMap<String, String> uniqueActor = new HashMap<>();
//
//        // creating uniqueActor
//        for (Map.Entry<String, Integer> actor : actors.entrySet()) {
//            if (!oldActr.containsKey(actor.getKey())) {
//                uniqueActor.put(actor.getKey(), "");
//            }
//        }
//
//        // key: Star Name ; Value: ID
////        System.out.println(uniqueActor);
//
//        String new_ID;
//        for (Map.Entry<String, String> actor : uniqueActor.entrySet()) {
//            new_ID = getNextStarId(starmax_id);
//            actor.setValue(new_ID);
//            starmax_id = new_ID;
//        }
//        //------------------------------------------Alice------------------------
//        // combine both hashmaps
////        uniqueActor.putAll(oldActr);
//
//
//        //        genremax_id++;
////        System.out.println(genremax_id);
////        genremax_id++;
////        System.out.println(genremax_id);
//
////        System.out.println(uniqueActor.containsKey("Alan Miller"));
////
////        String star_name = "Luis Miguel Dominguin";
//
////        if (uniqueActor.containsKey(star_name)) {
////            System.out.println("unique Existing");
////            System.out.println(uniqueActor.get(star_name));
////
////        } else if (oldActr.containsKey(star_name)){
////            System.out.println("Old Existing");
////        }else{
////            System.out.println("not in both ");
////        }
//
////        if(oldActr.containsKey(star_name)){
////            System.out.println("Old Existing");
////            System.out.println(uniqueActor.get(star_name));
////        }
////        else{
////            System.out.println("Old Not Existing");
////        }
//
//
//
//
//
//
//        /// query statements
//        PreparedStatement movie_statement=null;
//        PreparedStatement wy_s_statement=null;
//        PreparedStatement woy_s_statement=null;
//        PreparedStatement sim_statement=null;
//
//
//        int[] movieRows=null;
//        int[] wyRows=null;
//        int[] woyRows=null;
//
//        String movie_query=null;
//        String wy_query=null;
//        String woy_query=null;
//
//        movie_query="call add_movie_parse(?, ?, ?, ?, ?); ";
//
//        String woy_s_id_name_year="INSERT INTO stars (id, name) VALUES(?, ?) ";
//        String wy_s_id_name="INSERT INTO stars (id, name, birthYear) VALUES(?, ?, ?) ";
//        String sim_id_name="INSERT INTO stars_in_movies VALUES(?, ?) ";
//
//        /// query statements
//
////        wy_query="call add_star_wy_new(?, ?, ?, ?); ";
////        woy_query="call add_star_woy_new(?, ?, ?); ";
//        movie_statement=conn.prepareStatement(movie_query);
//        wy_s_statement=conn.prepareStatement(wy_s_id_name);
//        woy_s_statement=conn.prepareStatement(woy_s_id_name_year);
//        sim_statement=conn.prepareStatement(sim_id_name);
//
//        HashMap<String, String> addedStar = new HashMap<>();
//
//        int count=0;
//
//        while(df.hasNext()){
//            Map.Entry mapElement = (Map.Entry) df.next();
//            List<DirectorFilms> curr_df = (List<DirectorFilms>) mapElement.getValue();
//            Iterator<DirectorFilms> cdf = curr_df.iterator();
//
////            System.out.println("~~~~~~~~~~~~~~~1 many directors ~~~~~~~~~~~~~~~");
//
//            while(cdf.hasNext()){
//
//                // According to one director
////                System.out.println("~~~~~~~~~~~~~~~2 one director ~~~~~~~~~~~~~~~");
//
//                DirectorFilms cur_directorFilm = cdf.next();
//                // !!!! Movie Director name:
//                String m_director = cur_directorFilm.getDirectorName(); // director name
//
//
//                HashMap<String, Film> currFilmHash = cur_directorFilm.getfilmHash(); // fid : Film obj
//                Iterator fi = currFilmHash.entrySet().iterator(); // iterating through films hash table
//
//                conn.setAutoCommit(false);
//
//                // director has many films
////                System.out.println("~~~~~~~~~~~~~~~3 many films from one director~~~~~~~~~~~~~~~");
//
//                while(fi.hasNext()){
////                    System.out.println("~~~~~~~~~~~~~~~4 one film from one director~~~~~~~~~~~~~~~");
//                    // each movie happens
//                    Map.Entry me = (Map.Entry) fi.next();
//                    Film curr_film = (Film) me.getValue();
//
//                    // One movie
//                    String movie_id = curr_film.getfId();
//                    String movie_title = curr_film.getfTitle();
//                    Integer m_year = curr_film.getfYear();
//                    String g_name = curr_film.getGenreName();
//
//
//                    count++;
//
//
//                    // this is where i add movie
////                    System.out.println("~~~~One Movie\n");
////                    System.out.println("\tm_director: " + m_director);
////                    System.out.println("\tmovie_id: " + movie_id);
////                    System.out.println("\tmovie_title: " + movie_title);
////                    System.out.println("\tm_year: " + m_year);
////                    System.out.println("\tg_name: " + g_name);
//
//
//                    if (m_director == null){
//                        movie_statement.setString(1, "null");
//                    }
//                    else{
//                        movie_statement.setString(1, m_director);
//                    }
//
//                    if(movie_id == null){
//                        break;
//                    }
//                    else{
//                        movie_statement.setString(2, movie_id);
//                    }
//
//                    movie_statement.setString(3, movie_title);
//
//                    if (m_year == null){
//                        movie_statement.setInt(4, 0);
//                    }
//                    else{
//                        movie_statement.setInt(4, m_year);
//                    }
//
//                    if (g_name == null){
//                        movie_statement.setString(5, "null");
//                    }
//                    else{
//                        movie_statement.setString(5, g_name);
//                    }
//
//                    movie_statement.addBatch();
////                    System.out.println(movie_statement);
////                    System.out.println("\t\t\tInsert Movies");
////                    movieRows = movie_statement.executeBatch();
//
//
//                    // Retrieving actor's info
//                    HashSet<Actor> fActorsList = curr_film.getActors();
//                    Iterator<Actor> it = fActorsList.iterator();
//
//
//
////                    System.out.println("~~~~~~~~~~~~~~~5 many actors from one film~~~~~~~~~~~~~~~");
//                    while(it.hasNext()){
//                        // this is where actors in one moive from 2
////                        System.out.println("~~~~~~~~~~~~~~~6 one actor from one film~~~~~~~~~~~~~~~");
//                        // !!!! information about the actors in a movie:
//                        Actor tempAtr = it.next();
//                        String s_name = tempAtr.getaName();
//                        Integer s_year = tempAtr.getBirthYear(); // currently NULL - need to parse still
//
//                        // this is wehre i add actor in movie
////                        System.out.println("~~~~List of star in one film\n");
////                        System.out.println("\tmovie_id: " + movie_id);
////                        System.out.println("\ts_name: " + s_name);
////                        System.out.println("\ts_year: " + s_year);
////                        System.out.println("--------------------");
//
//
////                        String wy_s_id_name="INSERT INTO stars (id, name) VALUES(?, ?) ";
////                        String woy_s_id_name_year="INSERT INTO stars (id, name, birthYear) VALUES(?, ?, ?) ";
////                        String sim_id_name="INSERT INTO stars_in_movies VALUES(?, ?) ";
//                        if(s_year == null){
////                            System.out.println("Star has no year");
////                            System.out.println(s_year);
//
//                            if(oldActr.containsKey(s_name)){
//                                // actor exist in old actor
//                                System.out.println("1Exsiting Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + oldActr.get(s_name));
//                                // linke the actor to moive
//                                sim_statement.setString(1, movie_id);
//                                sim_statement.setString(2, oldActr.get(s_name));
//                                sim_statement.addBatch();
//
////                                System.out.println(sim_statement);
//                            }
//                            else if (uniqueActor.containsKey(s_name)){
//                                // actor exist in uniqueactor
////                                System.out.println("2New Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + uniqueActor.get(s_name));
//
//                                if(!addedStar.containsKey(s_name))
//                                {
////                                    System.out.println("Added in to the added Star");
//                                    woy_s_statement.setString(1, uniqueActor.get(s_name));
//                                    woy_s_statement.setString(2, s_name);
//                                    woy_s_statement.addBatch();
//                                    addedStar.put(s_name,uniqueActor.get(s_name));
//                                }
//                                else{
////                                    System.out.println("didn't add in to the added Star");
//                                }
////                                System.out.println(woy_s_statement);
//
//                                // linke the actor to moive
//                                sim_statement.setString(1, movie_id);
//                                sim_statement.setString(2, uniqueActor.get(s_name));
//                                sim_statement.addBatch();
//
////                                System.out.println(sim_statement);
//
//                            }
//                            else{
//                                // actor not in both
////                                System.out.println("!!!! Actor not in both ");
////                                new_ID = getNextStarId(starmax_id);
////                                uniqueActor.put(s_name, new_ID);
////                                starmax_id = new_ID;
////
////                                System.out.println("3New Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + uniqueActor.get(s_name));
//                            }
//
//                        }else
//                        {
////                            System.out.println("Star has year");
//                            if(oldActr.containsKey(s_name)){
//                                // actor exist in old actor
////                                System.out.println("3Exsiting Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + oldActr.get(s_name));
//                                sim_statement.setString(1, movie_id);
//                                sim_statement.setString(2, oldActr.get(s_name));
//                                sim_statement.addBatch();
//
////                                System.out.println(sim_statement);
//
//                            }
//                            else if (uniqueActor.containsKey(s_name)){
//                                // actor exist in uniqueactor
////                                System.out.println("4New Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + uniqueActor.get(s_name));
//
//                                if(!addedStar.containsKey(s_name))
//                                {
////                                    System.out.println("Added in to the added Star");
//                                    wy_s_statement.setString(1, uniqueActor.get(s_name));
//                                    wy_s_statement.setString(2, s_name);
//                                    wy_s_statement.setInt(3, s_year);
//                                    wy_s_statement.addBatch();
//                                    addedStar.put(s_name,uniqueActor.get(s_name));
//                                }
//                                else{
////                                    System.out.println("didn't add in to the added Star");
//                                }
//
////                                // add the actor
////                                wy_s_statement.setString(1, uniqueActor.get(s_name));
////                                wy_s_statement.setString(2, s_name);
////                                wy_s_statement.setInt(3, s_year);
////                                wy_s_statement.addBatch();
//
////                                System.out.println(wy_s_statement);
//
//                                // link the actor to moive
//                                sim_statement.setString(1, movie_id);
//                                sim_statement.setString(2, uniqueActor.get(s_name));
//                                sim_statement.addBatch();
////                                System.out.println(sim_statement);
//
//                            }
//                            else{
////                                System.out.println("!!!! Actor not in both ");
////
////                                new_ID = getNextStarId(starmax_id);
////                                uniqueActor.put(s_name, new_ID);
////                                starmax_id = new_ID;
////
////                                System.out.println("3New Actor");
////                                System.out.println("\tmovie_id: " + movie_id);
////                                System.out.println("\ts_name: " + s_name);
////                                System.out.println("\ttemp_str_id: " + uniqueActor.get(s_name));
//                            }
//
//                        }
//
////
//                    }
//
////                    System.out.println("\t\t\tInsert stars");
////                    wyRows = wy_statement.executeBatch();
////                    woyRows = woy_statement.executeBatch();
////                    conn.commit();
//
////                     looking thorugh all actors in one movie end
////                    if(count == 1000){
////                        break;
////                    }
//
//                }
//                // looking thorugh all movies with one director end
////                if(count == 1000){
////                    break;
////                }
//            }
//            // looking thorugh all directors end
////            if(count == 1000){
////                break;
////            }
//
////
//
//        }
//
//
//        System.out.println("Insert Movies..");
//        movieRows = movie_statement.executeBatch();
//
//        System.out.println("Insert Stars wy..");
//        wyRows = wy_s_statement.executeBatch();
//
//        System.out.println("Insert Stars woy..");
//        woyRows = woy_s_statement.executeBatch();
//
//        System.out.println("Insert Stars sim..");
//        woyRows = sim_statement.executeBatch();
//
//        conn.commit();
//        System.out.println("finished");
//        final long endTime = System.currentTimeMillis();
//
//        System.out.println("Total execution time: " + (endTime - startTime)/60000);
//
//        rs_stars.close();
//        rs.close();
//        sim_statement.close();
//        movie_statement.close();
//        wy_s_statement.close();
//        woy_s_statement.close();
//        statement.close();
//        conn.close();
//
//    }
//
//}
////33.04151667