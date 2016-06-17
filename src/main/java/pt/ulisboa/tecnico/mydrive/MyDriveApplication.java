package pt.ulisboa.tecnico.mydrive;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.io.File;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.mydrive.domain.*;



public class MyDriveApplication {
    static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
    	System.out.println("*** Welcome to the MyDrive application! ***");
    	try{
    		setup();
    		for (String s: args) xmlScan(new File(s));
    		//test();
    	}
    	finally{
    		FenixFramework.shutdown();
    	}
    }
    
    @Atomic
    public static void setup() { // mydrive with debug data
        log.trace("Setup: " + FenixFramework.getDomainRoot());
        MyDrive.getInstance();
    }
    
    @Atomic
    public static void xmlScan(File file){
    	log.trace("xmlScan: " + FenixFramework.getDomainRoot());
    	MyDrive md = MyDrive.getInstance();
    	SAXBuilder builder = new SAXBuilder();
   	  	
  	  	try {
  	  		Document document = (Document)builder.build(file);
  	  		md.xmlImport(document.getRootElement());
  	  	}
  	  	catch (IOException io) {
  			System.out.println(io.getMessage());
  	  	} 
  	  	catch (JDOMException jdomex) {
  	  		System.out.println(jdomex.getMessage());
  	  	}
    }

    @Atomic
    public static void test(){
     MyDrive md = MyDrive.getInstance();
     Iterator<User> users = md.getUserSet().iterator();
     String content = "Lista de Utilizadores:";
     User user;
     while(users.hasNext()){
    	 content+="\n";
    	 user = users.next();
    	 content += user.getUsername();
     }
     md.createFile("/home", "README", content);
     md.createDirectory("/usr/local/bin");
     md.readContent("/home/README");
     md.deleteFile("/usr/local/bin");
    xmlPrint();
     md.deleteFile("/home/README");
     md.ListDirectory("/home");

    }
    
    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
        Document doc = MyDrive.getInstance().exportxml();
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
        try { xmlOutput.output(doc, new PrintStream(System.out));
	} catch (IOException e) { System.out.println(e); }
    }

    @Atomic
    public static void init() { 
        log.trace("Init: " + FenixFramework.getDomainRoot());
        MyDrive.getInstance().cleanup();
    }
}
