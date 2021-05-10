import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;
import java.io.FileWriter;

public class GeneralSaxParser {
    public static void main(String[] args) {
        HashMap<String, List<DirectorFilms>> directorFilms;

        MainsSaxParser msp = new MainsSaxParser();
        msp.runMainSAX();
        directorFilms = msp.getMainsData();

        CastSAXParser csp = new CastSAXParser(directorFilms);
        csp.runCastSAX();
//        csp.runCastSAX(directorFilms);
//        System.out.println(msp.getInconsistencies());
//        System.out.println(msp.getMainsData());
    }
}
