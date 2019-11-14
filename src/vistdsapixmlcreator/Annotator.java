/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistdsapixmlcreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adgao
 */
public class Annotator {
    private HashSet<String> terminalsWithValue;
    private String destino;
    public Annotator(String grammar, String destino, String nombreXML, String main) {
        File newFichero = new File(destino+"/");
        this.destino=destino;
        newFichero.mkdir();
        if(grammar.split("\\.")[1].equals("cup")){
            writeCup(grammar, destino, nombreXML, main);
        }
        else{
            writeAntlr(grammar, destino, nombreXML, main);
        }
    }
    private void writeCup(String grammar, String destino, String nombreXML, String main){
        String nombreArchivoEntrada=main.split("\\\\")[main.split("\\\\").length-1];
        String nombreArchivoMain=nombreArchivoEntrada.split("\\.")[0];
        String[] type=grammar.split("\\.");
        File newFichero = new File(destino+"/"+grammar);
        HashSet<String> terminals=new HashSet<>();
       // HashSet<String> noTerminals=new HashSet<>();
        terminalsWithValue=new HashSet<>();
        try {
            newFichero.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(grammar)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
            String line;
            Boolean firtsTerminal=true;
            Boolean firtsNonTerminal=true;
            String antecedent="";
            String antecedentClass="";
            String[] symbols=null;
            while ((line = br.readLine()) != null) {

                String[] linea=line.split(" ");
                if(linea.length>0){
                    if( line.split(" ")[0].equals("terminal") && firtsTerminal){

                        bw.write("action code\n" +
                        "{:\n" +
                        "Writer writer = new Writer("+"\""+grammar+"\""+",\"./"+nombreXML+"\","+nombreArchivoMain+".getChain()"+",false);\n" +
                        ":}\n\n");
                        firtsTerminal=false;
                    }
                    if(line.split(" ")[0].equals("terminal") /*&&line.contains(",")*/){
                        terminals.addAll(getTerminals(line));
                    }
                    if( line.split(" ")[0].equals("non") && firtsNonTerminal){
                        String newNoTerminals=newNoTerminals(terminals);
                        bw.write(newNoTerminals);
                        firtsNonTerminal=false;
                    }
                    if(line.split(" ")[0].equals("non") /*&&line.contains(",")*/){
                        String noTerminal=getNoTerminals(line);
                        //noTerminals.add(noTerminal);
                        ArrayList<String> listTypes=new ArrayList<>();
                        listTypes.add("Integer");
                        listTypes.add("Double");
                        listTypes.add("Boolean");
                        listTypes.add("Float");
                        listTypes.add("String");
                        listTypes.add("Character");
                        if(listTypes.contains(line.split(" ")[2])){
                            
                            line=line.replace(line.split(" ")[2],line.split(" ")[3].substring(0, 1)+line.split(" ")[3].substring(1, line.split(" ")[3].length()-1).toLowerCase());
                        }
                            
                        if(noTerminal.length()>1){
                            noTerminal=noTerminal.substring(0,1)+noTerminal.substring(1,noTerminal.length()).toLowerCase();
                        }
                        createClass(noTerminal);
                    }
                    if(line.contains(antecedent)&& line.contains("new")){
                        line="";
                    }
                    if (line.contains("::=") && !line.contains("writer")){
                        symbols=line.split("::=")[1].split(" ");
                        
                        antecedent=line.split("::=")[0];
                        antecedentClass=antecedent.substring(0,1)+antecedent.substring(1,antecedent.length()).toLowerCase();
                        line=line.split("::=")[0]+"::="+addNameToSymbols(symbols)+"\n\t"+antecedentClass+" "+antecedent.toLowerCase()+"=new "+antecedentClass+"();";
                    }
                    if (line.contains("|") && !line.contains("writer")){
                        String[] aux=line.split("\\|");
                        symbols=line.split("\\|")[1].split(" ");
                        line="|"+addNameToSymbols(symbols)+"\n\t"+antecedentClass+" "+antecedent.toLowerCase()+"=new "+antecedentClass+"();";

                    }
                    if (line.contains("addPasoNoTerminal")){
                        line=addSymbolsObjects(line, antecedent, symbols);
                        
                    }
                    if (line.contains("addPasoLambda")){
                        line=addSymbolsObjects(line, antecedent);
                        
                    }
                    if(line.contains("RESULT=")){
                        line=line.split("=")[0]+"="+antecedent.toLowerCase()+";";
                    }
                    if(line.split(" ")[0].equals("import")){
                        line+="\nimport vistdsapixmlcreator.Writer;\n" +
                        "import vistdsapixmlcreator.Paso;\n" +
                        "import vistdsapixmlcreator.Node;";
                    }
            }
//                    else if(line.split(" ").length>0 && line.split(" ")[0].equals("terminal")){
//                        terminals.add(line.substring(0,line.length()-1).split(" "))
//                    }
            bw.write(line+"\n");
               // line.substring(8).split(", ")
            }
            bw.write(noTerminalsOfTerminals(terminals));
            br.close();
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private HashSet<String> getTerminals(String line) {
        Boolean needValue=false;
        HashSet<String> terminals=new HashSet<>();
        String[] auxLine=line.substring(8).split(" ");
        if( !auxLine[1].contains(",") && !auxLine[1].contains(","))
            needValue=true;
        for(int i=1;i<auxLine.length;i++){
            if(auxLine[i].charAt(auxLine[i].length()-1)==','||auxLine[i].charAt(auxLine[i].length()-1)==';'){
                terminals.add(auxLine[i].substring(0,auxLine[i].length()-1));
                if(needValue){
                    terminalsWithValue.add(auxLine[i].substring(0, auxLine[i].length()-1));
                }
            }
            
        }

        return terminals;
    }
    private String getNoTerminals(String line) {
        String noTerminals="";
        String[] auxLine=line.substring(12).split(" ");
        for(int i=1;i<auxLine.length;i++){
            if(auxLine[i].charAt(auxLine[i].length()-1)==';'){
                noTerminals=auxLine[i].substring(0,auxLine[i].length()-1);
            }
        }

        return noTerminals;
    }
    private String newNoTerminals(HashSet<String> terminals) {
        String noTerminals="";
        for(String s:terminals){
            String className=s.substring(0,1).toUpperCase()+s.substring(1,s.length());
            noTerminals+="non terminal "+className+" "+s.toUpperCase()+";\n";
            createClass(className);
        }
        return noTerminals;
    }

    private void createClass(String s) {
       
        File newFichero = new File(destino+"/"+s+".java");
        try {
            newFichero.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
            bw.write("/*\n" +
            " * To change this license header, choose License Headers in Project Properties.\n" +
            " * To change this template file, choose Tools | Templates\n" +
            " * and open the template in the editor.\n" +
            " */\n" +
            "import vistdsapixmlcreator.Node;\n" +
            "import vistdsapixmlcreator.Paso;\n" +
            "\n" +
            "/**\n" +
            " *\n" +
            " * @author Adrian Garcia Oller\n" +
            " */\n" +
            "public class "+s+" {\n" +
            "\n" +
            "    private String value;\n" +
            "    private Integer id;\n" +
            "    \n" +
            "    private Paso paso;\n" +
            "    private Node node;\n" +
            "    public "+s+"() {\n" +
            "    }\n" +
            "\n" +
            "    \n" +
            "\n" +
            "    public Integer getId() {\n" +
            "        return id;\n" +
            "    }\n" +
            "\n" +
            "    public void setId(Integer id) {\n" +
            "        this.id = id;\n" +
            "    }\n" +
            "\n" +
            "    \n" +
            "\n" +
            "    public Paso getPaso() {\n" +
            "        return paso;\n" +
            "    }\n" +
            "\n" +
            "    public void setPaso(Paso paso) {\n" +
            "        this.paso = paso;\n" +
            "    }\n" +
            "\n" +
            "    public Node getNode() {\n" +
            "        return node;\n" +
            "    }\n" +
            "\n" +
            "    public void setNode(Node node) {\n" +
            "        this.node = node;\n" +
            "    }\n" +
            "\n" +
            "    public String getValue() {\n" +
            "        return value;\n" +
            "    }\n" +
            "\n" +
            "    public void setValue(String value) {\n" +
            "        this.value = value;\n" +
            "    }\n" +
            "    \n" +
            "}");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * 
     * @param symbols
     * @return 
     */
    private String addNameToSymbols(String[] symbols) {
        String result="";
        for(int i=1;i<symbols.length-1;i++){
            if(!symbols[i].contains(":")){
                if(symbols[i].substring(0,1).toLowerCase().equals(symbols[i].substring(0,1))){
                    symbols[i]=symbols[i].toUpperCase();
                }
            
                result+=" "+symbols[i]+":"+symbols[i].toLowerCase();
            }
            else
                result+=" "+symbols[i].split(":")[0].toUpperCase()+":"+symbols[i].split(":")[1];
       }
        return result+" {:";
    }

    private String noTerminalsOfTerminals(HashSet<String> terminals) {
        String result="";
        
        for(String terminal:terminals){
            String identifier=terminal+"id";
            String className=terminal.substring(0,1).toUpperCase()+terminal.substring(1,terminal.length());
            if(terminalsWithValue.contains(terminal)){
                
                result+=terminal.toUpperCase()+" ::= "+terminal+":"+identifier+" {:\n" +
                "    "+className+" "+terminal.substring(0,1)+"=new "+className+"();\n"+ 
                "    writer.addPasoTerminal("+"\""+terminal+"\""+", \"vlex\", "+identifier+", "+terminal.substring(0,1)+");\n" +
                "    RESULT="+terminal.substring(0,1)+";\n" +
                ":};\n\n";
            }
            else{
                result+=terminal.toUpperCase()+" ::= "+terminal+":"+identifier+" {:\n" +
                "    "+className+" "+terminal.substring(0,1)+"=new "+className+"();\n"+ 
                "    writer.addPasoTerminal("+identifier+".toString()"+", null, "+terminal.substring(0,1)+");\n" +
                "    RESULT="+terminal.substring(0,1)+";\n" +
                ":};\n\n";
            }
        }
        return result;
    }

    private void writeAntlr(String grammar, String destino, String nombreXML, String main) {
        String nombreArchivoEntrada=main.split("\\\\")[main.split("\\\\").length-1];
        String nombreArchivoMain=nombreArchivoEntrada.split("\\.")[0];
        File newFichero = new File(destino+"/"+grammar);
        String terminals="";
        String noTerminals="";
        try {
            newFichero.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(grammar)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
            String line;
            terminalsWithValue=new HashSet<>();
            Boolean firstLine=true;
            Boolean isFirstNoTerminal=true;
            Boolean firstStep=true;
            Boolean beforeFirtsNoTerminal=true;
            Boolean haveHeader=false;
            Boolean terminalsBegins=false;
            Boolean insideAction=false;
            Boolean insideNoTerminal=false;
            String antecedente="";
            Integer contCorch=0;
            Integer contActions=0;
            String antecedenteClass="";
            String varReturn="";
            Boolean hadLambdaStep=false;
            while ((line = br.readLine()) != null) {
                String[] aux=line.split(" ");
                
                if(line.split(" ").length>0 ){
                    if(!isFirstNoTerminal && !insideAction && !insideNoTerminal){
                        line=modifyAntecedent(line);
                    }
                    if(line.contains("returns")){
                        
                        ArrayList<String> listTypes=new ArrayList<>();
                        listTypes.add("Integer");
                        listTypes.add("Double");
                        listTypes.add("Boolean");
                        listTypes.add("Float");
                        listTypes.add("String");
                        listTypes.add("Character");
                        String[] aux1=line.split(" ");
                        if(listTypes.contains(line.split(" ")[2].substring(1))){
                            antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                            line=line.replace(line.split(" ")[2],"["+antecedenteClass);
                            
                            varReturn=line.split(" ")[3].split("]")[0];
                            
                        }
                        
                        else if (line.split(" ").length>7&&listTypes.contains(line.split(" ")[7].substring(1))){
                            
                            antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                            line=line.replace(line.split(" ")[7],"["+antecedenteClass);
                            
                            varReturn=line.split(" ")[8].split("]")[0];
                        }
                        else if(line.split(" ").length>6&&listTypes.contains(line.split(" ")[5].substring(1))){
                            antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                            aux=line.split(" ");
                            line=line.replace(line.split(" ")[5],"["+antecedenteClass);
                            varReturn=line.split(" ")[6].split("]")[0]; 
                        }
                        else{
                            if (line.split(" ").length>7){
                                antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                                varReturn=line.split(" ")[8].split("]")[0];  
                            }
                            else if(line.split(" ").length>6){
                                antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                                varReturn=line.split(" ")[6].split("]")[0]; 
                            }
                            else{
                                varReturn=line.split(" ")[3].split("]")[0];
                                antecedenteClass=line.split(" ")[0].substring(0, 1).toUpperCase()+line.split(" ")[0].substring(1, line.split(" ")[0].length());
                            }
                        }
                        isFirstNoTerminal=false;
                    }
                    if(line.split(" ")[0].equals("@header")){
                        haveHeader=true;
                        line+="\n    import vistdsapixmlcreator.Writer;\n" +
                        "    import vistdsapixmlcreator.Node;\n" +
                        "    import vistdsapixmlcreator.Paso;";
                    }
                    if(!firstLine&& !haveHeader && beforeFirtsNoTerminal && !line.split(" ")[0].equals("@")){

                        bw.write("@header {\n" +
                        "    import vistdsapixmlcreator.Writer;\n" +
                        "    import vistdsapixmlcreator.Node;\n" +
                        "    import vistdsapixmlcreator.Paso;\n" +
                        "}\n"+"@members {\n" +
                        "    Writer writer = new Writer(\""+grammar+"\",\"./"+nombreXML+"\","+nombreArchivoMain+".getChain()"+",true);\n" +
                        "}\n\n");
                        beforeFirtsNoTerminal=false;
                    }
                    if ( !line.equals("")&&!line.substring(0,1).equals(" ") && line.substring(0,1).toUpperCase().equals(line.substring(0,1))){
                        terminalsBegins=true;
                        String terminal=line.split(" ")[0];
                        String noTerminal=terminal.substring(0,1).toLowerCase()+terminal.substring(1, terminal.length());
                        if(terminalsWithValue.contains(noTerminal)){
                            noTerminals+=noTerminal+" [Node nodeAnt, Boolean haveBrother]  returns ["+terminal+" "+noTerminal+"O]\n" +
                            "    : "+terminal+" {\n" +
                            "        \n" +
                            "        "+terminal+" "+noTerminal+"O=new "+terminal+"();\n" +
                            "        writer.addPasoTerminal(\""+noTerminal+"\", \"vlex\", "+"Integer.parseInt(this._ctx.getText()), "+noTerminal+"O, haveBrother, nodeAnt);\n" +
                            "        \n" +
                            "        _localctx."+noTerminal+"O="+noTerminal+"O;\n" +
                            "    }\n" +
                            "    ;"+"\n";
                        }
                        else{
                            noTerminals+=noTerminal+" [Node nodeAnt, Boolean haveBrother]  returns ["+terminal+" "+noTerminal+"O]\n" +
                            "    : "+terminal+" {\n" +
                            "        \n" +
                            "        "+terminal+" "+noTerminal+"O=new "+terminal+"();\n" +
                            "        writer.addPasoTerminal("+"this._ctx.getText()"+", null, "+noTerminal+"O, haveBrother, nodeAnt);\n" +
                            "        \n" +
                            "        _localctx."+noTerminal+"O="+noTerminal+"O;\n" +
                            "    }\n" +
                            "    ;"+"\n";
                        }
                        createClass(terminal);
                    }
                    
                    if(!line.equals("")&&!line.substring(0,1).equals(" ")&&line.contains("returns")){
                        antecedente=line.split(" ")[0];
                        createClass(antecedente.substring(0,1).toUpperCase()+antecedente.substring(1,antecedente.length()));
                    }
                    
                    
                    
                    if(line.contains("{")){
                        
                        contCorch++;
                        if(contCorch==1){
                            contActions++; 
                        }
                        if(contCorch==1 && contActions%2!=0){
                            line+="\n\t\t"+antecedenteClass+" "+varReturn+"= new "+antecedenteClass+"();";
                        }
                        if(hadLambdaStep){
                            contActions--;
                            hadLambdaStep=false;
                        }
                        if(!insideAction)
                            insideAction=true;
                    }
                    if(line.contains(";")&&!insideAction){
                        insideNoTerminal=false;
                        
                    }
                    if(!insideAction && insideNoTerminal && !line.contains("|")){
                       
                       line=modifyProduction(line.split(" "));
                    }
                    if(line.contains("|")){
                        hadLambdaStep=true;
                    }
                    if(line.contains("_localctx.")){
                        line=line.split("=")[0]+"="+varReturn+";";
                    }
                    if(line.contains(":")&&!insideAction){
                        insideNoTerminal=true;
                    }
                    if(!line.contains("_localctx.")&&line.contains("Context)_localctx")&& !insideAction){
                        line=formatOperationANTLR(line);
                    }
                    
                    if(line.contains("}")){
                        contCorch--;
                        if(contCorch==0)
                            insideAction=false;
                    }
                    if(line.contains("addPaso")){
                        
                        line=addNodeAnt(line, firstStep, varReturn);//line.split("\\)")[0]+", nodeAnt)"+line.split("\\)")[1];
                        firstStep=false;
                    }
                    if(line.contains("updateNoTerminals")){
                        
                        line=line.substring(0,line.length()-2)+", "+varReturn+");";
                        if(!line.contains(".getValue()")){
                            line=line.replace(line.split(",")[1], line.split(",")[1]+".getValue()");
                        }
                    }
                if(!terminalsBegins){
                    bw.write(line+"\n");
                }
                else{
                    terminals+=line+"\n";
                }
                   // line.substring(8).split(", ")
                firstLine=false;
                }
            }
            bw.write(noTerminals);
            bw.write(terminals);
            
            br.close();
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String transformRule(String[] simbols) {
        String result="    ";
        for(int i=4;i<simbols.length;i++){
            if(simbols[i].contains(result)){
                
            }
//            result+=""
        }
        return result;
    }

    private String modifyProduction(String[] line) {
        String newLine="";
        Boolean insideAtHer=false;
        for(int i=0;i<line.length;i++){
//            if(line[i].contains("[")&& !line[i].contains("]")){
//                insideAtHer=true;
//            }
            
            
            if(!insideAtHer && !line[i].equals("")){
                if(line[i].contains("=")){
                    if(line[i].split("=")[1].substring(0,1).toUpperCase().equals(line[i].split("=")[1].substring(0,1))){
                        String terminal=line[i].split("=")[1].substring(0,1).toLowerCase()+line[i].split("=")[1].substring(1,line[i].split("=")[1].length());
                        terminalsWithValue.add(terminal);
                        if(i<line.length-1)
                                newLine+=line[i].split("=")[0]+"="+terminal+"[node, true] ";
                            else 
                                newLine+=line[i].split("=")[0]+"="+terminal+"[node, false] ";
                    }
                    else{
                        if(line[i].contains("[")){
                            String[] aux=line[i].split("\\[");
                            if(i<line.length-1)
                                newLine+=aux[0]+"[node, true, "+aux[1]+" ";
                            else 
                                newLine+=aux[0]+"[node, false, "+aux[1]+" ";
                        }
                        else{
                            if(i<line.length-1)
                                newLine+=line[i]+"[node, true] ";
                            else 
                                newLine+=line[i]+"[node, false] ";
                        }
                    }
                }
                else{
                    if(line[i].substring(0,1).toUpperCase().equals(line[i].substring(0,1))){
                        String var=line[i].substring(0,1).toLowerCase()+line[i].substring(1,line[i].length());
                        if(i<line.length-1)
                            newLine+=var+"O="+var+"[node, true] ";
                        else 
                            newLine+=var+"O="+var+"[node, false] ";
                    }
                    else{

                        if(line[i].contains("[")){
                        String[] var=line[i].split("[");
                            if(i<line.length-1)
                                newLine+=var[0]+"[node, true, "+var[1]+" ";
                            else 
                                newLine+=var[0]+"[node, false, "+var[1]+" ";
                        }
                        else{

                            if(i<line.length-1)
                                newLine+=line[i]+"[node, true] ";
                            else 
                                newLine+=line[i]+"[node, true] ";
                        }
                    }

                }
            }
            else{
                newLine+=line[i];
            }
            if(line[i].contains("[")){
                insideAtHer=true;
            }
            if(line[i].contains("]")){
                insideAtHer=false;
            }
        }
        return newLine;
    }

    private String modifyAntecedent(String line) {
        String newLine="";
        if(!line.equals("")){
            if(line.split("\\[").length>2){
                newLine+=line.split("\\[")[0]+"[Node nodeAnt,Boolean haveBrother,"+line.split("\\[")[1]+line.split("returns")[1];
                line=newLine;
            }
            else if(line.split("\\[").length==2){
                newLine+=line.split("returns")[0]+"[Node nodeAnt,Boolean haveBrother] returns"+line.split("returns")[1];
                line=newLine;
            }
        }
        return line;
    }

//    private String tranformsAction(String action, String atributte) {
//        String newAction="";
//        
//        return newAction;
//    }

    private String addNodeAnt(String line, Boolean firstStep, String result) {
        String newLine="";
        String[] paraLine=line.split(",");
        for(int i=0;i<paraLine.length;i++){
            if(i==paraLine.length-1){
                if(firstStep)
                    newLine+=paraLine[i].substring(0,paraLine[i].length()-2)+", "+result+", null, false);";
                else
                    newLine+=paraLine[i].substring(0,paraLine[i].length()-2)+", "+result+", nodeAnt, haveBrother);";
            }
            else
                newLine+=paraLine[i]+", ";
        }
        return newLine;
    }

    private String addSymbolsObjects(String line, String antecedent, String[] symbols) {
        String newLine="";
        String[] args=line.split(", ");
        for(int i=0;i<args.length;i++){
            if(i==2){
                
                String newValue=operationToObjectOperation(args[i]);
                
                args[i]=newValue;
            }
            newLine+=args[i]+", ";
            
        }    
        newLine=newLine.substring(0, newLine.length()-4)+", "+antecedent.toLowerCase()+getSymbolsOfProductions(symbols)+");";
        return newLine;
    }

    private String getSymbolsOfProductions(String[] symbols) {
        String allSymbols="";
        for(int i=0;i<symbols.length-1;i++){
            if(symbols[i].contains(":"))
                allSymbols+=symbols[i].split(":")[1].toLowerCase()+", ";
            else
                allSymbols+=symbols[i].split(":")[0].toLowerCase()+", ";
        }
        return allSymbols.substring(0, allSymbols.length()-2);
    
    }

    private String operationToObjectOperation(String operation) {
        String newValue="";
        String[] elements=operation.split(" ");
        if(!operation.contains("getValue()")){
            for(int j=0;j<elements.length;j+=2)
                        try{
                            Integer.parseInt(elements[j]);

                        }
                        catch (NumberFormatException excepcion) {
                            elements[j]="Integer.parseInt("+elements[j]+".getValue())";

                        }
        }
        for(int j=0;j<elements.length;j++){
            newValue+=elements[j];
        }
        
        return newValue;
    }

    private String addSymbolsObjects(String line, String antecedent) {
        String newLine="";
        String[] args=line.split(", ");
        for(int i=0;i<args.length;i++){
            if(i==2){
                
                String newValue=operationToObjectOperation(args[i]);
                
                args[i]=newValue;
            }
            newLine+=args[i]+", ";
            
        }    
        newLine=newLine.substring(0, newLine.length()-4)+", "+antecedent.toLowerCase()+");";
        return newLine;
    }

    private String formatOperationANTLR(String line) {
        String[] auxLine=line.split("\\[");
        String newLine=line;
        for(int i=0;i<auxLine.length;i++){
            if(auxLine[i].contains("Context)_localctx)")){
                String op=auxLine[i].split("]")[0];
                String[] operandos=op.split(" ");
                for(int j=0;j<operandos.length;j++){
                    if(!operandos[j].contains(".getValue()")){
                        if(operandos[j].contains("Context)_localctx)")){
                            String partsOperandos=operandos[j].split("\\.")[2];
                            if(operandos[j].contains("getText()")){
                                newLine=newLine.replace("getText()", partsOperandos+".getValue())");
                            }
                            else{
                                if(operandos[j].contains("Integer.parseInt(")){
                                    newLine=newLine.replace(operandos[j], operandos[j].substring(0,operandos[j].length() )+".getValue())");

                                }
                                else{
                                    newLine=newLine.replace(operandos[j], "Integer.parseInt("+operandos[j]+".getValue())");
                                }
                            }

                        }
                    }
                }
            }

        }
        return newLine;
    }
    
    
}
