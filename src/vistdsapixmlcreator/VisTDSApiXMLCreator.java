/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistdsapixmlcreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adgao
 */
public class VisTDSApiXMLCreator {

    /**antlr-4.7.2-complete.jar
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            File directorio = new File(".\\production");
            if(!directorio.exists())
                directorio.mkdir();
            else{
                deleteFiles(directorio.listFiles());
                directorio.delete();
                directorio.mkdir();
            }
            String ruta=args[0];/*"gramatica.g4";*/
            String destino=".\\production";
            String main="Main.java";//args[1];
            String nombreArchivoEntrada=ruta.split("\\.")[0];
            String extension=ruta.split("\\.")[1];
            
            
//            Random numaleatorio = new Random(3816L);    
//            File oldFichero = new File(main+".java");
//            File newFichero = new File(main+Math.abs(numaleatorio.nextInt())+".java");
//            BufferedReader br;
//            Boolean yaEstaChain=false;
//            try {
//                br = new BufferedReader(new FileReader(main+".java"));
//                BufferedWriter bw=null;
//                try {
//                    bw = new BufferedWriter(new FileWriter(newFichero));
//                } catch (IOException ex) {
//                    Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                String linea;
//                try {
//                    while((linea=br.readLine()) != null)
//                    {
//                        if(linea.contains("private static String cadena;"))
//                            yaEstaChain=true;
//                        if(!yaEstaChain&&linea.contains("public static void main"))
//                        {
//                            linea="private static String cadena;\n" +
//                            "    public static void setChain(String chain){\n" +
//                            "        cadena=chain;\n" +
//                            "    } \n" +
//                            "    public static String getChain(){\n" +
//                            "        return cadena;\n" +
//                            "    }\n"+linea+"\n" +
//                            "        setChain(args[0]);";
//
//                        }
//                        bw.write(linea+"\n");
//
//                    }
//                    bw.close();
//                    br.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//
//
//
//                    // Capturo el nombre del fichero antiguo
//
//                    // Borro el fichero antiguo
//                    if(oldFichero.exists()){               
//                        oldFichero.delete();
//                    }
//                    //Renombro el fichero auxiliar con el nombre del fichero antiguo
//                    newFichero.renameTo(oldFichero);
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
//            }

//            ---------------------------------------------------------------------------------------
//            try {
//                if(extension.equals("cup")){
//                    File newFichero = new File("main.java");
//            
//            BufferedWriter bw=null;
//            try {
//                bw = new BufferedWriter(new FileWriter(newFichero));
//            } catch (IOException ex) {
//                Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            String linea;
//            try {
//
//
//                linea="";
//
//
//                bw.write(linea+"\n");
//
//
//                bw.close();
//
//            } catch (IOException ex) {
//                Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
//            }
              try {
                if(extension.equals("cup")){
                    
            
                    createMain(ruta,destino);
                    Annotator annotator=new Annotator(ruta, destino, nombreArchivoEntrada+"XML", main);
                    Process process=Runtime.getRuntime().exec("java -jar ./lib/java-cup-11b.jar "+destino+"/"+ruta);
                     try {
                        process.waitFor();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (process.exitValue() != 0) {
                        System.out.println();
                        InputStream errorStream = process.getErrorStream();
                        int c = 0;
                        while ((c = errorStream.read()) != -1) {
                            System.out.print((char)c);
                        }
                    }
                    try {
                        Thread.sleep(330);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    moveFilesCup(destino);
                }
                else{
                    createMain(ruta,destino);
                    Annotator annotator=new Annotator(ruta, destino, ruta.split("\\.")[0]+"XML", main);
                    
                    Process process=Runtime.getRuntime().exec("java -jar "+"./lib/antlr-4.7.2-complete.jar "+destino+"/"+ruta);
                    try {
                        process.waitFor();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VisTDSApiXMLCreator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (process.exitValue() != 0) {
                        System.out.println();
                        InputStream errorStream = process.getErrorStream();
                        int c = 0;
                        while ((c = errorStream.read()) != -1) {
                            System.out.print((char)c);
                        }
                    }
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

    
    private static void createMain(String gramatica, String destino){
        String extension=gramatica.split("\\.")[1];
        String nombreArchivoEntrada=gramatica.split("\\.")[0];
        String nombreArchivoMain="Main";
        File newFichero = new File(destino+"/"+nombreArchivoMain+".java");
        try {
            newFichero.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        if(extension.equals("cup")){    
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
                bw.write("/*\n" +
                " * To change this license header, choose License Headers in Project Properties.\n" +
                " * To change this template file, choose Tools | Templates\n" +
                " * and open the template in the editor.\n" +
                " */\n" +
                "import java.io.FileWriter;\n" +
                "import java.io.PrintWriter;\n" +
                "\n" +
                "/**\n" +
                " *\n" +
                " * @author Adrian Garcia Oller\n" +
                " */\n" +
                "public class "+nombreArchivoMain+"{\n"+
                "   private static String cadena;\n"+
                "   public static void setChain(String chain){\n"+
                "       cadena=chain;\n"+
                "}\n"+
                "\n"+
                "   public static String getChain(){\n"+
                "       return cadena;\n"+
                "   }\n"+ 
                "   public static void main(String argv[]) {\n"+
                "\n"+
                "       setChain(argv[0]);\n"+
                "       if (argv.length == 0) {\n"+
                "           System.out.println(\"Inserta nombre de archivo\"+\"( Usage : java Analizador <inputfile> )\");\n"+
                "       } else {\n"+
                "           for (int i = 0; i < argv.length; i++) {\n"+
                "               analizador_lexico lexico = null;\n"+
                "               try {\n"+
                "			lexico = new analizador_lexico( new java.io.FileReader(argv[i]));\n"+
                "			parser sintactico = new parser(lexico);\n"+
                "			sintactico.parse();\n"+
                "                   \n"+                    
                "			FileWriter fileWriter = new FileWriter(argv[i].substring(0, argv[i].length()-2)+\".pas\");\n"+
                "                       PrintWriter pw = new PrintWriter(fileWriter);\n"+
                "			fileWriter.close();\n"+
                "               }\n"+
                "               catch (java.io.FileNotFoundException e) {\n"+
                "			System.out.println(\"Archivo \"+\"argv[i]\"+\" no encontrado.\");\n"+
                "               }\n"+
                "               catch (java.io.IOException e) {\n"+
                "			System.out.println(\"Error durante la lectura del archivo: \" +argv[i]+\".\");\n"+
                "			e.printStackTrace();\n"+
                "               }\n"+
                "               catch (Exception e) {\n"+
                "			System.out.println(\"Excepcion:\");\n"+
                "			e.printStackTrace();\n"+
                "               }\n"+
                "           }\n"+
                "       }\n"+
                "   }\n"+
                "}\n");
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            
        }
        else{
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(newFichero));
                bw.write("/*\n" +
                " * To change this license header, choose License Headers in Project Properties.\n" +
                " * To change this template file, choose Tools | Templates\n" +
                " * and open the template in the editor.\n" +
                " */\n" +
                "import java.io.IOException;"+        
                "import org.antlr.v4.runtime.CharStream;\n" +
                "import org.antlr.v4.runtime.CharStreams;\n" +
                "import org.antlr.v4.runtime.CommonTokenStream;\n" +
                "\n" +
                "/**\n" +
                " *\n" +
                " * @author Adrian Garcia Oller\n" +
                " */\n" +
                "public class "+nombreArchivoMain+" {\n"+
                "   private static String cadena;\n"+
                "   public static void setChain(String chain){\n"+
                "       cadena=chain;\n"+
                "}\n"+
                "\n"+
                "   public static String getChain(){\n"+
                "       return cadena;\n"+
                "   }\n"+ 
                "   public static void main(String argv[]) {\n"+
                "\n"+
                "        setChain(argv[0]);\n"+
                "        CharStream in=null;\n" +
                "        try {\n" +
                "            in = CharStreams.fromFileName(argv[0]);\n" +
                "        } catch (IOException ex) {\n" +
                "        }" +
                "        "+nombreArchivoEntrada+"Lexer lexer = new "+nombreArchivoEntrada+"Lexer(in);\n" +
                "        CommonTokenStream tokens = new CommonTokenStream(lexer);\n" +
                "        "+nombreArchivoEntrada+"Parser parser = new "+nombreArchivoEntrada+"Parser(tokens);\n" +
                "        parser.exp();\n"+
                "   }\n"+       
                "}\n");
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Annotator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void moveFilesANTLR(String nombreArchivo, String destino) {
        
       File interpFichero = new File(nombreArchivo+".interp");
       if(interpFichero.exists()){
           File interpFicheroDest = new File(destino+"/"+nombreArchivo+".interp");
           interpFicheroDest.delete();
           interpFichero.renameTo(interpFicheroDest);
           
        }
       File tokenFichero = new File(nombreArchivo+".tokens");
       if(tokenFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+".tokens");
           ficheroDest.delete();
           
           tokenFichero.renameTo(ficheroDest);
       }
       File baseListenerFichero = new File(nombreArchivo+"BaseListener.java");
       if(baseListenerFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"BaseListener.java");
           ficheroDest.delete();
           baseListenerFichero.renameTo(ficheroDest);
       }
       File lexerInterpFichero = new File(nombreArchivo+"Lexer.interp");
       if(lexerInterpFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"Lexer.interp");
           ficheroDest.delete();
           lexerInterpFichero.renameTo(ficheroDest);
       }
       File lexerFichero = new File(nombreArchivo+"Lexer.java");
       if(lexerFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"Lexer.java");
           ficheroDest.delete();
           lexerFichero.renameTo(ficheroDest);
       }
       File lexerTokensFichero = new File(nombreArchivo+"Lexer.tokens");
       if(lexerTokensFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"Lexer.tokens");
           ficheroDest.delete();
           lexerTokensFichero.renameTo(ficheroDest);
       }
       File listenerFichero = new File(nombreArchivo+"Listener.java");
       if(listenerFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"Listener.java");
           ficheroDest.delete();
           listenerFichero.renameTo(ficheroDest);
       }
       File parserFichero = new File(nombreArchivo+"Parser.java");
       if(parserFichero.exists()){
           File ficheroDest = new File(destino+"/"+nombreArchivo+"Parser.java");
           ficheroDest.delete();
           parserFichero.renameTo(ficheroDest);
       }
    }

    private static void moveFilesCup(String destino) {
   
      
       File fichero = new File("parser.java");
       if(fichero.exists()){
           File ficheroDest = new File(destino+"/"+"parser.java");
           ficheroDest.delete();
           fichero.renameTo(ficheroDest);
       }  
       fichero = new File("sym.java");
       if(fichero.exists()){
           File ficheroDest = new File(destino+"/"+"sym.java");
           ficheroDest.delete();
           fichero.renameTo(ficheroDest);
       }
    }

    private static void deleteFiles(File[] listFiles) {
        for (int i=0;i<listFiles.length;i++){
            listFiles[i].delete();
        }
    }
    
}
