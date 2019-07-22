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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adgao
 */
public class VisTDSApiXMLCreator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String ruta=args[0];/*"gramatica.g4";*/
        String cadena=args[1];/*"cadena.txt";*/
        String destino=args[2];/*"./";*/
        try {
            if(ruta.split("\\.")[1].equals("cup")){
                Annotator annotator=new Annotator(ruta, cadena, destino);
                Runtime.getRuntime().exec("java -jar java-cup-11b.jar "+destino+"/production/gramatica.cup");
                try {
                    Thread.sleep(330);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
                moveFilesCup(destino);
            }
            else{
                Annotator annotator=new Annotator(ruta, cadena, destino);
                Runtime.getRuntime().exec("java -jar antlr-4.7.2-complete.jar "+destino+"/production/gramatica.g4");
                
                try {
                    Thread.sleep(1175);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
                moveFilesANTLR(ruta.split("\\.")[0], destino);
            }
        } catch (IOException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void moveFilesANTLR(String nombreArchivo, String destino) {
        
       File interpFichero = new File(nombreArchivo+".interp");
       if(interpFichero.exists()){
           File interpFicheroDest = new File(destino+"/production/"+nombreArchivo+".interp");
           interpFicheroDest.delete();
           interpFichero.renameTo(interpFicheroDest);
           
        }
       File tokenFichero = new File(nombreArchivo+".tokens");
       if(tokenFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+".tokens");
           ficheroDest.delete();
           
           tokenFichero.renameTo(ficheroDest);
       }
       File baseListenerFichero = new File(nombreArchivo+"BaseListener.java");
       if(baseListenerFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"BaseListener.java");
           ficheroDest.delete();
           baseListenerFichero.renameTo(ficheroDest);
       }
       File lexerInterpFichero = new File(nombreArchivo+"Lexer.interp");
       if(lexerInterpFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"Lexer.interp");
           ficheroDest.delete();
           lexerInterpFichero.renameTo(ficheroDest);
       }
       File lexerFichero = new File(nombreArchivo+"Lexer.java");
       if(lexerFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"Lexer.java");
           ficheroDest.delete();
           lexerFichero.renameTo(ficheroDest);
       }
       File lexerTokensFichero = new File(nombreArchivo+"Lexer.tokens");
       if(lexerTokensFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"Lexer.tokens");
           ficheroDest.delete();
           lexerTokensFichero.renameTo(ficheroDest);
       }
       File listenerFichero = new File(nombreArchivo+"Listener.java");
       if(listenerFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"Listener.java");
           ficheroDest.delete();
           listenerFichero.renameTo(ficheroDest);
       }
       File parserFichero = new File(nombreArchivo+"Parser.java");
       if(parserFichero.exists()){
           File ficheroDest = new File(destino+"/production/"+nombreArchivo+"Parser.java");
           ficheroDest.delete();
           parserFichero.renameTo(ficheroDest);
       }
    }

    private static void moveFilesCup(String destino) {
   
      
       File fichero = new File("parser.java");
       if(fichero.exists()){
           File ficheroDest = new File(destino+"/production/"+"parser.java");
           ficheroDest.delete();
           fichero.renameTo(ficheroDest);
       }  
       fichero = new File("sym.java");
       if(fichero.exists()){
           File ficheroDest = new File(destino+"/production/"+"sym.java");
           ficheroDest.delete();
           fichero.renameTo(ficheroDest);
       }
    }
    
}
