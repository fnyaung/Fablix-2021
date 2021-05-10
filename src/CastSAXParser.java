import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import java.io.FileWriter;

public class CastSAXParser extends DefaultHandler{
    // key: dirID ; Value: DirectorFilms obj
    HashMap<String, List<DirectorFilms>> directorFilms; // contains parsed data from XML
    List<String> inconsistencies; // contains inconsistencies from XML
    List<String> duplicates;

    private String tempVal;

    // to maintain context
    private String curr_dirID;
    private String curr_fID;
    private String curr_movieName;
    private List<DirectorFilms> tempDFList;
    private Film tempFilm;
    private Actor tempActor;

    private int castEntries = 0;  // total entries in general (duplicates included)
    private int addedEntries = 0; // non-duplicates entries

    FileWriter myWriter; // write results to CastOutput.txt

    public CastSAXParser(HashMap<String, List<DirectorFilms>> directorFilms){
        this.directorFilms = directorFilms;
        inconsistencies = new ArrayList<>();
        duplicates = new ArrayList<>();
    }

    public void runCastSAX(){
        parseDocument();
        printData();
    }

    public HashMap<String, List<DirectorFilms>> getCastData(){
        return directorFilms;
    }
    public List<String> getInconsistencies(){
        return inconsistencies;
    }

    // SAX Parser
    private void parseDocument(){
        // get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            myWriter = new FileWriter("CastOutput.txt");
            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("casts124.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void printData(){
        System.out.println("No of castEntries'" + castEntries + ".");
        System.out.println("No of addedEntries " + addedEntries + ".");

        Iterator df = directorFilms.entrySet().iterator();
        Iterator<String> err = inconsistencies.iterator();
        Iterator<String> dup = duplicates.iterator();

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
            System.out.println("Inconsistencies in CastXML: \n");
            while(err.hasNext()){
                System.out.println(err.next());
            }

            // Printing Duplicates
            System.out.println("\nDuplicates in CastXML: \n");
            while(dup.hasNext()){
                System.out.println(dup.next());
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

        if(qName.equalsIgnoreCase("a")){
            tempActor = new Actor();
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
                System.out.println("__1__");
                System.out.println("__1__ directorFilms.get(curr_dirID) = " + directorFilms.get(curr_dirID));
                tempDFList = directorFilms.get(curr_dirID);
                System.out.println("__2__curr_dirID: " + curr_dirID);
                System.out.println("__3__tempDFList" + tempDFList.toString());
            }catch(Exception e){
                System.out.println("Error in retrieving dirID - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("f")){
            try{
                curr_fID = tempVal;
                System.out.println("curr_fID = " + curr_fID);
                System.out.println("++++1+++ tempDFList= " + tempDFList);
                for(DirectorFilms df : tempDFList){
                    System.out.println("++++2+++ df.getfilmHash()" + df.getfilmHash());
                    if(df.getfilmHash().containsKey(curr_fID)){
                        System.out.println("We found the corresponding filmHash");
                        tempFilm =  df.getfilmHash().get(curr_fID);
                        System.out.println("--- tempfilm: " + tempFilm);
                        break;
                    }
                }
            }catch(Exception e){
                System.out.println("Error in retrieving film id <f> - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("t")){
            try{
                curr_movieName = tempVal;
            } catch(Exception e){
                System.out.println("Error in retrieving film name <t> - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("a")){
            try{
                tempActor.setaName(tempVal);
//                System.out.println(">> 1) tempActor.name: " + tempActor.getaName());
                // if we have a duplicate actor
                if(tempFilm.getActors().contains(tempActor)){
//                    System.out.println(">> 2) We have a duplicate!");
                    String dupErr = "fid: " + curr_fID + ", MoveName: " + curr_movieName + ", actorName: " + tempActor.getaName() + "\n";
//                    System.out.println(dupErr);
                    duplicates.add(dupErr);
//                    System.out.println("~~~ added dupErr successfully!");
                }else{
//                    System.out.println(">> 3.1 trying to add actor");
                    tempFilm.addActor(tempActor);
//                    System.out.println(">> 3.2 added successfully!");
                    addedEntries++;
                }
                castEntries++;
                // clear tempActor
                tempActor = null;
                curr_movieName = "";
                curr_fID = "";
            }catch(Exception e){
                System.out.println("Error in adding tempActor - tempVal: " + tempVal);
            }
        } else if(qName.equalsIgnoreCase("dirfilms")){
            curr_dirID = "";
            tempDFList = null;
        }
    }
}
