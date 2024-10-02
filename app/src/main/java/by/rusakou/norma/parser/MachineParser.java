package by.rusakou.norma.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Класс для парсинга объектов Machine.
 * После парсинга создаём коллекцию объектов Machine.
 */
public class MachineParser {
    private ArrayList<Machine> machines;

    public MachineParser(){
        machines = new ArrayList<>();
    }

    public ArrayList<Machine> getMachines(){
        return  machines;
    }

    public boolean parse(String xmlData){
        boolean status = true;
        Machine currentMachine = null;
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
                        if("Machine".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentMachine = new Machine();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("Machine".equalsIgnoreCase(tagName)){
                                machines.add(currentMachine);
                                inEntry = false;
                            } else if("name".equalsIgnoreCase(tagName)){
                                currentMachine.setName(textValue);
                            } else if("model_machine".equalsIgnoreCase(tagName)){
                                currentMachine.setModelMachine(textValue);
                            }else if("max_form_width".equalsIgnoreCase(tagName)) {
                                currentMachine.setMaxFormWidth(textValue);
                            } else if("max_form_length".equalsIgnoreCase(tagName)) {
                                currentMachine.setMaxFormLength(textValue);
                            } else if("machine_bfs".equalsIgnoreCase(tagName)){
                                currentMachine.setMachineBFS(textValue);
                            } else if("between_product_bfs".equalsIgnoreCase(tagName)){
                                currentMachine.setBetweenProductBFS(textValue);
                            } else if("for_edge_form_bfs".equalsIgnoreCase(tagName)){
                                currentMachine.setForEdgeFormBFS(textValue);
                            }else if("machine_punching".equalsIgnoreCase(tagName)){
                                currentMachine.setMachinePunching(textValue);
                            } else if("between_product_punching".equalsIgnoreCase(tagName)){
                                currentMachine.setBetweenProductPunching(textValue);
                            } else if("for_edge_form_punching".equalsIgnoreCase(tagName)){
                                currentMachine.setForEdgeFormPunching(textValue);
                            } else if("for_chain".equalsIgnoreCase(tagName)){
                                currentMachine.setForChain(textValue);
                            } else if("used_pp".equalsIgnoreCase(tagName)){
                                currentMachine.setUsedPP(textValue);
                            } else if("max_length_knife_ps".equalsIgnoreCase(tagName)){
                                currentMachine.setMaxLengthKnifePS(textValue);
                            } else if("max_length_knife_pet".equalsIgnoreCase(tagName)){
                                currentMachine.setMaxLengthKnifePET(textValue);
                            } else if("max_length_knife_pvc".equalsIgnoreCase(tagName)){
                                currentMachine.setMaxLengthKnifePVC(textValue);
                            } else if("max_length_knife_pp".equalsIgnoreCase(tagName)){
                                currentMachine.setMaxLengthKnifePP(textValue);
                            } else if("coefficient_knife_bfs".equalsIgnoreCase(tagName)){
                                currentMachine.setCoefficientKnifeBFS(textValue);
                            } else if("array_max_knife_thickness".equalsIgnoreCase(tagName)){
                                currentMachine.setArrayMaxKnifeThickness(textValue);
                            } else if("array_max_length_knife_ps".equalsIgnoreCase(tagName)){
                                currentMachine.setArrayMaxLengthKnifePS(textValue);
                            } else if("array_max_length_knife_pet".equalsIgnoreCase(tagName)){
                                currentMachine.setArrayMaxLengthKnifePET(textValue);
                            } else if("array_max_length_knife_pvc".equalsIgnoreCase(tagName)){
                                currentMachine.setArrayMaxLengthKnifePVC(textValue);
                            } else if("array_max_length_knife_pp".equalsIgnoreCase(tagName)){
                                currentMachine.setArrayMaxLengthKnifePP(textValue);
                            } else if("type_machine_k".equalsIgnoreCase(tagName)){
                                currentMachine.setTypeMachineK(textValue);
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
