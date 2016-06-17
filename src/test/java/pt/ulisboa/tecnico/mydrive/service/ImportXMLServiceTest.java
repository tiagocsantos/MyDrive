package pt.ulisboa.tecnico.mydrive.service;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;

public class ImportXMLServiceTest extends AbstractServiceTest {
	
	private MyDrive md;
	
	@Override
	protected void populate() {
		md = MyDrive.getInstance();
		
	}
		
	@Test
	public void xmlImport(){
		
		ImportXMLService impXML = new ImportXMLService("info/esxml.xml");
		impXML.execute();
		User tiago = md.getUserByUserName("tiago123");
		Directory d = tiago.getMainDir();
		Directory cenas = (Directory) d.getInsideDir("cenas");
		cenas.getInsideDir("textfile1");
		cenas.getInsideDir("app1");
		d.getInsideDir("link1");
		
	}
	
	@Test
	public void doubleXMLImport(){
		
		ImportXMLService impXML = new ImportXMLService("info/esxml.xml");
		impXML.execute();
		User tiago = md.getUserByUserName("tiago123");
		Directory d = tiago.getMainDir();
		Directory cenas = (Directory) d.getInsideDir("cenas");
		cenas.getInsideDir("textfile1");
		cenas.getInsideDir("app1");
		d.getInsideDir("link1");
		
		ImportXMLService impXML2 = new ImportXMLService("info/esxml2.xml");
		impXML2.execute();
		User mario = md.getUserByUserName("mario123");
		Directory d2 = mario.getMainDir();
		Directory pastateste = (Directory) d2.getInsideDir("pastateste");
		pastateste.getInsideDir("textfile1");
		pastateste.getInsideDir("app1");
		d2.getInsideDir("link1");
		
	}
	


}
