/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistdsapixmlcreator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 *
 * @author adgao
 */
public class Writer {
    private HashMap<String, ArrayList<String>> grammar;//antecedente, producciones// the grammar with the semantics actions
    //private HashMap<String, ArrayList<String>> grammar;//antecedente, producciones//the grammar without the semantics actions
    private String path;//path of the file of the grammar
    private String pathResult;
    private Stack<String> pendChain;
    private String readChain;
    private Element espec;
    private Document doc;
    private Integer ruleCount=1;
    private ArrayList<String> antecedentes;
    private Integer numNodos=0;
    private ArrayList<Node> nodes;
    private ArrayList<Paso> steps;
    private HashMap<String, String> ruleId;//rule, id of the rule
    private Integer pasoCount=0;
    private String traductorType;
    private HashMap<Integer,Paso>stepMaps;//step id , step
    
    
    
    public Writer( String path, String pathResult, String entryChainPath, Boolean isDescendat) {
        //this.grammar = new HashMap<>();
        this.grammar = new HashMap<>();
        this.path=path;
        this.pathResult=pathResult;
        this.antecedentes=new ArrayList<>();
        this.pendChain=new Stack<>();
        this.stepMaps=new HashMap<>();
        
        readChain(entryChainPath);
        //grammarWithoutActions();
        this.ruleId=new HashMap<>();
        
        
        this.nodes=new ArrayList<>();
        this.steps=new ArrayList<>();
        if(isDescendat){
            traductorType="Descendente";
        }
        else{
            traductorType="Ascendente";
        }
        readGramatica(path);
        DocumentBuilderFactory dbFactory =DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder=null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        doc = dBuilder.newDocument();

        Element rootElement = doc.createElement("raiz");
        doc.appendChild(rootElement);
        this.espec = doc.createElement("espec");
        
        rootElement.appendChild(espec);
        Attr attr = doc.createAttribute("nombre");
	attr.setValue("Especificación del XML");
        espec.setAttributeNode(attr); 
        
        
        
        //this.table= new Integer[this.noTerminals.size()][this.terminals.size()];
        
        
        
        
    }
    /**
     * add a new node to the node list
     * @param element
     * element of the node
     * @param terminal
     * true if is terminal false if not 
     * @param haveBrother 
     * boolean that notify if the node have a rigth brother
     * @return 
     * the new node
     */
    public Node addNode(String element, Boolean terminal,Boolean haveBrother){
        Node node=new Node(numNodos, element, terminal, 0,haveBrother);
        nodes.add(node);
        numNodos++;
        return node;
    } 

    /**
     * add a no terminal step
     * @param element
     * element of the step
     * @param atributo
     * name of the attribute of the step
     * @param objects
     * no terminals related with this step
     * @return
     * the added step
     */
    public Node addPasoNoTerminal(String element, String atributo, String value, String rule, Object ... objects){
        //rule=processRule(rule, element);
        Node nodo=addNode(element, false,null);
        HashSet<Integer> relNodo=new HashSet<>();
        for(int i=1;i<objects.length;i++){
            Class clase=getClass(objects[i]);
            Method getId=getMethod(clase, "getId");
            Integer id=getId(getId,objects[i]);
            Method getNode=getMethod(clase, "getNode");
            Node node=getNode(getNode,objects[i]);
            relNodo.add(id);  
            node.setFatherNode(nodo);
            if(i==1){
                Method getPaso=getMethod(clase, "getPaso");
                Paso paso=getPaso(getPaso, objects[i]);
                paso.setRegla(removeOnlyActions(rule)); 
            }
        }
        Class claseAnt=getClass(objects[0]);
        Method getValue=getMethod(claseAnt, "getValue");
        //String value=getValue(getValue,objects[0]);
        Paso paso=null;
        if(!atributo.equals("null"))
            paso=addPaso(false,null,element, element+"."+atributo+"="+value, null,relNodo);
        else
            paso=addPaso(false,null,element, null, null,relNodo);
        
        Method setId=getMethodSetId(claseAnt, "setId");
        setValue(claseAnt, objects[0], value);
        setId(setId,objects[0], paso.getId());
        setNode(claseAnt,objects[0],nodo);
        setPaso(claseAnt, objects[0], paso);
        return nodo;
    }
    public Node addPasoNoTerminal(String element, String atributo, Integer value, String rule, Object ... objects){
        return addPasoNoTerminal(element, atributo, value.toString(), rule, objects);
    }
    
