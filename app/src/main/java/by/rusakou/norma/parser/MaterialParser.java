package by.rusakou.norma.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Класс для парсинга объектов Material.
 * После парсинга создаём коллекцию объектов Material.
 */
public class MaterialParser {

    private ArrayList<Material> materials;

    public MaterialParser(){
        materials = new ArrayList<>();
    }

    public ArrayList<Material> getMaterials(){
        return  materials;
    }

    public boolean parse(String xmlData){
        boolean status = true;
        Material currentMaterial = null;
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
                        if("Material".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentMaterial = new Material();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("Material".equalsIgnoreCase(tagName)){
                                materials.add(currentMaterial);
                                inEntry = false;
                            } else if("Name".equalsIgnoreCase(tagName)){
                                currentMaterial.setName(textValue);
                            } else if("Shrinkage".equalsIgnoreCase(tagName)) {
                                currentMaterial.setShrinkage(textValue);
                            } else if("Density".equalsIgnoreCase(tagName)){
                                currentMaterial.setDensity(textValue);
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
