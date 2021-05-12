import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ActorSAXParser extends DefaultHandler{
    // key: actor's stagename ; Value: actor's dob
    HashMap<String, Integer> actors;
    List<String> inconsistencies;

    // to maintain context
    String tempVal;
    String curr_stagename;

    public ActorSAXParser(){
        actors = new HashMap<String, Integer>();
        inconsistencies = new ArrayList<String>();
    }

    public void runActorSAX(){
        parseDocument();
        printData();
    }

    public HashMap<String, Integer> getActors(){
        return actors;
    }
    public List<String> getInconsistencies(){
        return this.inconsistencies;
    }

    public void printData(){
        Iterator actor = actors.entrySet().iterator();
        while(actor.hasNext()){
            Map.Entry actr = (Map.Entry) actor.next();
            System.out.println("StageName:" +  actr.getKey()  + "-- DOB: " + actr.getValue() + "\n");
        }

        System.out.println("Inconsistencies in ActorsSAX");
        Iterator<String> inc = inconsistencies.iterator();
        while(inc.hasNext()){
            System.out.println(inc.next());
        }
    }

    // SAX Parser
    private void parseDocument(){
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            //get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            //parse the file and also register this class for call backs
            sp.parse("actors63.xml", this);

        } catch (SAXException se) {
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    //Event Handlers
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //reset tempVal
        tempVal = "";
    }

    // get characters within the tag
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if(qName.equalsIgnoreCase("stagename")){
            curr_stagename = tempVal;
        }else if(qName.equalsIgnoreCase("dob")){
            try{
                if(tempVal != null && tempVal != ""){
                    actors.put(curr_stagename, Integer.parseInt(tempVal));
                }else{
                    actors.put(curr_stagename, null);
                }
            }catch(Exception e){
                actors.put(curr_stagename, null);
                inconsistencies.add("<dob>: " + tempVal);
//                System.out.println("ActorSAX: Error in adding stagename - tempVal: " + tempVal);
            }
        }
    }

}
