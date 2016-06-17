package pt.ulisboa.tecnico.mydrive.service;

import static org.junit.Assert.*;

import java.nio.file.InvalidPathException;

import org.junit.Test;

import pt.ulisboa.tecnico.mydrive.domain.App;
import pt.ulisboa.tecnico.mydrive.domain.Directory;
import pt.ulisboa.tecnico.mydrive.domain.FileText;
import pt.ulisboa.tecnico.mydrive.domain.Files;
import pt.ulisboa.tecnico.mydrive.domain.Link;
import pt.ulisboa.tecnico.mydrive.domain.Login;
import pt.ulisboa.tecnico.mydrive.domain.MyDrive;
import pt.ulisboa.tecnico.mydrive.domain.User;
import pt.ulisboa.tecnico.mydrive.exception.FileExistsException;
import pt.ulisboa.tecnico.mydrive.exception.InconsistentContentException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidArgContentException;
import pt.ulisboa.tecnico.mydrive.exception.InvalidSessionException;
import pt.ulisboa.tecnico.mydrive.exception.PermissionDeniedException;

public class CreateFileServiceTest extends AbstractServiceTest {
	private long tokenFalse;
	private long tokenTrue;
	private long token3;
	private long tokenNob;
	
	private MyDrive md;
	
	@Override
	protected void populate() {
		md = MyDrive.getInstance();
		User user = new User(md, "sofia", "Sofia Martins", "12345678");
		new User(md, "mjps", "mjps", "12345678");
		
		new FileText(user.getMainDir(), "txt", "", user, md.getIDF());
		new Link(user.getMainDir(), "link2", "/home/sofia/link", user, md.getIDF());
		
		Login l1 = new Login("sofia", "12345678", md, false);
		Login l3 = new Login("mjps", "12345678", md , false);
		Login l4 = new Login("nobody","",md,false);
		Login l2 = new Login("sofia", "12345678", md, true);
		
		l4.setCurrentDir(user.getMainDir());
		l3.setCurrentDir(user.getMainDir());
		tokenFalse = l1.getToken();
		tokenTrue = l2.getToken();
		token3 = l3.getToken();
		tokenNob =l4.getToken();
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void testNobody(){
		CreateFileService service = new CreateFileService(tokenNob,"txt1","app");
		service.execute(); 
	}
	
	@Test (expected=FileExistsException.class)
	public void CreateExisting(){
		CreateFileService lis = new CreateFileService(tokenFalse,"txt","app");
		lis.execute();
	
	}
	
	@Test (expected=PermissionDeniedException.class)
	public void CreateFileWithoutPermission(){
		CreateFileService lis = new CreateFileService(token3,"newfile","textfile", "conrent");
		lis.execute();
		
	}
		
	@Test (expected = InvalidSessionException.class)
	public void CreateInvalidSession(){
		CreateFileService service = new CreateFileService(tokenTrue, "txt1", "dir");
		service.execute();
		
	}
	
	@Test
	public void CreateLink(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt2", "link", "dir/dir/dir");
		service.execute();
		Files f = md.getLogin(tokenFalse).getCurrentDir().getInsideDir("txt2");
		assertNotNull("ficheiro nao foi criado",f);	
		
	}
	
	@Test
	public void CreateApp(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt3", "app");
		service.execute();
		Files f = md.getLogin(tokenFalse).getCurrentDir().getInsideDir("txt3");
		assertNotNull("ficheiro nao foi criado",f);	
		
	}
	
	@Test
	public void CreateDir(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt4", "dir");
		service.execute();
		Files f = md.getLogin(tokenFalse).getCurrentDir().getInsideDir("txt4");
		assertNotNull("ficheiro nao foi criado",f);	
		
	}
	
	@Test
	public void CreateFileText(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt5", "textfile");
		service.execute();
		Files f = md.getLogin(tokenFalse).getCurrentDir().getInsideDir("txt5");
		assertNotNull("ficheiro nao foi criado",f);	
		
	}
	
	@Test (expected = InvalidArgContentException.class)
	public void CreateDirWithContent(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt6", "dir", "content");
		service.execute();
			
	}
	
	@Test
	public void CreateFileWithContent(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt7", "textfile", "content");
		service.execute();
		Files f = md.getLogin(tokenFalse).getCurrentDir().getInsideDir("txt7");
		assertNotNull("ficheiro nao foi criado",f);	
			
	}
	
	@Test (expected = InconsistentContentException.class)
	public void CreateLinkWithInconsistContent(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt8", "link", ".dasd////");
		service.execute();
			
	}
	
	@Test (expected = InconsistentContentException.class)
	public void CreateAppWithInconsistContent(){
		CreateFileService service = new CreateFileService(tokenFalse, "txt9", "app", "content/ddhshs");
		service.execute();
			
	}
	
	@Test (expected = InconsistentContentException.class)
	public void CreateLinkwithSimpleloop(){
		CreateFileService service = new CreateFileService(tokenFalse, "link1", "link", "/home/sofia/link1");
		service.execute();
	}
	
//	@Test (expected = InconsistentContentException.class)
//	public void CreateLinkwithComplexloop(){
//		CreateFileService service = new CreateFileService(tokenFalse, "link", "link", "/home/sofia/link2");
//		service.execute();
//	}
	

	}
