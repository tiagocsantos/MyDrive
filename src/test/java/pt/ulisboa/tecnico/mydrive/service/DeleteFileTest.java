package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.domain.Files;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.FileDoesNotExistException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

public class DeleteFileTest extends AbstractServiceTest {
	private long tokencaneca;
	private long tokentiago;
	private long expiredtoken;
	private long tokenNob;
	
	@Override
	protected void populate() {
		MyDrive md = MyDrive.getInstance();
		
		User u = new User(md, "tiago", "Tiago Santos", "123456789");
		User u2 = new User(md, "caneca", "Rui Caneca", "123456789");
		
		new FileText(u.getMainDir(), "file", "nothin", u, md.getIDF());
		new Directory("emptydir", u.getMainDir(), u, md.getIDF());
		Directory dir = new Directory("dir", u.getMainDir(), u, md.getIDF());
		new Directory("dir2", dir, u, md.getIDF());
		new Directory("deir", u2.getMainDir(), u2, md.getIDF());
		
		Login l = new Login("tiago", "123456789", md, false);
		Login l2 = new Login("caneca", "123456789", md, false);
		Login l4 = new Login("nobody","",md,false);
		Login l3 = new Login("caneca", "123456789", md, true);
		
		System.out.println(l.getCurrentDir().checkExistence("emptydir"));
		l4.setCurrentDir(u.getMainDir());
		l2.setCurrentDir(u.getMainDir());
		tokentiago = l.getToken();
		tokencaneca = l2.getToken();
		expiredtoken = l3.getToken();
		tokenNob = l4.getToken();
	}

	private Files getFile(String name, String filename){
		User u = MyDriveService.getUser(name);
		return u.getFile(filename);
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		DeleteFileService service = new DeleteFileService(tokenNob, "file");
		service.execute(); 
	}
	
	@Test(expected = FileDoesNotExistException.class)
	public void deletenonexisting(){
		DeleteFileService service = new DeleteFileService(tokentiago, "filefilefile");
		service.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void deletehomedir(){
		DeleteFileService service = new DeleteFileService(tokentiago, "..");
		service.execute();
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void deleterootfile(){
		DeleteFileService service = new DeleteFileService(tokentiago, "..");
		service.execute();
		
	}

	@Test
	public void simplefile(){
		DeleteFileService service = new DeleteFileService(tokentiago, "file");
		service.execute();
		Files f = null;
		try{
		f = getFile("tiago", "file");
		}
		catch (Exception e){}
		assertNull("File was not removed", f);
	}
	
	@Test
	public void simpledir(){
		DeleteFileService service = new DeleteFileService(tokentiago, "emptydir");
		service.execute();
		Files f = null;
		try{
			f = getFile("tiago", "emptydir");
		}
		catch (Exception e){}
		assertNull("Directory was not removed", f);
	}
	
	@Test
	public void notemptydir(){
		DeleteFileService service = new DeleteFileService(tokentiago, "dir");
		service.execute();
		
		Files f = null; 
		Files f2 = null; 
		try{
			f = getFile("tiago", "dir");
			f2 = getFile("tiago", "dir2");
		}
		catch(Exception e){
			//DO NOTHING
		}
		assertNull("Directory was not removed", f);
		assertNull("Directory content was not Removed", f);
	}
	
	@Test(expected = PermissionDeniedException.class)
	public void notyourstodelete(){
		DeleteFileService service = new DeleteFileService(tokencaneca, "file");
		service.execute();
	}
	
	@Test(expected = InvalidSessionException.class)
	public void expiredSession(){
		DeleteFileService service = new DeleteFileService(expiredtoken, "deir");
		service.execute();
	}
}
