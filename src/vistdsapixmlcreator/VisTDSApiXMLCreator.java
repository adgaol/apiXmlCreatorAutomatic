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
        String ruta=/*args[0];*/"gramatica.cup";
        String cadena=/*args[1];*/"cadena.txt";
        try {
            if(ruta.split("\\.")[1].equals("cup")){
                Annotator annotator=new Annotator(ruta, cadena);
                Runtime.getRuntime().exec("java -jar java-cup-11b.jar gramatica.cup");
            }
            else{
                Annotator annotator=new Annotator(ruta, cadena);
            }
        } catch (IOException ex) {
            Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
