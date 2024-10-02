package by.rusakou.norma.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Класс для парсинга объектов Coefficient.
 * После парсинга создаём коллекцию объектов Coefficient.
 */
public class CoefficientParser {

    private ArrayList<Coefficient> coefficients;

    public CoefficientParser(){ coefficients = new ArrayList<>();}

    public ArrayList<Coefficient> getCoefficients(){
        return  coefficients;
    }

    public boolean parse(String xmlData){
        boolean status = true;
        Coefficient currentCoefficient = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("Coefficient".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentCoefficient = new Coefficient();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("Coefficient".equalsIgnoreCase(tagName)){
                                coefficients.add(currentCoefficient);
                                inEntry = false;
                            } else if("Name".equalsIgnoreCase(tagName)){
                                currentCoefficient.setName(textValue);
                            } else if("coefficient_for_norm".equalsIgnoreCase(tagName)) {
                                currentCoefficient.setCoefficientForNorm(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return  status;
    }
}