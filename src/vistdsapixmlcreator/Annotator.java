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
    

    public Annotator(String grammar, String cadena) {
        String[] type=grammar.split("\\.");
        File newFichero = new File("./production/gramaticaT.cup");
        HashSet<String> terminals=new HashSet<>();
        HashSet<String> noTerminals=new HashSet<>();
        try {
            newFichero.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(type[type.length-1].equals("cup")){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(grammar)));
                BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
                String line;
                Boolean firtsTerminal=true;
                Boolean firtsNonTerminal=true;
                while ((line = br.readLine()) != null) {
                    
                    String[] linea=line.split(" ");
                    if(line.split(" ").length>0 && line.split(" ")[0].equals("terminal") && firtsTerminal){
                        
                        bw.write("action code\n" +
                        "{:\n" +
                        "Writer writer = new Writer(\"./gramatica.txt\",\"./ascendent\",\""+cadena+"\",false);\n" +
                        ":}\n\n");
                        firtsTerminal=false;
                    }
                    if(line.split(" ").length>0 && line.split(" ")[0].equals("terminal") /*&&line.contains(",")*/){
                        terminals.addAll(getTerminals(line));
                    }
                    if(line.split(" ").length>0 && line.split(" ")[0].equals("non") && firtsNonTerminal){
                        String newNoTerminals=newNoTerminals(terminals);
                        bw.write(newNoTerminals);
                        firtsNonTerminal=false;
                    }
                    if(line.split(" ").length>0 && line.split(" ")[0].equals("non") /*&&line.contains(",")*/){
                        String noTerminal=getNoTerminals(line);
                        noTerminals.add(noTerminal);
                        createClass(noTerminal);
                    }
                    if (line.contains("::=")){
                        String[] symbols=line.split("::=")[1].split(" ");
                        line=line.split("::=")[0]+"::="+addNameToSymbols(symbols);
                    }
                    if (line.contains("|")){
                        String[] symbols=line.split("|")[1].split(" ");
                        line="|"+addNameToSymbols(symbols);
                        
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
    }

    private HashSet<String> getTerminals(String line) {
        HashSet<String> terminals=new HashSet<>();
        String[] auxLine=line.substring(8).split(" ");
        for(int i=1;i<auxLine.length;i++){
            if(auxLine[i].charAt(auxLine[i].length()-1)==','||auxLine[i].charAt(auxLine[i].length()-1)==';'){
                terminals.add(auxLine[i].substring(0,auxLine[i].length()-1));
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
            " * @author Adrián García Oller\n" +
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
            "    public Integer getValue() {\n" +
            "        return value;\n" +
            "    }\n" +
            "\n" +
            "    public void setValue(Integer value) {\n" +
            "        this.value = value;\n" +
            "    }\n" +
            "    \n" +
            "}");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private String addNameToSymbols(String[] symbols) {
       String result="";
       for(int i=1;i<symbols.length-1;i++){
           result+=" "+symbols[i]+":"+symbols[i].toLowerCase();
       }
       return result+" {:";
    }

    private String noTerminalsOfTerminals(HashSet<String> terminals) {
        String result="";
        for(String terminal:terminals){
            String className=terminal.substring(0,1).toUpperCase()+terminal.substring(1,terminal.length());
            result+=terminal.toUpperCase()+" ::= "+terminal+" {:\n" +
            "    "+className+" "+terminal.substring(0,1)+"=new "+className+"();\n" +
            "    "+terminal.substring(0,1)+".setValue("+terminal+");\n"+
            "    writer.addPasoTerminal("+terminal+", null, "+terminal.substring(0,1)+");\n" +
            "    RESULT="+terminal.substring(0,1)+";\n" +
            ":};\n\n";
        }
        return result;
    }
    
}
