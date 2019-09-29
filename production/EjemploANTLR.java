/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemploantlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author adgao
 */
public class EjemploANTLR {
    private static String cadena;
    public static void setChain(String chain){
        cadena=chain;
    } 
    public static String getChain(){
        return cadena;
    } 
    /**
     * @param args the command line arguments
     */
private static String cadena;
    public static void setChain(String chain){
        cadena=chain;
    } 
    public static String getChain(){
        return cadena;
    }
private static String cadena;
    public static void setChain(String chain){
        cadena=chain;
    } 
    public static String getChain(){
        return cadena;
    }
private static String cadena;
    public static void setChain(String chain){
        cadena=chain;
    } 
    public static String getChain(){
        return cadena;
    }
private static String cadena;
    public static void setChain(String chain){
        cadena=chain;
    } 
    public static String getChain(){
        return cadena;
    }
    public static void main(String[] args) {
        setChain(args[0]);
        setChain(args[0]);
        setChain(args[0]);
        setChain(args[0]);
        setChain(args[0]);
        
        CharStream in = CharStreams.fromString("2 *4+3;");
        gramaticaLexer lexer = new gramaticaLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        gramaticaParser parser = new gramaticaParser(tokens);
        parser.exp();
    }
    
}
