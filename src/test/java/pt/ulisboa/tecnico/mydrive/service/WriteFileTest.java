package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.mydrive.domain.*;
import pt.ulisboa.tecnico.mydrive.exception.*;

import mockit.Mock;
import mockit.MockUp;

@RunWith(JMockit.class)
public class WriteFileTest extends AbstractServiceTest {
	private long tokenSofia;
	private long tokenExpired;
	private long tokenRoot;
	private long tokenTiago;

	private long tokenNob;

	
	private static final String pathEnv="/home/$USER/textfile1";
	private static  Files EnvFile;
	

	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		User u = new User(md, "Sofia", "Sofia Martins", "123456789");
		User u2 = new User(md, "Caneca", "Rui Caneca", "123456789");
		User u3 = new User(md, "Tiago", "Tiago Santos", "123456789");
		User root=md.getSuperuser();
		
		
		new FileText(u.getMainDir(), "txt", "", u, md.getIDF());
		new App(u.getMainDir(), "appFile", "sfdsdf", u, md.getIDF());
		new Link(u.getMainDir(), "linkFile", "df/", u, md.getIDF());
		new FileText(u2.getMainDir(), "txt123", "", u2, md.getIDF());
		
		
		new Link(u.getMainDir(),"linkEnv", pathEnv, u, md.getIDF());
		EnvFile=new FileText(u.getMainDir(),"textfile1", "123", u, md.getIDF());
		
		Login l1 = new Login("Sofia", "123456789", md, false);
		Login l3 = new Login("Tiago", "123456789", md ,false);
		Login lroot=new Login("root","***",md,false);
		Login l4 = new Login("nobody","",md,false);
		Login l2 = new Login("Caneca", "123456789", md ,true);
		lroot.setCurrentDir(u.getMainDir());
		l4.setCurrentDir(u.getMainDir());		
		l3.setCurrentDir(u2.getMainDir());
		tokenSofia = l1.getToken();
		tokenTiago=l3.getToken();
		tokenExpired = l2.getToken();
		tokenRoot=lroot.getToken();
		tokenNob = l4.getToken();
	}

	private Files getFile(String name, String filename){
		User u = MyDriveService.getUser(name);
		return u.getFile(filename);
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		WriteFileService service = new WriteFileService(tokenNob, "appFile", "123456");
		service.execute(); 
	}
	
	@Test(expected = FileDoesNotExistException.class)
	public void writeNonExisting(){
		WriteFileService service = new WriteFileService(tokenSofia, "filetxt", "123456");
		service.execute();
	}
	
	@Test(expected = InvalidSessionException.class)
	public void writeInvalidSession(){
		WriteFileService service = new WriteFileService(tokenExpired, "txt123", "123456");
		service.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void writeFileWithoutPermission(){
		WriteFileService service = new WriteFileService(tokenTiago, "txt123", "123456");
		service.execute();
	}
	
	@Test(expected = NotATextFileException.class)
	public void writeANotTextFile(){
		WriteFileService service = new WriteFileService(tokenSofia, ".", "123456");
		service.execute();
	}
	
	@Test(expected = InconsistentContentException.class)
	public void writeInconsistentContentApp(){
		WriteFileService service = new WriteFileService(tokenSofia, "appFile", "app/appfile");
		service.execute();
	}

	@Test
	public void writeRoot(){
		WriteFileService service = new WriteFileService(tokenRoot, "txt", "123456");
		service.execute();
		
		Files f = getFile("Sofia", "txt");
	//	((FileText) f).setContent("123456");
		assertEquals("123456", ((FileText) f).getContent());	
	}
	
	
	@Test
	public void write(){
		WriteFileService service = new WriteFileService(tokenSofia, "appFile", "123456");
		service.execute();
		
		Files f = getFile("Sofia", "appFile");
		/*((FileText) f).setContent("123456");*/
		assertEquals("123456", ((FileText) f).getContent());	
	}
	
	@Test
	public void writeLinkEnvironment(){
		
		new MockUp<Directory>(){
			@Mock
			Files processPath(String pathEnv){return EnvFile;}
		};

		WriteFileService wf=new WriteFileService(tokenSofia,"linkEnv","SUCESS");
		wf.execute();
		Files result=getFile("Sofia","textfile1");
		assertEquals(result.readFile(),"SUCESS");
	}
	
	
}
