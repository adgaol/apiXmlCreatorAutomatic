if exist ".\production" (
    del ".\production" 
)
java -jar VisTDSApiXMLCreator.jar %1 

javac -cp ".;.\VisTDSApiXMLCreator.jar .;.\lib\java-cup-11b-runtime.jar .;.\lib\antlr-4.7.2-complete.jar" .\production\*.java
java -cp ".;.\VisTDSApiXMLCreator.jar .;.\lib\java-cup-11b-runtime.jar .;.\lib\antlr-4.7.2-complete.jar .;.\production" Main %2
move %1-5%XML.xml .\traductores
java -jar VisTDS.jar .\traductores\%1-5%XML.xml