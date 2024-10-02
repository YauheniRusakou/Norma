package by.rusakou.norma.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Класс для парсинга объектов Property.
 * После парсинга создаём коллекцию объектов Property.
 */
public class PropertyParser {

        private ArrayList<Property> properties;

        public PropertyParser(){ properties = new ArrayList<>();}

        public ArrayList<Property> getProperties(){
            return  properties;
        }

        public boolean parse(String xmlData){
            boolean status = true;
            Property currentProperty = null;
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
                            if("Property".equalsIgnoreCase(tagName)){
                                inEntry = true;
                                currentProperty = new Property();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            textValue = xpp.getText();
                            break;
                        case XmlPullParser.END_TAG:
                            if(inEntry){
                                if("Property".equalsIgnoreCase(tagName)){
                                    properties.add(currentProperty);
                                    inEntry = false;
                                } else if("start_material".equalsIgnoreCase(tagName)){
                                    currentProperty.setStartMaterial(textValue);
                                } else if("start_machine".equalsIgnoreCase(tagName)) {
                                    currentProperty.setStartMachine(textValue);
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
