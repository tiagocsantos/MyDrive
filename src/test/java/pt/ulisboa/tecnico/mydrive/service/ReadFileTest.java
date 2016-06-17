package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.mydrive.domain.*;
import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.exception.NotATextFileException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

@RunWith(JMockit.class)
public class ReadFileTest extends AbstractServiceTest{
	private long tokenmjps;
	private long tokenmiguel;
	private long tokenroot;
	private long tokenExpired;
	private long tokenNob;
	private long tokentiago123;

	
	private static final String pathEnv="/home/$USER/textfile1";
	private static  Files EnvFile;
	
	
	@Override
	protected void populate() {
		MyDrive	md = MyDrive.getInstance();
		
		User u = new User(md, "mjps", "Mario Jorge", "123456789");
		User u1 = new User(md, "Caneca", "Rui Caneca", "123456789");
		User u2 = new User(md,"miguel","Miguel Catarino", "123456789");
		User u3 = new User(md,"tiago123","Tiago", "tiago123");
		User root = md.getSuperuser();
		
		new Directory("dir", u.getMainDir(), u, md.getIDF());
		
		new FileText(u.getMainDir(),"ES","Bue Fixe!!!", u,md.getIDF() );
		new FileText(u.getMainDir(),"Tecnico","IST", u,md.getIDF() );
		new FileText(u.getMainDir(),"cadeira","", u,md.getIDF() );
		new FileText(u1.getMainDir(),"Test", "asd", u1, md.getIDF());
		new Link(u.getMainDir(),"link", "file", u, md.getIDF());
		new FileText(u.getMainDir(), "file", "cenas", u, md.getIDF());
		
		new Link(u3.getMainDir(),"linkEnv", pathEnv, u3, md.getIDF());
		EnvFile=new FileText(u3.getMainDir(),"textfile1", "SUCESS", u3, md.getIDF());
		
		Login l = new Login("mjps", "123456789", md, false);
		Login l1 = new Login("miguel", "123456789", md, false);
		Login lroot = new Login("root", "***", md, false);

		Login l3 = new Login("nobody","",md,false);


		Login tiago123=new Login("tiago123","tiago123",md,false);

		Login l2 = new Login("Caneca","123456789", md, true);
		lroot.setCurrentDir(u.getMainDir());
		l1.setCurrentDir(u.getMainDir());
		l3.setCurrentDir(u.getMainDir());
		tokenmjps = l.getToken(); 
		tokenmiguel = l1.getToken();
		tokenExpired = l2.getToken();
		tokentiago123=tiago123.getToken();
		tokenroot = lroot.getToken();
		tokenNob = l3.getToken();
	}
	
	private Files getFile(String name, String filename){
		User u = MyDriveService.getUser(name);
		return  u.getFile(filename); 
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		ReadFileService service = new ReadFileService(tokenNob, "Tecnico");
		service.execute(); 
	}
	
	@Test
	public void readFile(){
		ReadFileService service = new ReadFileService(tokenmjps, "Tecnico");
		service.execute();
	}
	
	@Test(expected = NotATextFileException.class)
	public void readDirectory(){
		ReadFileService service = new ReadFileService(tokenmjps, "dir");
		service.execute();
	}
	
	@Test(expected = FileDoesNotExistException.class)
	public void readfilenonexisting(){
		DeleteFileService service = new DeleteFileService(tokenmjps, "ficheiro");
		service.execute();
	}
	
	@Test(expected = InvalidSessionException.class)
	public void readInvalidSession(){
		ReadFileService service = new ReadFileService(tokenExpired, "Test");
		service.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void readFileWithoutPermission(){
		ReadFileService service = new ReadFileService(tokenmiguel, "ES");
		service.execute();
	}
	
	
	@Test
	public void contentfile(){
		ReadFileService service = new ReadFileService(tokenmjps, "ES");
		service.execute();
		Files f = getFile("mjps", "ES");
		assertEquals("Bue Fixe!!!", ((FileText) f).getContent());
	}
	
	@Test
	public void rootread(){
		ReadFileService service = new ReadFileService(tokenroot, "ES");
		service.execute();
		
		Files f = getFile("mjps", "ES");
		assertEquals("Bue Fixe!!!", ((FileText) f).getContent());
	}
	
	@Test
	public void emptyfile(){
		ReadFileService service = new ReadFileService(tokenmjps, "cadeira");
		service.execute();
		
		Files f = getFile("mjps", "cadeira");
		assertEquals("", ((FileText) f).getContent());
	}
	
	@Test
	public void readLink(){
		ReadFileService service = new ReadFileService(tokenmjps, "link");
		service.execute();
		
		String result = service.result();
		
		Files f = getFile("mjps", "file");
		assertEquals(result, ((FileText) f).getContent());		
	}
	
	@Test
	public void readLinkEnvironment(){
		
		new MockUp<Directory>(){
			@Mock
			Files processPath(String pathEnv){return EnvFile;}
		};
		
		ReadFileService rf=new ReadFileService(tokentiago123,"linkEnv");
		rf.execute();
		assertEquals(rf.result(),"SUCESS");
	}
}