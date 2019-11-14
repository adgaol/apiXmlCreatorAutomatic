/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Adrian Garcia Oller
 */
public class Main{
   private static String cadena;
   public static void setChain(String chain){
       cadena=chain;
}

   public static String getChain(){
       return cadena;
   }
   public static void main(String argv[]) {

       setChain(argv[0]);
       if (argv.length == 0) {
           System.out.println("Inserta nombre de archivo"+"( Usage : java Analizador <inputfile> )");
       } else {
           for (int i = 0; i < argv.length; i++) {
               analizador_lexico lexico = null;
               try {
			lexico = new analizador_lexico( new java.io.FileReader(argv[i]));
			parser sintactico = new parser(lexico);
			sintactico.parse();
                   
			FileWriter fileWriter = new FileWriter(argv[i].substring(0, argv[i].length()-2)+".pas");
                       PrintWriter pw = new PrintWriter(fileWriter);
			fileWriter.close();
               }
               catch (java.io.FileNotFoundException e) {
			System.out.println("Archivo "+"argv[i]"+" no encontrado.");
               }
               catch (java.io.IOException e) {
			System.out.println("Error durante la lectura del archivo: " +argv[i]+".");
			e.printStackTrace();
               }
               catch (Exception e) {
			System.out.println("Excepcion:");
			e.printStackTrace();
               }
           }
       }
   }
}