    /**
     * add a terminal step
     * @param element
     * element of the step
     * @param atributo
     * name of the attribute of the step
     * @param object
     * no terminal related with this step
     * @return
     * the added step
     */
    public Node addPasoTerminal(String element, String atributo, String value, Object object){
       Node nodo=addNode(element, true, null);
       Class claseAnt=getClass(object);
       Method getValue=getMethod(claseAnt, "getValue");
       //String value=getValue(getValue, object);
       Paso paso=null;
       if(atributo!=null)
           paso=addPaso(true,element,element, element+"."+atributo+"="+value, null);
       else
           paso=addPaso(true, element, element, null, null);

       Method setId=getMethodSetId(claseAnt, "setId");
       setId(setId,object, paso.getId());
       setNode(claseAnt, object, nodo);
       setPaso(claseAnt, object, paso);
       setValue(claseAnt, object, value);
       return nodo;
    }
    public Node addPasoTerminal(String element, String atributo, Integer value, Object object){
        return addPasoTerminal(element, atributo, value.toString(), object);
    }
    public Node addPasoTerminal(String element, String atributo, Object object){
       Node nodo=addNode(element, true, null);
       Class claseAnt=getClass(object);
       //Method getValue=getMethod(claseAnt, "getValue");
       //String value=getValue(getValue, object);
       Paso paso=addPaso(true, element, element, null, null);

       Method setId=getMethodSetId(claseAnt, "setId");
       setId(setId,object, paso.getId());
       setNode(claseAnt, object, nodo);
       setPaso(claseAnt, object, paso);
       return nodo;
    }
    /**
     * add a lambda step
     * @param element
     * element of the father step
     * @param atributo
     * name of the attribute of the father step
     * @param object
     * no terminal related with the father step
     * @param value
     * value of the step
     * @return
     * the added step
    **/
    public Node addPasoLambda(String element, String atributo, String value, String action, Object object){
        
        
        String regla=element+"::= λ";
        
        
        Node nodoL=addNode("λ", true,null);
        Node nodo=addNode(element, false,null);

        nodoL.setFatherNode(nodo);

        Class claseAnt=getClass(object);
        //Method getValue=getMethod(claseAnt, "getValue");
        //String value=getValue(getValue,object);
        setValue(claseAnt, object, value);
        Paso paso=addPaso(true,null,"λ",null,regla);
        paso=addPaso(false,null,element, element+"."+atributo+"="+value, null,paso.getId());                        
        Method setId=getMethodSetId(claseAnt, "setId");
        setValue(claseAnt, object, value);
        setId(setId,object, paso.getId());
        setNode(claseAnt,object,nodo);
        setPaso(claseAnt, object, paso);
        return nodo;
    }
    public Node addPasoLambda(String element, String atributo, Integer value, String action, Object object){
        return addPasoLambda(element, atributo, value.toString(), action, object);
    }
    /**
     * add a lambda step
     * @param element
     * element of the father step
     * @param atributo
     * name of the attribute of the father step
     * @param haveBrother
     * @param her
     * @param nodeAnt
     * @param object
     * no terminal related with the father step
     * @return
     * the added step
    **/
    public Node addPasoLambda(String element, String atributoHer, String atributoSint, String her, String action, Object object, Boolean haveBrother, Node nodeAnt){
        String regla=element+"::= λ";
        
        
        Node nodo=null;
        Paso paso=null;
        Class claseAnt=getClass(object);

        nodo=addNode(element, false, haveBrother);
        paso=addPaso(false,null,element, element+"."+atributoHer+"="+her+" "+element+"."+atributoSint+"=null", null, nodeAnt.getId()); 

        nodo.setFatherNode(nodeAnt);
        Node nodoL=addNode("λ", true,false);
        nodoL.setFatherNode(nodo);
        Paso pasoL=addPaso(true, null, "λ", null, regla, paso.getId());
        setNode(claseAnt,object,nodo);
        setPaso(claseAnt, object, paso);
        setValue(claseAnt, object, her);
        updatesValues(pasoL, nodoL, her);
        return nodo;
    }
    public Node addPasoLambda(String element, String atributoHer, String atributoSint, Integer her, String action, Object object, Boolean haveBrother, Node nodeAnt){
        return addPasoLambda(element, atributoHer, atributoSint, her.toString(), action, object, haveBrother, nodeAnt);
    }
    /**
     * add a lambda step
     * @param element
     * element of the father step
     * @param atributo
     * name of the attribute of the father step
     * @param haveBrother
     * @param nodeAnt
     * @param object
     * no terminal related with the father step
     * @return
     * the added step
    **/
    public Node addPasoTerminal(String element, String atributo, String value, Object object, Boolean haveBrother, Node nodeAnt){
        Class claseAnt=getClass(object);
        
        Paso paso=null;
        if(atributo!=null){
            //Method getValue=getMethod(claseAnt, "getValue");
            //String value=getValue(getValue,object);
            paso=addPaso(true,value, element, element+"."+atributo+"="+value, null,nodeAnt.getId());
            
        }
        else
            paso=addPaso(true,element, element, null, null,nodeAnt.getId());
        Node node=addNode(element, true,haveBrother);
        node.setFatherNode(nodeAnt);
        setNode(claseAnt,object,node);
        setPaso(claseAnt, object, paso);
        setValue(claseAnt, object, value);
        return node;
    }
    public Node addPasoTerminal(String element, String atributo, Integer value, Object object, Boolean haveBrother, Node nodeAnt){
        return addPasoTerminal(element, atributo, value.toString(), object, haveBrother, nodeAnt);
    }
    public Node addPasoTerminal(String element, String atributo, Object object, Boolean haveBrother, Node nodeAnt){
        Class claseAnt=getClass(object);
        
        Paso paso=addPaso(true,element, element, null, null,nodeAnt.getId());
        Node node=addNode(element, true,haveBrother);
        node.setFatherNode(nodeAnt);
        setNode(claseAnt,object,node);
        setPaso(claseAnt, object, paso);
        return node;
    }
     /**
     * add a lambda step
     * @param element
     * element of the father step
     * @param atributo
     * name of the attribute of the father step
     * @param haveBrother
     * @param nodeAnt
     * @param object
     * no terminal related with the father step
     * @return
     * the added step
    **/
    public Node addPasoNoTerminal(String element, String atributoHer, String atributoSint, Object object, Boolean haveBrother, String her, Node nodeAnt){
        
        
        Node nodo=null;
        Paso paso=null;
        Class claseAnt=getClass(object);
        if(nodeAnt==null){
            nodo=addNode(element, false, haveBrother);
            paso=addPasoPrimero(element, null, null);
        }
        else{
            
            
            
                nodo=addNode(element, false, haveBrother);
               if(atributoHer==null){
                    paso=addPaso(false,null,element,element+"."+atributoSint+"=null", null, nodeAnt.getId());     
                }
                else
                    paso=addPaso(false,null,element, element+"."+atributoHer+"="+her+" "+element+"."+atributoSint+"=null", null, nodeAnt.getId());                        
            
        }
        nodo.setFatherNode(nodeAnt);
        setNode(claseAnt,object,nodo);
        setPaso(claseAnt, object, paso);
        return nodo;
    }
    /**
     * add a new step to the step list
     * @param isDisplacement
     * true if is displacement false if not
     * @param elementoLeido
     * read element of the chain if exist 
     * @param element
     * element that is being processed
     * @param valor
     * value of the element
     * @param regla
     * rule of the element (only if the symbol is the first of the rule)
     * @param relNodo
     * father node
     * @return 
     * the new step
     */
    public Paso addPaso(Boolean isDisplacement,String elementoLeido,String element, String valor, String regla,HashSet<Integer> relNodo){
        Paso paso=null;
        if(elementoLeido!=null){
             readChain+=pendChain.pop()+" ";
        }
        String pendiente=writePendChain();
        if(isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "despDes", readChain, pendiente, element, valor, relNodo, regla);
        else if(isDisplacement && !traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "desplazamiento", readChain, pendiente, element, valor, relNodo, regla);
        else if(!isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "derivacion", readChain, pendiente, element, valor, relNodo, regla);
        else 
            paso=new Paso(this.pasoCount, "reduccion", readChain, pendiente, element, valor, relNodo, regla);
        steps.add(paso);
        stepMaps.put(this.pasoCount, paso);
        pasoCount++;
        return paso;
    } 
    /**
     * add a new step to the step list
     * @param isDisplacement
     * true if is displacement false if not
     * @param elementoLeido
     * read element of the chain if exist 
     * @param element
     * element that is being processed
     * @param valor
     * value of the element
     * @param regla
     * rule of the element (only if the symbol is the first of the rule)
     * @return 
     * the new step
     */
    public Paso addPaso(Boolean isDisplacement,String elementoLeido,String element, String valor, String regla){
        Paso paso=null;
        if(elementoLeido!=null){
             readChain+=pendChain.pop()+" ";
        }
        String pendiente=writePendChain();
        if(isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "despDes", readChain, pendiente, element, valor, null, regla);
        else if(isDisplacement && !traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "desplazamiento", readChain, pendiente, element, valor, null, regla);
        else if(!isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "derivacion", readChain, pendiente, element, valor, null, regla);
        else 
            paso=new Paso(this.pasoCount, "reduccion", readChain, pendiente, element, valor, null, regla);
        steps.add(paso);
        stepMaps.put(this.pasoCount, paso);
        pasoCount++;
        return paso;
    } 
    /**
     * add a new step to the step list
     * @param element
     * element that is being processed
     * @param valor
     * value of the element
     * @param regla
     * rule of the element (only if the symbol is the first of the rule)
     * @return 
     * the new step
     */
    public Paso addPasoPrimero(String element, String valor, String regla){
        String pendiente=writePendChain();
        Paso paso=new Paso(this.pasoCount, "primero", readChain, pendiente, element, valor, null, regla);
        steps.add(paso);
        stepMaps.put(this.pasoCount, paso);
        pasoCount++;
        return paso;
    } 
    /**
     * 
     * add a new step to the step list
     * @param isDisplacement
     * true if is displacement false if not
     * @param elementoLeido
     * read element of the chain if exist 
     * @param element
     * element that is being processed
     * @param valor
     * value of the element
     * @param regla
     * rule of the element (only if the symbol is the first of the rule)
     * @param relNodo
     * related nodes or parents nodes
     * @return 
     * the new step
     */
    public Paso addPaso(Boolean isDisplacement,String elementoLeido,String element, String valor, String regla,Integer relNodo){
        HashSet<Integer> relNodes=new HashSet<>();
        relNodes.add(relNodo);
        if(elementoLeido!=null){
             readChain+=pendChain.pop()+" ";
        }
        String pendiente=writePendChain();
        Paso paso=null;
        if(isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "despDes", readChain, pendiente, element, valor, relNodes, regla);
        else if(isDisplacement && !traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "desplazamiento", readChain, pendiente, element, valor, relNodes, regla);
        else if(!isDisplacement && traductorType.equals("Descendente"))
            paso=new Paso(this.pasoCount, "derivacion", readChain, pendiente, element, valor, relNodes, regla);
        else 
            paso=new Paso(this.pasoCount, "reduccion", readChain, pendiente, element, valor, relNodes, regla);
        steps.add(paso);
        stepMaps.put(this.pasoCount, paso);
        pasoCount++;
        return paso;
    } 
    /**
     * write the XML
     * @return 
     * true if the operation is successful false if not
     */
    public Boolean writeXML(){
        writeTraductor();
        writeArbol();
        writeContenido();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer=null;
            try {
                transformer = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(pathResult+".xml"));
            try {
                transformer.transform(source, result);
            } catch (TransformerException ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        // Output to console for testing
        StreamResult consoleResult = new StreamResult(System.out);
            try { 	
                transformer.transform(source, consoleResult);
            } catch (TransformerException ex) {
                Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        return true;
    }

 
    /**
     * remove the semantics actions of a production
     * @param production
     * production where remove the semantics actions
     * @return
     * production without actions
     */
    private String removeActions(String production){
        String[] symbols=production.split(" ");
        String result="";
        for(int i=0;i<symbols.length;i++){
            String symbol=symbols[i];
            if(!symbol.startsWith("{")){
                if(grammar.containsKey(symbol)){
                    result+=symbol+" ";
                }
                else{
                    result+=symbol.substring(0, getNumberIndex(symbol))+" ";
                }
            }
        }
        return result.substring(0, result.length()-1);
    }
    /**
     * Remove the semantics actions only
     * @param production
     * production where remove the actions
     * @return 
     * a production without the actions
     */
    private String removeOnlyActions(String production){
        String[] symbols=production.split(" ");
        String result="";
        for(int i=0;i<symbols.length;i++){
            String symbol=symbols[i];
            if(!symbol.startsWith("{")){
                result+=symbol+" ";
                
                
            }
        }
        return result.substring(0, result.length()-1);
    }
    /**
     * find the position where finnish the letters and begin the digits 
     * @param symbol
     * Symbol to find index
     * @return 
     * index of the symbol
     */
    private Integer getNumberIndex(String symbol){
        char[] letters=symbol.toCharArray();
        Integer index=letters.length;
        for(int i=0;i<letters.length;i++){
            if(Character.isDigit(letters[i])){
               index=i;
               return index;
            }
        }
        return index;
    }
    
         
       
    /**
    * write the part of <traductor> and <cadena> in a xml
    */
    private void writeTraductor() {
        Element traductor = doc.createElement("traductor");
        espec.appendChild(traductor); 
        Element tipo= doc.createElement("tipo");
        traductor.appendChild(tipo);
        tipo.setTextContent(traductorType);
        for(String antecedent:antecedentes){
            for(String production:grammar.get(antecedent)){
              addRule(antecedent,production,traductor);  
            }
        }
        Element cadena = doc.createElement("cadena");
        espec.appendChild(cadena);
        cadena.setTextContent(readChain+writePendChain());
    }
    /**
     * write a rule in xml
     * @param antecedent
     * antecendent of the rule
     * @param production
     * production of the antecedent to write
     * @param traductor 
     * element where to add the rule in the xml
     */
    private void addRule(String antecedent ,String production,Element traductor) {
        String id="R"+ruleCount;
        ruleId.put(removeOnlyActions(production), id);
        Element regla=doc.createElement("regla");
        traductor.appendChild(regla);
        Attr attrRegla = doc.createAttribute("id");
	attrRegla.setValue(id);
        regla.setAttributeNode(attrRegla);
        ArrayList<String> actions=actions(production);
        for(String action:actions){
            Element actionXml=doc.createElement("accionSemantica");
            regla.appendChild(actionXml);
            Integer pos=getPos(action,production);
            Attr attrAccion = doc.createAttribute("pos");
            attrAccion.setValue(pos.toString());
            actionXml.setAttributeNode(attrAccion);
            actionXml.setTextContent(action.substring(1,action.length()-1));
            if(pos<removeActions(production).split(" ").length){
                Element intermedio=doc.createElement("intermedio");
                actionXml.appendChild(intermedio);
                intermedio.setTextContent("si");
                
            }
            
        }
        addSymbols(production,regla,antecedent);
        ruleCount++;
    }
    /**
     * group the actions of one production
     * @param production
     * production where the actions are
     * @return 
     * A ArrayList with the actions
     */
    private ArrayList<String> actions(String production) {
        ArrayList<String> result=new ArrayList<>();
        String[] actions=production.split(" ");
        for(int i=1;i<actions.length;i++){
            if(actions[i].contains("{")){
                result.add(actions[i]);
            }
                
        }
        return result;
    }
    /**
     * obtain the position of the action in the production
     * @param action
     * action to find
     * @param production
     * production where find
     * @return 
     * the index of the symbol before the action
     */
    private Integer getPos(String action, String production) {
        Integer pos=0;
        int i=0;
        String[] symbols=production.split(" ");
        while(!symbols[i].equals(action)){
           
            if(!symbols[i].contains("{"))
                pos++;
            i++;
        }
        return pos;
    }
    /**
     * write the symbols of one rule in the xml
     * @param production
     * production where the symbols are
     * @param regla
     * element where to add the symbols in the xml
     * @param antecedente 
     * antecedent of the production
     */
    private void addSymbols(String production, Element regla,String antecedente) {
        String[] symbols=production.split(" ");
        for(int i=0;i<symbols.length;i++){
            if (i==0){
                Element simbolo=doc.createElement("simbolo");
                regla.appendChild(simbolo);
                Element valor=doc.createElement("valor");
                simbolo.appendChild(valor);
                if(grammar.get(antecedente).get(0).equals(production))
                    valor.setTextContent(antecedente+"::=");
                else
                    valor.setTextContent("|");
                Element terminal=doc.createElement("terminal");
                simbolo.appendChild(terminal);
                
                terminal.setTextContent("false");
                
            }
            else if(!symbols[i].contains("{")){
                Element simbolo=doc.createElement("simbolo");
                regla.appendChild(simbolo);
                Element valor=doc.createElement("valor");
                simbolo.appendChild(valor);
                valor.setTextContent(symbols[i]);
                Element terminal=doc.createElement("terminal");
                simbolo.appendChild(terminal);
                if(Character.isUpperCase(symbols[i].charAt(0)))
                    terminal.setTextContent("false");
                else
                    terminal.setTextContent("true");
            }
        }
    }
    /**
     * write the part of <arbol> of the xml 
     */
    private void writeArbol() {
       Integer altura=updateNode();
       Element arbol = doc.createElement("arbol");
       espec.appendChild(arbol);
       Element numNodosE = doc.createElement("num_nodos");
       arbol.appendChild(numNodosE);
       numNodosE.setTextContent(nodes.size()+"");
       Element alturaE = doc.createElement("altura");
       arbol.appendChild(alturaE);
       alturaE.setTextContent(altura.toString());
       for(Node node:nodes){
           Element nodo = doc.createElement("nodo");
           Attr id = doc.createAttribute("id");
           id.setValue(node.getId()+"");
           nodo.setAttributeNode(id);
           arbol.appendChild(nodo);
           Element element = doc.createElement("elemento");
           nodo.appendChild(element);
           element.setTextContent(node.getElement());
           Element level = doc.createElement("nivel");
           nodo.appendChild(level);
           level.setTextContent(node.getNivel().toString());
           Element terminal = doc.createElement("terminal");
           nodo.appendChild(terminal);
           terminal.setTextContent(node.getTerminal().toString());
       }
    }
  
   
   
    /**
     * write the part of <contenido> of the xml
     */
    private void writeContenido( ) {
        Element contenido = doc.createElement("contenido");
        espec.appendChild(contenido);
        for(Paso step:steps){
            Element pasoE = doc.createElement("paso");
            Attr id = doc.createAttribute("id");
            id.setValue(step.getId()+"");
            pasoE.setAttributeNode(id);
            contenido.appendChild(pasoE);
            Element tipo = doc.createElement("tipo");
            tipo.setTextContent(step.getTipo());
            pasoE.appendChild(tipo);
            if(step.getRegla()!=null){
                Element nuevaRegla = doc.createElement("nuevaRegla");
                Attr refRegla = doc.createAttribute("refRegla");
                refRegla.setValue(ruleId.get(step.getRegla()));
                nuevaRegla.setTextContent(step.getRegla());
                nuevaRegla.setAttributeNode(refRegla); 
                pasoE.appendChild(nuevaRegla); 
                
            }

            Element cadena = doc.createElement("cadena");
            pasoE.appendChild(cadena);
            Element leido = doc.createElement("leido");
            leido.setTextContent(step.getLeido());
            cadena.appendChild(leido);
            Element pendiente = doc.createElement("pendiente");
            pendiente.setTextContent(step.getPendiente());
            cadena.appendChild(pendiente);
            Element elemento = doc.createElement("elemento");
            elemento.setTextContent(step.getElemento());
            pasoE.appendChild(elemento);
            if(step.getRelNodo()!=null){
                Element relNodos = doc.createElement("relNodos");
                String relNodes=writeRelNodes(step.getRelNodo());
                relNodos.setTextContent(relNodes);
                pasoE.appendChild(relNodos);
            }
            Element valor = doc.createElement("valor");
            valor.setTextContent(step.getValor());
            pasoE.appendChild(valor);
            if(!step.getChangedNodes().isEmpty()){
                Element accionSemanticaEjecutada = doc.createElement("accionSemanticaEjecutada");
                pasoE.appendChild(accionSemanticaEjecutada);
                for(int i=0;i<step.getChangedNodes().size();i++){
                    Element nodo = doc.createElement("nodo");
                    accionSemanticaEjecutada.appendChild(nodo);
                    Element refNodo = doc.createElement("refNodo");
                    refNodo.setTextContent(step.getChangedNodes().get(i).toString());
                    nodo.appendChild(refNodo);
                    Element atributos = doc.createElement("atributos");
                    atributos.setTextContent(step.getChanges().get(i));
                    nodo.appendChild(atributos);
                    
                }
            }
        }
    }
    /**
     * transform the HashSet relNode into a String
     * @param relNodo
     * Hashset to transform
     * @return 
     * the String of relational nodes
     */
    private String writeRelNodes(HashSet<Integer> relNodo) {
        String result="";
        for(Integer node:relNodo){
            result+=" "+node;
        }
        return result.substring(1);
    }
    /**
     * read the chain
     * @param entryChainPath
     * path of the file .txt with the chain
     */
    private void readChain(String entryChainPath) {
       readChain="";
       try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(entryChainPath), "UTF-8"))) { 
	            String line;
                    int contador=0;
	            while ((line = br.readLine()) != null) {
                        String[] chainElements=line.split(" ");
                        for(int i=chainElements.length-1;i>=0;i--){
                            pendChain.push(chainElements[i]);
                        }
                    }
        }
        catch (IOException e) {
	    e.printStackTrace();
	} 
    }
    /**
     * transform the pendingChain Stack into a String 
     * @return 
     * pending chain in form of String
     */
    private String writePendChain() {
        String result="";
        for(String elem:pendChain){
            result=elem+" "+result;
        }
        return result.substring(0,result.length());
    }

    public Integer getPasoCount() {
        return pasoCount;
    }
    /**
     * update the level in the sintactic tree of the nodes
     * @return 
     * the heigth of the sintactic tree
     */
    private Integer updateNode(){
        Paso raiz;
        if(this.traductorType.equals("Ascendente")) 
            raiz=steps.get(steps.size()-1);
        else
            raiz=steps.get(0);
        Integer altura=1;
        for(Node nodo:nodes){
            Integer nivel=distanciaARaiz(raiz,nodo);
            nodo.setNivel(nivel);
            altura=Math.max(altura, nivel);
        }
        return altura;
    }
    /**
     * Calculate the distance between the node and the root of the sintactic tree
     * @param raiz
     * root of the sintactic tree
     * @param objetivo
     * node to start
     * @return 
     * distance between the node and the root of the sintactic tree in form of Integer
     */
    private Integer distanciaARaiz(Paso raiz, Node objetivo) {
        Integer i=1;
        Node actual=objetivo;
        while (!actual.getId().equals(raiz.getId())){
            i++;
            actual=actual.getFatherNode();
        }
        return i;
        
    }
    /**
     * return the step corresponding with the id 
     * @param id
     * id of the step
     * @return 
     * step 
     */
    public Paso getStep(Integer id){
        return stepMaps.get(id);
        
    }
    public HashMap<String, ArrayList<String>> getGrammar() {
        return grammar;
    }

    public String getPath() {
        return path;
    }

    public String getPathResult() {
        return pathResult;
    }

    public Element getEspec() {
        return espec;
    }

    public Document getDoc() {
        return doc;
    }

    public Integer getRuleCount() {
        return ruleCount;
    }

    public Integer getNumNodos() {
        return numNodos;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Paso> getSteps() {
        return steps;
    }

    public String getTraductorType() {
        return traductorType;
    }
    /**
     * save the updated values of the steps whose value depend of this step
     * @param paso
     * step whose save the values 
     * @param node
     * node process in this step
     * @param value 
     * value to updated
     */
    public void updatesValues(Paso paso,Node node, String value){
        Node nodeAux=node.getFatherNode();
        while (!nodeAux.getHaveBrother()){
            paso.getChangedNodes().add(nodeAux.getId());
            Paso pasoAux =getStep(nodeAux.getId());
            String values=pasoAux.getValor();

            values=values.replace("null",value );
            paso.getChanges().add(values); 
            nodeAux=nodeAux.getFatherNode();
        }
        paso.getChangedNodes().add(nodeAux.getId());
        Paso pasoAux =getStep(nodeAux.getId());
        String values=pasoAux.getValor();

        values=values.replace("null",value );
        paso.getChanges().add(values); 
            
        
    }
    /**
     * obtain the class of an object
     * @param ob
     * object to obtain the class
     * @return 
     * the class of the object
     */    
    private Class getClass( Object ob){
        Class result=null;
        try {
            result=Class.forName(ob.getClass().getCanonicalName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }  

        return result;
    }
    /**
     * obtain the method designed of the class
     * @param clase
     * class where search
     * @param methodName
     * name of the method to search
     * @return 
     * the method
     */
    private Method getMethod(Class clase, String methodName) {
        Method method=null;
        Method[] methods=clase.getMethods();
        try {
            
            method=clase.getMethod(methodName);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return method;
    }
    /**
     * search the setId method
     * @param clase
     * class where search
     * @param methodName
     * method name
     * @return
     * method setId
     */
    private Method getMethodSetId(Class clase, String methodName) {
        Method method=null;
        try {
            
            method=clase.getMethod(methodName,Integer.class);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return method;
    }
    /**
     * get the id of the object
     * @param id
     * method get id
     * @param object
     * object where get id
     * @return
     * id of the object
     */
    private Integer getId(Method id,Object object) {
        Integer result=null;
        try {
            result =(Integer) id.invoke(object);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    /**
     * get the node of the object
     * @param node
     * method getNode
     * @param object
     * object where get node
     * @return 
     */
    private Node getNode(Method node, Object object) {
        Node result=null;
        try {
            result =(Node) node.invoke(object);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    /**
     * get the step of the object
     * @param paso
     * method getPaso
     * @param object
     * object where get step
     * @return 
     * the step of the object
     */
    private Paso getPaso(Method paso, Object object) {
        Paso result=null;
        try {
            result =(Paso) paso.invoke(object);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        }
    /**
     * set the id to the object
     * @param setId
     * method setId
     * @param object
     * object to set id
     * @param id 
     * id to set
     */
    private void setId(Method setId, Object object, Integer id) {
        try {
            setId.invoke(object,id);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * get the value of the object
     * @param value
     * method getValue
     * @param object
     * object to get the value
     * @return 
     * the value of the object
     */
    private String getValue(Method value, Object object) {
        String result=null;
        try {
            if(value.invoke(object)!=null)
            result =value.invoke(object).toString();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result; }
    /**
     * set node to the object
     * @param claseAnt
     * class of the object
     * @param object
     * object where set node
     * @param nodo 
     * node to set
     */
    private void setNode(Class claseAnt, Object object, Node nodo) {
        Method method=null;
        
        try {
            
            method=claseAnt.getMethod("setNode",nodo.getClass());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        try {
            method.invoke(object,nodo);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * set the step of the object
     * @param claseAnt
     * class of the object
     * @param object
     * object to set the step
     * @param paso 
     * step to set
     */
    private void setPaso(Class claseAnt, Object object, Paso paso) {
        Method method=null;
        
        try {
            
            method=claseAnt.getMethod("setPaso",paso.getClass());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        try {
            method.invoke(object,paso);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * write a rule with the objects
     * @param objects
     * objects of the rule
     * @return 
     * a string with the rule
     */
    private String buildRule(Object[] objects) {
        String result=objects[0].getClass().getSimpleName()+"::=";
        for(int i=1;i<objects.length;i++){
            if(objects[0].equals(objects[i]))
                result+=" "+objects[i].getClass().getSimpleName()+"1";
            else
                result+=" "+objects[i].getClass().getSimpleName();
        }
        return result+" ;";
    }

    private void setValue(Class claseAnt, Object object, String value) {
     Method method=null;
        
        try {
            
            method=claseAnt.getMethod("setValue",String.class);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }   
        try {
            method.invoke(object,value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateNoTerminals(String regla, String valor, Object antecedente, Object primerSimbolo){
        
        Class clase=getClass(primerSimbolo);
        Method getPaso=getMethod(clase, "getPaso");
        Paso paso=getPaso(getPaso, primerSimbolo);
        //regla=processRule(regla, paso.getElemento());
        paso.setRegla(removeOnlyActions(regla)); 
        Class claseAnt=getClass(antecedente);
        setValue(claseAnt, antecedente, valor);
        
    }
    public void updateNoTerminals(String regla, Integer valor, Object antecedente, Object primerSimbolo){
        updateNoTerminals(regla, valor.toString(), antecedente, primerSimbolo);       
    }

    private String processRule(String rule, String element) {
        String newRule="";
        String[] supSymbols=rule.split(" ");
        ArrayList<String> rules;
        if(grammar.get(element)==null){
            rules=new ArrayList<>();
            grammar.put(element, rules);
        }
        else{
            rules=grammar.get(element);
        }
        rules.add(rule);
        for(int i=0;i<supSymbols.length;i++){
            if(!(supSymbols[i].contains("{") && supSymbols[i].contains("{"))){
                newRule+=supSymbols[i]+" ";
            }
        }
        return newRule;
    }

    private void readGramatica(String path) {
      try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            while ((line = br.readLine()) != null) {
                String rule="";
                String antecedent="";
                if(line.contains("addPasoLambda")){
                    String parameters=line.split("\\(")[1];
                    antecedent=parameters.split(",")[0].substring(1,parameters.split(",")[0].length()-1);
                    if(traductorType.equals("Ascendente"))
                        rule=antecedent+"::= λ "+line.split(",")[3].substring(2,line.split(",")[3].length()-1);
                    else
                        rule=antecedent+"::= λ "+line.split(",")[4].substring(2,line.split(",")[4].length()-1);
                    if(!antecedentes.contains(antecedent))
                        antecedentes.add(antecedent);
                    ArrayList<String> rules;
                    if(grammar.get(antecedent)==null){
                        rules=new ArrayList<>();
                        grammar.put(antecedent, rules);
                    }
                    else{
                        rules=grammar.get(antecedent);
                    }
                    rules.add(rule);
                }
                if(line.contains("addPasoNoTerminal") && traductorType.equals("Ascendente")){
                    String parameters=line.split("\\(")[1];
                    
                    rule=line.split(",")[3].substring(2,line.split(",")[3].length()-1);
                    antecedent=parameters.split(",")[0].substring(1,parameters.split(",")[0].length()-1);
                    if(!antecedentes.contains(antecedent))
                        antecedentes.add(antecedent);
                    ArrayList<String> rules;
                    if(grammar.get(antecedent)==null){
                        rules=new ArrayList<>();
                        grammar.put(antecedent, rules);
                    }
                    else{
                        rules=grammar.get(antecedent);
                    }
                    rules.add(rule);
                }
                if(line.contains("updateNoTerminals")){
                    String aux=line.split("\"")[1];
                    //String parameters=aux.split("\\(")[1];
                    
                    rule=aux;
                    antecedent=aux.split("::=")[0];
                    if(!antecedentes.contains(antecedent))
                        antecedentes.add(antecedent);
                    ArrayList<String> rules;
                    if(grammar.get(antecedent)==null){
                        rules=new ArrayList<>();
                        grammar.put(antecedent, rules);
                    }
                    else{
                        rules=grammar.get(antecedent);
                    }
                    rules.add(rule);
                }
                
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
