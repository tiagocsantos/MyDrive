package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.*;
import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidPathException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.exception.NotADirectoryException;

public class ChangeDirectoryServiceTest extends AbstractServiceTest {

	private long tokenmiguel;
	private long tokenmiguelexpired;
	private Login l1;
	private Directory dir1;
	
	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		User user = new User(md, "miguel", "Miguel Catarino", "12345678");
		
		new FileText(user.getMainDir(), "file", "nothin", user, md.getIDF());
		Directory dir = new Directory("dir", user.getMainDir(), user, md.getIDF());
		dir1 = new Directory("dir1", dir, user, md.getIDF());

		
		l1 = new Login("miguel", "12345678", md, false);
		Login l2 = new Login("miguel", "12345678", md, true);
		tokenmiguel = l1.getToken();
		tokenmiguelexpired = l2.getToken();
	}
	
	@Test (expected = InvalidSessionException.class)
	public void expiredSession(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguelexpired, "/home/root");
		service.execute();
	}
	
	@Test (expected = InvalidPathException.class)
	public void invalidPath(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, "\\home");
		service.execute();
	}
	
	@Test (expected = FileDoesNotExistException.class)
	public void directoryDoesNotExist(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, "naoexiste");
		service.execute();
	}
	
	@Test (expected = NotADirectoryException.class)
	public void notADirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, "file");
		service.execute();
	}
	
	@Test 
	public void changeToActualDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, ".");
		service.execute();
		String result = service.result();
		assertEquals(l1.getCurrentDir().getPath(),result);
	}
	
	@Test 
	public void changeToPreviousDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, "..");
		service.execute();
		String result = service.result();
		assertEquals("/home",result);
	}
	
	@Test 
	public void changeToDirectory(){
		ChangeDirectoryService service = new ChangeDirectoryService(tokenmiguel, "dir/dir1");
		service.execute();
		String result = service.result();
		assertEquals(dir1.getPath(),result);
	}
}