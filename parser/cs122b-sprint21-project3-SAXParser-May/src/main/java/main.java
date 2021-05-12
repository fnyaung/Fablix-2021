//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.Statement;
//import java.sql.SQLException;
//
//import java.io.IOException;
//import java.util.*;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//
//import org.xml.sax.helpers.DefaultHandler;
//import java.io.FileWriter;
///*
//
//Note: Please change the username, password and the name of the datbase.
//
//*/
//
//public class BatchInsert {
//
//    public static void main (String[] args) throws Exception, InstantiationException, IllegalAccessException, ClassNotFoundException{
//        HashMap<String, List<DirectorFilms>> directorFilms;
//        HashMap<String, Integer> actors;
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
//        try{
//            FileWriter myWriterInc = new FileWriter("Inconsistencies.txt");
//            FileWriter myWriterDup = new FileWriter("Duplicates.txt");
//
//            Iterator<String> incM = msp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in mains243.xml: \n");
//            while(incM.hasNext()){
//                myWriterInc.write(incM.next() + "\n");
//            }
//
//            System.out.println("\n");
//            // Inconsistencies and Duplicates from Cast124.xml
//            Iterator<String> incC = csp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in Cast124.xml: \n");
//            while(incC.hasNext()){
//                myWriterInc.write(incC.next() + "\n");
//            }
//
//            Iterator<String> incA = asp.getInconsistencies().iterator();
//            myWriterInc.write("Inconsistencies in actor63.xml: \n");
//            while(incA.hasNext()){
//                myWriterInc.write(incA.next() + "\n");
//            }
//
//            System.out.println("\n");
//
//            Iterator<String> dup = csp.getDuplicates().iterator();
//            myWriterDup.write("Duplicates in Cast124.xml: \n");
//            while(dup.hasNext()){
//                myWriterDup.write(dup.next() + "\n");
//            }
//
//            myWriterInc.close();
//            myWriterDup.close();
//        }catch(IOException e){
//            System.out.println("Writing Failed!");
//        }
//
//        String loginUser = "mytestuser";
//        String loginPasswd = "My6$Password";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedbex";
////        String loginUrl = "jdbc:mysql://localhost:3306/moviedb_example";
//
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        Connection conn = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
//
//        Iterator df = directorFilms.entrySet().iterator();
//
//        /// query statements
//        PreparedStatement movie_statement=null;
//        PreparedStatement wy_statement=null;
//        PreparedStatement woy_statement=null;
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
//        wy_query="call add_star_wy(?, ?, ?); ";
//        woy_query="call add_star_woy(?, ?); ";
//
//        movie_statement=conn.prepareStatement(movie_query);
//        wy_statement=conn.prepareStatement(wy_query);
//        woy_statement=conn.prepareStatement(woy_query);
//        /// query statements
//
//
//        int count = 0;
//
//        while(df.hasNext()){
//            Map.Entry mapElement = (Map.Entry) df.next();
//            List<DirectorFilms> curr_df = (List<DirectorFilms>) mapElement.getValue();
//            Iterator<DirectorFilms> cdf = curr_df.iterator();
//
//            System.out.println("~~~~~~~~~~~~~~~1 many directors ~~~~~~~~~~~~~~~");
//
//            while(cdf.hasNext()){
//
//                // According to one director
//                System.out.println("~~~~~~~~~~~~~~~2 one director ~~~~~~~~~~~~~~~");
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
//                System.out.println("~~~~~~~~~~~~~~~3 many films from one director~~~~~~~~~~~~~~~");
//
//                while(fi.hasNext()){
//                    System.out.println("~~~~~~~~~~~~~~~4 one film from one director~~~~~~~~~~~~~~~");
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
//                    // this is where i add movie
//                    System.out.println("~~~~One Movie\n");
////                    System.out.println("\m_director: " + m_director);
////                    System.out.println("\tmovie_id: " + movie_id);
////                    System.out.println("\tmovie_title: " + movie_title);
////                    System.out.println("\tm_year: " + m_year);
////                    System.out.println("\tg_name: " + g_name);
//
//                    count ++;
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
//                    System.out.println(movie_statement);
//                    System.out.println("\t\t\tInsert Movies");
//                    movieRows = movie_statement.executeBatch();
//
//
//
//
//                    // Retrieving actor's info
//                    HashSet<Actor> fActorsList = curr_film.getActors();
//                    Iterator<Actor> it = fActorsList.iterator();
//
//                    int actor_count = 0;
//
//                    System.out.println("~~~~~~~~~~~~~~~5 many actors from one film~~~~~~~~~~~~~~~");
//                    while(it.hasNext()){
//                        // this is where actors in one moive from 2
//                        System.out.println("~~~~~~~~~~~~~~~6 one actor from one film~~~~~~~~~~~~~~~");
//                        actor_count++;
//                        // !!!! information about the actors in a movie:
//                        Actor tempAtr = it.next();
//                        String s_name = tempAtr.getaName();
//                        Integer s_year = tempAtr.getBirthYear(); // currently NULL - need to parse still
//
//                        // this is wehre i add actor in movie
//                        System.out.println("~~~~List of star in one film\n");
////                        System.out.println("\tmovie_id: " + movie_id);
////                        System.out.println("\ts_name: " + s_name);
////                        System.out.println("\ts_year: " + s_year);
//                        if(s_year == null){
//                            System.out.println("Star has no year");
//                            woy_statement.setString(1, movie_id);
//                            woy_statement.setString(2, s_name);
//                            woy_statement.addBatch();
//                            System.out.println(woy_statement);
//                        }else
//                        {
//                            System.out.println("Star has year");
//                            wy_statement.setString(1, movie_id);
//                            wy_statement.setString(2, s_name);
//                            wy_statement.setInt(3, s_year);
//                            wy_statement.addBatch();
//                            System.out.println(wy_statement);
//                        }
//
////
//                    }
//
//                    System.out.println("\t\t\tInsert stars");
//                    wyRows = wy_statement.executeBatch();
//                    woyRows = woy_statement.executeBatch();
//                    conn.commit();
//
//                    // looking thorugh all actors in one movie end
////                    if(count == 5){
////                        break;
////                    }
//
//                }
//                // looking thorugh all movies with one director end
////                if(count == 5){
////                    break;
////                }
//            }
//            // looking thorugh all directors end
////            if(count == 5){
////                break;
////            }
//
////
////            movieRows = movie_statement.executeBatch();
////
////            wyRows = wy_statement.executeBatch();
////            woyRows = woy_statement.executeBatch();
////            conn.commit();
//        }
//
////        conn.commit();
//        System.out.println("finished");
//
//
//    }
//
//}