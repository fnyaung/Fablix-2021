import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import java.io.FileWriter;

public class MainsSaxParser extends DefaultHandler{
    // key: dirID ; Value: DirectorFilms obj
    HashMap<String, List<DirectorFilms>> directorFilms; // contains parsed data from XML
//    List<DirectorFilms> directorFilms;
    List<String> inconsistencies; // contains inconsistencies from XML

    private String tempVal;

    // to maintain context
    private String curr_dirID; // current director ID we're looking at
    private DirectorFilms tempDirectorFilms;
    private Film tempFilm;
    private String curr_fID;

    private int countMovies = 0;
    FileWriter myWriter; // write results to MainOutput.txt

    public MainsSaxParser() {
        directorFilms = new HashMap<>(); // for every <movies>
        inconsistencies = new ArrayList<>();
    }

    public void runMainSAX(){
        parseDocument();
        printData();
    }

    public HashMap<String, List<DirectorFilms>> getMainsData(){
        return directorFilms;
    }
    public List<String> getInconsistencies(){
        return inconsistencies;
    }

    // SAX Parser
    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            myWriter = new FileWriter("MainOutput.txt");
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("mains243.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Iterate through the list and print
     * the contents
     */
    private void printData(){

        System.out.println("No of DirectorFilms '" + directorFilms.size() + ".");
        System.out.println("No of Movies " + countMovies + ".");

        Iterator df = directorFilms.entrySet().iterator();
        Iterator<String> err = inconsistencies.iterator();
        try{
            // Printing parsed data from XML
            while (df.hasNext()) {
                Map.Entry mapElement = (Map.Entry) df.next();
                myWriter.write("--- Director ID: " + mapElement.getKey() + "\n");

                List<DirectorFilms> curr_df= (List<DirectorFilms>) mapElement.getValue();
                Iterator<DirectorFilms> cdf = curr_df.iterator();
                while(cdf.hasNext()){
                    myWriter.write(cdf.next().toString());
                }
//            System.out.println(it.next().toString());
            }
            myWriter.close();

            // Printing inconsistencies
            System.out.println("Inconsistencies in MainXML: \n");
            while(err.hasNext()){
                System.out.println(err.next());
            }
        }catch(IOException e){
            System.out.println("Writing Failed!");
        }
    }

    //Event Handlers
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset tempVal
        tempVal = "";
        if (qName.equalsIgnoreCase("directorfilms")) {
            // create a new instance of DirectorFilms
            tempDirectorFilms = new DirectorFilms();
        }else if(qName.equalsIgnoreCase("film")){
            // create a new instance of film
            curr_fID = "";
            tempFilm = new Film();
        }
    }

    // get characters within the tag
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(qName.equalsIgnoreCase("dirid")){
            try{
                curr_dirID = tempVal;
            }catch(Exception e){
                System.out.println("Error in setting curr_dirID - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("directorfilms")){
            try{
                if(!directorFilms.containsKey(curr_dirID)){
                    // new director ID. Make a new List<DirectorFilms> obj
                    directorFilms.put(curr_dirID, new ArrayList<DirectorFilms>());
                }
                // director ID existed before. Just append the <directorfilms>
                directorFilms.get(curr_dirID).add(tempDirectorFilms);
//                directorFilms.add(tempDirectorFilms);
            }catch(Exception e){
                System.out.println("Error in adding tempDirectorFilms to directorFilms - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("film")){
            try{
                tempDirectorFilms.addFilm(curr_fID, tempFilm);
                countMovies++;
            }catch(Exception e){
                System.out.println("Error in adding tempFilm to tempDirectorFilms - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("dirname")){
            try{
                tempDirectorFilms.setDirectorName(tempVal);
            }catch(Exception e){
//                System.out.println("Error in setting Director Name - tempVal: " + tempVal);
                tempDirectorFilms.setDirectorName(null);
                inconsistencies.add("<dirname> : " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("fid")){
            try{
                curr_fID = tempVal;
                tempFilm.setfId(tempVal);
            }catch(Exception e){
//                System.out.println("Error in putting in setting fID - tempVal: " + tempVal);
                tempFilm.setfId(null);
                inconsistencies.add("<fid> : " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("t")){
            try{
                tempFilm.setfTitle(tempVal);
            }catch(Exception e){
//                System.out.println("Error in putting in setting fTitle - tempVal: " + tempVal);
                tempFilm.setfTitle(null);
                inconsistencies.add("<t> : " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("year")){
            try{
                tempFilm.setfYear(Integer.parseInt(tempVal));
            }catch(Exception e){
//                System.out.println("Error in putting in setting fYear - tempVal: " + tempVal);
                tempFilm.setfYear(null);
                inconsistencies.add("<year> : " + tempVal);

            }
        } else if(qName.equalsIgnoreCase("cat")){
            try{
                tempFilm.setGenreName(tempVal);
            }catch(Exception e){
//                System.out.println("Error in putting in setting fYear - tempVal: " + tempVal);
                tempFilm.setGenreName(null);
                inconsistencies.add("<cat> : " + tempVal);
            }
        }
    }

//    public static void main(String[] args) {
//        MainsSaxParser spc = new MainsSaxParser();
//        spc.runMainSAX();
//    }
}
