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

    public Annotator(String grammar, String cadena, String destino) {
        File newFichero = new File(destino+"/production/");
        
        newFichero.mkdir();
        if(grammar.split("\\.")[1].equals("cup")){
            writeCup(grammar, cadena, destino);
        }
        else{
            writeAntlr(grammar, cadena, destino);
        }
    }
    private void writeCup(String grammar, String cadena, String destino){
        String[] type=grammar.split("\\.");
        File newFichero = new File(destino+"/production/gramatica.cup");
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
            while ((line = br.readLine()) != null) {

                String[] linea=line.split(" ");
                if(linea.length>0){
                    if( line.split(" ")[0].equals("terminal") && firtsTerminal){

                        bw.write("action code\n" +
                        "{:\n" +
                        "Writer writer = new Writer(\"./gramatica.txt\",\"./ascendent\",\""+cadena+"\",false);\n" +
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
                        if(noTerminal.length()>1){
                            noTerminal=noTerminal.substring(0,1)+noTerminal.substring(1,noTerminal.length()).toLowerCase();
                        }
                        createClass(noTerminal);
                    }
                    if (line.contains("::=") && !line.contains("writer")){
                        String[] symbols=line.split("::=")[1].split(" ");
                        line=line.split("::=")[0]+"::="+addNameToSymbols(symbols);
                    }
                    if (line.contains("|")){
                        String[] symbols=line.split("|")[1].split(" ");
                        line="|"+addNameToSymbols(symbols);

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
       
        File newFichero = new File("./production/"+s+".java");
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
                result+=" "+symbols[i];
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
                "    writer.addPasoTerminal("+"\""+terminal+"\""+", null, "+identifier+", "+terminal.substring(0,1)+");\n" +
                "    RESULT="+terminal.substring(0,1)+";\n" +
                ":};\n\n";
            }
            else{
                result+=terminal.toUpperCase()+" ::= "+terminal+":"+identifier+" {:\n" +
                "    "+className+" "+terminal.substring(0,1)+"=new "+className+"();\n"+ 
                "    writer.addPasoTerminal("+identifier+", null, "+terminal.substring(0,1)+");\n" +
                "    RESULT="+terminal.substring(0,1)+";\n" +
                ":};\n\n";
            }
        }
        return result;
    }

    private void writeAntlr(String grammar, String cadena, String destino) {
     
        File newFichero = new File(destino+"/production/gramatica.g4");
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
            Boolean firtsTerminal=true;
            Boolean isFirstNoTerminal=true;
            Boolean beforeFirtsNoTerminal=true;
            Boolean haveHeader=false;
            Boolean terminalsBegins=false;
            Boolean insideAction=false;
            Boolean insideNoTerminal=false;
            String antecedente="";
            Integer contCorch=0;
            while ((line = br.readLine()) != null) {
                String[] aux=line.split(" ");
                
                if(line.split(" ").length>0 ){
                    if(!isFirstNoTerminal && !insideAction && !insideNoTerminal){
                        line=modifyAntecedent(line);
                    }
                    if(line.contains("returns")){
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
                        "    Writer writer = new Writer(\"./gramatica.txt\",\"./descendent\",\""+cadena+"\",true);\n" +
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
                            "        writer.addPasoTerminalDes(\""+noTerminal+"\", \"vlex\", "+"Integer.parseInt(this._ctx.getText()), "+noTerminal+"O, haveBrother, nodeAnt);\n" +
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
                            "        writer.addPasoTerminalDes("+"this._ctx.getText()"+", null, "+noTerminal+"O, haveBrother, nodeAnt);\n" +
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
                        if(!insideAction)
                            insideAction=true;
                    }
                    if(line.contains(";")&&!insideAction){
                        insideNoTerminal=false;
                    }
                    if(!insideAction && insideNoTerminal && !line.contains("|")){

                       line=modifyProduction(line.split(" "));
                    }
                    if(line.contains(":")&&!insideAction){
                        insideNoTerminal=true;
                    }
                    
                    
                    if(line.contains("}")){
                        contCorch--;
                        if(contCorch==0)
                            insideAction=false;
                    }
                    
                    
//                    if(!line.equals("")&&line.substring(0,1).equals(" ")&&line.contains("[")){
//                        String[] simbols=line.split(" ");
//                        line=transformRule(simbols);
//                    }
    //                if(line.split(" ").length>0 && line.split(" ")[0].equals("terminal") /*&&line.contains(",")*/){
    //                    terminals.addAll(getTerminals(line));
    //                }
    //                if(line.split(" ").length>0 && line.split(" ")[0].equals("non") && firtsNonTerminal){
    //                    String newNoTerminals=newNoTerminals(terminals);
    //                    bw.write(newNoTerminals);
    //                    firtsNonTerminal=false;
    //                }
    //                if(line.split(" ").length>0 && line.split(" ")[0].equals("non") /*&&line.contains(",")*/){
    //                    String noTerminal=getNoTerminals(line);
    //                    noTerminals.add(noTerminal);
    //                    createClass(noTerminal);
    //                }
    //                if (line.contains("::=")){
    //                    String[] symbols=line.split("::=")[1].split(" ");
    //                    line=line.split("::=")[0]+"::="+addNameToSymbols(symbols);
    //                }
    //                if (line.contains("|")){
    //                    String[] symbols=line.split("|")[1].split(" ");
    //                    line="|"+addNameToSymbols(symbols);
    //
    //                }

    //                    else if(line.split(" ").length>0 && line.split(" ")[0].equals("terminal")){
    //                        terminals.add(line.substring(0,line.length()-1).split(" "))
    //                    }
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
        for(int i=0;i<line.length;i++){
            if(!line[i].equals("")){
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
                newLine+=line.split("returns")[0]+"[Node nodeAnt,Boolean haveBrother] returns "+line.split("returns")[1];
                line=newLine;
            }
        }
        return line;
    }
    
}
