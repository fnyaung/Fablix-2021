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
//
//public class GeneralSaxParser {
//    public static void main(String[] args) {
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
//
//
//            myWriterInc.close();
//            myWriterDup.close();
//        }catch(IOException e){
//            System.out.println("Writing Failed!");
//        }
//
//
//    }
//}
