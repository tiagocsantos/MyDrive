package pt.ulisboa.tecnico.mydrive.service;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.exception.MyDriveException;

public class ImportXMLService extends MyDriveService {
	
	private File fich;
	
	public ImportXMLService(String nomeFicheiro){

		fich = new File(nomeFicheiro);
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md  = getMyDrive();
		SAXBuilder builder = new SAXBuilder();
  	  	try {
  	  		Document document = (Document)builder.build(fich);
  	  		md.xmlImport(document.getRootElement());
  	  	}
  	  	catch (IOException io) {
  			System.out.println(io.getMessage());
  	  	} 
  	  	catch (JDOMException jdomex) {
  	  		System.out.println(jdomex.getMessage());
  	  	}
	}
}
